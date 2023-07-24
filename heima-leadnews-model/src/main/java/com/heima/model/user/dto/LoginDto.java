package com.heima.model.user.dto;

import lombok.Data;

/**
 * @author Fu Qiujie
 * @since 2023/7/24
 */
@Data
public class LoginDto {
    /**
     * 手机号
     */
    private String phone;

    /**
     * 密码
     */
    private String password;
}
