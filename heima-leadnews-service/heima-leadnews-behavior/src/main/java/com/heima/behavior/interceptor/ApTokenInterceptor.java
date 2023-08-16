package com.heima.behavior.interceptor;

import com.heima.model.user.entity.ApUser;
import com.heima.model.wemedia.entity.WmUser;
import com.heima.util.thread.AppThreadLocalUtil;
import com.heima.util.thread.WmThreadLocalUtil;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Fu Qiujie
 * @since 2023/7/28
 */

public class ApTokenInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userId = request.getHeader("userId");
        if (userId != null) {
            ApUser apUser = new ApUser();
            apUser.setId(Integer.valueOf(userId));
            AppThreadLocalUtil.setUser(apUser);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        AppThreadLocalUtil.clear();
    }

}