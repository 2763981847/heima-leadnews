package com.heima.model.user;

import lombok.Data;

import java.util.PriorityQueue;

/**
 * @author Fu Qiujie
 * @since 2023/8/16
 */
@Data
public class UserRelationDto {
    private Long articleId;
    private Integer authorId;
    private Short operation;
}
