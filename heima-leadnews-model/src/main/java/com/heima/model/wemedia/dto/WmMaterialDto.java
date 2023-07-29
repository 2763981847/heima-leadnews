package com.heima.model.wemedia.dto;

import com.heima.model.common.dto.PageRequestDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Fu Qiujie
 * @since 2023/7/29
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WmMaterialDto extends PageRequestDto {

    /**
     * 1 收藏
     * 0 未收藏
     */
    private Short isCollection;
}
