package com.heima.user.util;

import org.springframework.util.DigestUtils;

/**
 * @author Fu Qiujie
 * @since 2023/7/24
 */
public class UserUtils {
    public static String getEncryptPassword(String password, String salt) {
        return DigestUtils.md5DigestAsHex((password + salt).getBytes());
    }
}
