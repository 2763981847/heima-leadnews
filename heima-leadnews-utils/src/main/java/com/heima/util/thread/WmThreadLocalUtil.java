package com.heima.util.thread;

import com.heima.model.wemedia.entity.WmUser;

/**
 * @author Fu Qiujie
 * @since 2023/7/28
 */
public class WmThreadLocalUtil {
    private static final ThreadLocal<WmUser> WM_USER_THREAD_LOCAL = new ThreadLocal<>();

    public static void setUser(WmUser wmUser) {
        WM_USER_THREAD_LOCAL.set(wmUser);
    }

    public static WmUser getUser() {
        return WM_USER_THREAD_LOCAL.get();
    }

    public static void clear() {
        WM_USER_THREAD_LOCAL.remove();
    }
}
