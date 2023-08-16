package com.heima.model.behavior.dto;

import lombok.Data;

/**
 * @author Fu Qiujie
 * @since 2023/8/16
 */
@Data
public class LikesBehaviorDto {
    private Long articleId;
    private Short operation;
    private Short type;
}
