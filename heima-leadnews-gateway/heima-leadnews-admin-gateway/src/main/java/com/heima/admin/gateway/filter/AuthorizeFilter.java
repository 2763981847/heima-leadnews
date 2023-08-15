package com.heima.admin.gateway.filter;

import com.heima.admin.gateway.util.AdminJwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author Fu Qiujie
 * @since 2023/7/25
 */
@Component
public class AuthorizeFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        // 1.判断当前请求是否为登录请求，如果是则直接放行
        if (request.getURI().getPath().contains("/login")) {
            return chain.filter(exchange);
        }
        // 2.从请求头中获取token数据
        String token = request.getHeaders().getFirst("token");
        // 3.判断当前令牌是否存在，如果不存在则向客户端返回错误提示信息
        if (StringUtils.isEmpty(token)) {
            // 向客户端返回错误提示信息
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
        // 4.如果令牌存在，则校验令牌是否有效
        Claims claimsBody = AdminJwtUtil.getClaimsBody(token);
        // -1：有效，0：有效，1：过期，2：过期
        int verified = AdminJwtUtil.verifyToken(claimsBody);
        // 5.如果令牌无效，则向客户端返回错误提示信息
        if (verified == 1 || verified == 2) {
            // 向客户端返回错误提示信息
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
        //  6.如果令牌有效将用户id放入到header中
        Long userId = claimsBody.get("id", Long.class);
        request = request
                .mutate()
                .headers(httpHeaders -> httpHeaders.add("userId", userId.toString()))
                .build();
        exchange = exchange.mutate().request(request).build();
        // 放行
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
