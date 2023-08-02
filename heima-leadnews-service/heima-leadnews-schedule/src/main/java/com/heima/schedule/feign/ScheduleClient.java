package com.heima.schedule.feign;

import com.heima.model.common.dto.ResponseResult;
import com.heima.model.schedule.dto.Task;
import com.heima.schedule.service.TaskService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author Fu Qiujie
 * @since 2023/8/2
 */

@RestController
@RequestMapping("/api/v1/task")
public class ScheduleClient {

    @Resource
    private TaskService taskService;

    @PostMapping("/add")
    public ResponseResult<Long> addTask(@RequestBody Task task) {
        return ResponseResult.okResult(taskService.addTask(task));
    }

    @GetMapping("/cancel/{taskId}")
    public ResponseResult<?> cancelTask(@PathVariable("taskId") Long taskId) {
        taskService.cancelTask(taskId);
        return ResponseResult.okResult(null);
    }

    @GetMapping("/poll/{taskType}/{priority}")
    public ResponseResult<Task> pollTask(@PathVariable("taskType") Integer taskType, @PathVariable("priority") Integer priority) {
        return ResponseResult.okResult(taskService.pollTask(taskType, priority));
    }
}
