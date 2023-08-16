package com.heima.model.wemedia.dto;

import com.heima.model.common.dto.PageRequestDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Fu Qiujie
 * @since 2023/8/16
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SensitiveDto extends PageRequestDto {
    private String name;
}
