package com.heima.schedule.service;

import com.heima.model.schedule.dto.Task;

/**
 * @author Fu Qiujie
 * @since 2023/8/1
 */
public interface TaskService {
    Long addTask(Task task);

    void cancelTask(Long taskId);

    Task pollTask(Integer taskType, Integer priority);

    void refresh();

    void syncDBtoRedis();
}
