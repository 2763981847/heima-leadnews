package com.heima.util.common;

import org.springframework.util.DigestUtils;

/**
 * @author Fu Qiujie
 * @since 2023/8/15
 */
public class LoginUtils {
    public static String getEncryptPassword(String password, String salt) {
        return DigestUtils.md5DigestAsHex((password + salt).getBytes());
    }
}
