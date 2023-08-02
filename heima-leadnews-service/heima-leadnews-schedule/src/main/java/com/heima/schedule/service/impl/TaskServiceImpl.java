package com.heima.schedule.service.impl;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import cn.hutool.core.bean.BeanUtil;
import com.heima.common.constants.ScheduleConstants;
import com.heima.common.redis.CacheService;
import com.heima.model.schedule.dto.Task;
import com.heima.model.schedule.entity.Taskinfo;
import com.heima.model.schedule.entity.TaskinfoLogs;
import com.heima.schedule.service.TaskService;
import com.heima.schedule.service.TaskinfoLogsService;
import com.heima.schedule.service.TaskinfoService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author Fu Qiujie
 * @since 2023/8/1
 */
@Service
public class TaskServiceImpl implements TaskService {

    @Resource
    private TaskinfoService taskinfoService;

    @Resource
    private TaskinfoLogsService taskinfoLogsService;

    @Resource
    private CacheService cacheService;

    @Override
    @Transactional
    public Long addTask(Task task) {
        // 将Task保存到数据库
        boolean success = saveTaskToDB(task);
        if (!success) {
            return null;
        }
        // 将Task保存到Redis
        saveTaskToRedis(task);
        return task.getTaskId();
    }

    @Override
    public void cancelTask(Long taskId) {
        // 更新数据库
        Task task = updateTaskInDB(taskId, ScheduleConstants.CANCELLED);
        // 更新Redis
        updateTaskInRedis(task);
    }

    @Override
    public Task pollTask(Integer taskType, Integer priority) {
        String key = taskType + ":" + priority;
        Task task = cacheService.lRightPop(ScheduleConstants.TOPIC + key, Task.class);
        if (task == null) {
            return null;
        }
        // 更新数据库
        task = updateTaskInDB(task.getTaskId(), ScheduleConstants.EXECUTED);
        return task;
    }

    @Override
    @Scheduled(cron = "0 */1 * * * ?")
    public void refresh() {
        String lock = cacheService.tryLock(ScheduleConstants.REFRESH_LOCK, 30, TimeUnit.SECONDS);
        if (lock == null) {
            return;
        }
        // 获取待执行的任务的key
        Set<String> keys = cacheService.scan(ScheduleConstants.FUTURE + "*");
        // 取出需要立即执行的任务
        keys.forEach(key -> {
            String topicKey = ScheduleConstants.TOPIC + key.split(ScheduleConstants.FUTURE)[1];
            Set<String> tasks = cacheService.zRangeByScore(key, 0, System.currentTimeMillis());
            if (CollectionUtils.isEmpty(tasks)) {
                return;
            }
            cacheService.refreshWithPipeline(key, topicKey, tasks);
        });
    }

    @Override
    @PostConstruct
    @Scheduled(cron = "0 */5 * * * ?")
    public void syncDBtoRedis() {
        // 清理Redis
        clearTaskInRedis();
        // 查询小于未来5分钟的所有任务
        List<Taskinfo> taskinfos = taskinfoService.lambdaQuery()
                .lt(Taskinfo::getExecuteTime, LocalDateTime.now().plus(5, ChronoUnit.MINUTES))
                .list();
        if (CollectionUtils.isEmpty(taskinfos)) {
            return;
        }
        // 将任务保存到Redis
        taskinfos.forEach(taskinfo -> {
            Task task = BeanUtil.copyProperties(taskinfo, Task.class);
            task.setExecuteTime(taskinfo.getExecuteTime()
                    .atZone(ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli());
            saveTaskToRedis(task);
        });
    }

    private void clearTaskInRedis() {
        Set<String> keys = cacheService.scan(ScheduleConstants.FUTURE + "*");
        keys.addAll(cacheService.scan(ScheduleConstants.TOPIC + "*"));
        cacheService.delete(keys);
    }

    private void updateTaskInRedis(Task task) {
        String key = task.getTaskType() + ":" + task.getPriority();
        long executeTime = task.getExecuteTime();
        // 判断待取消任务是否需要立即执行
        if (executeTime <= System.currentTimeMillis()) {
            // 立即执行
            cacheService.lRemove(ScheduleConstants.TOPIC + key, 0, task);
        } else {
            // 非立即执行
            cacheService.zRemove(ScheduleConstants.FUTURE + key, task);
        }
    }

    private Task updateTaskInDB(Long taskId, int status) {
        taskinfoService.removeById(taskId);
        TaskinfoLogs taskinfoLogs = taskinfoLogsService.getById(taskId);
        taskinfoLogs.setStatus(status);
        taskinfoLogsService.updateById(taskinfoLogs);
        Task task = BeanUtil.copyProperties(taskinfoLogs, Task.class);
        task.setExecuteTime(taskinfoLogs.getExecuteTime()
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli());
        return task;
    }

    private void saveTaskToRedis(Task task) {
        String key = task.getTaskType() + ":" + task.getPriority();
        long executeTime = task.getExecuteTime();
        long nextScheduleTime = Instant.now().plus(5, ChronoUnit.MINUTES).toEpochMilli();
        // 判断是否需要立即执行
        if (executeTime <= System.currentTimeMillis()) {
            // 立即执行
            cacheService.lLeftPush(ScheduleConstants.TOPIC + key, task);
        } else if (executeTime < nextScheduleTime) {
            // 非立即执行, 且在5分钟内执行
            cacheService.zAdd(ScheduleConstants.FUTURE + key, task, executeTime);
        }
    }

    private boolean saveTaskToDB(Task task) {
        Taskinfo taskinfo = BeanUtil.copyProperties(task, Taskinfo.class);
        taskinfo.setExecuteTime(LocalDateTime
                .ofInstant(Instant.ofEpochMilli(task.getExecuteTime()), ZoneId.systemDefault()));
        boolean success = taskinfoService.save(taskinfo);
        task.setTaskId(taskinfo.getTaskId());
        TaskinfoLogs taskinfoLogs = BeanUtil.copyProperties(taskinfo, TaskinfoLogs.class);
        taskinfoLogs.setVersion(1);
        taskinfoLogs.setStatus(ScheduleConstants.SCHEDULED);
        success &= taskinfoLogsService.save(taskinfoLogs);
        return success;
    }
}
