package com.heima.model.behavior.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Fu Qiujie
 * @since 2023/8/16
 */
@Data
public class CollectionBehaviorDto {

    private Long entryId;
    private Short operation;
    private LocalDateTime publishedTime;
    private Short type;
}
