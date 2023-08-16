package com.heima.model.user.dto;

import com.heima.model.common.dto.PageRequestDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Fu Qiujie
 * @since 2023/8/16
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class AuthDto extends PageRequestDto {
    private Integer id;
    private Integer status;
    private String msg;
}
