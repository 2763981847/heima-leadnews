package com.heima.model.schedule.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

/**
 * @TableName taskinfo_logs
 */
@TableName(value = "taskinfo_logs")
@Data
public class TaskinfoLogs implements Serializable {
    /**
     * 任务id
     */
    @TableId(value = "task_id")
    private Long taskId;

    /**
     * 执行时间
     */
    @TableField(value = "execute_time")
    private LocalDateTime executeTime;

    /**
     * 优先级
     */
    @TableField(value = "priority")
    private Integer priority;

    /**
     * 任务类型
     */
    @TableField(value = "task_type")
    private Integer taskType;

    /**
     * 版本号,用乐观锁
     */
    @TableField(value = "version")
    @Version
    private Integer version;

    /**
     * 状态 0=初始化状态 1=EXECUTED 2=CANCELLED
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 参数
     */
    @TableField(value = "parameters")
    private byte[] parameters;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}