package com.heima.schedule.service;


import com.heima.model.schedule.dto.Task;
import com.heima.schedule.ScheduleApplication;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Fu Qiujie
 * @since 2023/8/1
 */
@SpringBootTest(classes = ScheduleApplication.class)
@RunWith(SpringRunner.class)
class TaskServiceTest {

    @Resource
    private TaskService taskService;

    @Test
    void addTask() {
        Task task = new Task();
        task.setTaskType(0);
        task.setPriority(0);
        task.setExecuteTime(System.currentTimeMillis() + 10000);
        task.setParameters("test".getBytes());
        Long taskId = taskService.addTask(task);
        System.out.println(taskId);
    }

    @Test
    void cancelTask() {
        taskService.cancelTask(1686326610922549250L);
    }

    @Test
    void pollTask() {
        Task task = taskService.pollTask(0, 0);
        System.out.println(task);
    }
}