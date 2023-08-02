package com.heima.api.schedule;

import com.heima.model.common.dto.ResponseResult;
import com.heima.model.schedule.dto.Task;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author Fu Qiujie
 * @since 2023/8/2
 */

@FeignClient(value = "leadnews-schedule")
public interface ScheduleClient {
    /**
     * 添加任务
     *
     * @param task 任务对象
     * @return 任务id
     */
    @PostMapping("/api/v1/task/add")
    ResponseResult<Long> addTask(@RequestBody Task task);

    /**
     * 取消任务
     *
     * @param taskId 任务id
     * @return 取消结果
     */
    @GetMapping("/api/v1/task/cancel/{taskId}")
    ResponseResult<?> cancelTask(@PathVariable("taskId") long taskId);

    /**
     * 按照类型和优先级来拉取任务
     *
     * @param taskType
     * @param priority
     * @return
     */
    @GetMapping("/api/v1/task/poll/{taskType}/{priority}")
    ResponseResult<Task> poll(@PathVariable("taskType") int taskType, @PathVariable("priority") int priority);
}