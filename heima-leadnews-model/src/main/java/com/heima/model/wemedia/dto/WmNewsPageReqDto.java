package com.heima.model.wemedia.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.heima.model.common.dto.PageRequestDto;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author Fu Qiujie
 * @since 2023/7/29
 */

@Data
public class WmNewsPageReqDto extends PageRequestDto {

    /**
     * 状态
     */
    private Short status;
    /**
     * 开始时间
     */
    private LocalDate beginPubDate;
    /**
     * 结束时间
     */

    private LocalDate endPubDate;
    /**
     * 所属频道ID
     */
    private Integer channelId;
    /**
     * 关键字
     */
    private String keyword;
}