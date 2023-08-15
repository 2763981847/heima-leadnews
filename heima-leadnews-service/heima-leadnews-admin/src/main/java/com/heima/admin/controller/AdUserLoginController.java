package com.heima.admin.controller;

import com.heima.admin.service.AdUserService;
import com.heima.model.admin.dto.AdUserDto;
import com.heima.model.common.dto.ResponseResult;
import com.heima.model.user.dto.LoginDto;
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
@RequestMapping("/login")
public class AdUserLoginController {
    @Resource
    private AdUserService adUserService;

    @PostMapping("/in")
    public ResponseResult<?> login(@RequestBody AdUserDto adUserDto) {
        return adUserService.login(adUserDto);
    }
}
