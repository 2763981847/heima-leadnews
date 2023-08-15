package com.heima.util.thread;

import com.heima.model.user.entity.ApUser;
import com.heima.model.wemedia.entity.WmUser;

/**
 * @author Fu Qiujie
 * @since 2023/7/28
 */
public class AppThreadLocalUtil {
    private static final ThreadLocal<ApUser> APP_USER_THREAD_LOCAL = new ThreadLocal<>();

    public static void setUser(ApUser apUser) {
        APP_USER_THREAD_LOCAL.set(apUser);
    }

    public static ApUser getUser() {
        return APP_USER_THREAD_LOCAL.get();
    }

    public static void clear() {
        APP_USER_THREAD_LOCAL.remove();
    }
}
