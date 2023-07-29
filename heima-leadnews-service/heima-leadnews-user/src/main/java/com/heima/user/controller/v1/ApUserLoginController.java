package com.heima.user.controller.v1;

import com.heima.model.common.dto.ResponseResult;
import com.heima.model.user.dto.LoginDto;
import com.heima.user.service.ApUserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Fu Qiujie
 * @since 2023/7/24
 */
@RestController
@RequestMapping("/api/v1/login")
public class ApUserLoginController {
    @Resource
    private ApUserService apUserService;

    @PostMapping("/login_auth")
    public ResponseResult<?> login(@RequestBody LoginDto loginDto) {
        return apUserService.login(loginDto);
    }
}
