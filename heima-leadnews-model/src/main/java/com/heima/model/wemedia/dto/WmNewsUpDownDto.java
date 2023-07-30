package com.heima.model.wemedia.dto;

import lombok.Data;

/**
 * @author Fu Qiujie
 * @since 2023/7/30
 */
@Data
public class WmNewsUpDownDto {
    private Integer id;
    /**
     * 0 下架 1 上架
     */
    private Integer enable;
}
