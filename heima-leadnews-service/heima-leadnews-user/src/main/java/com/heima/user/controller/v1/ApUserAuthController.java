package com.heima.user.controller.v1;

import com.heima.model.common.dto.ResponseResult;
import com.heima.model.user.dto.AuthDto;
import com.heima.user.service.ApUserRealnameService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;

/**
 * @author Fu Qiujie
 * @since 2023/8/16
 */
@RestController
@RequestMapping("/api/v1/auth")
public class ApUserAuthController {

    @Resource
    private ApUserRealnameService apUserRealnameService;

    @PostMapping("/list")
    public ResponseResult<?> listAuth(@RequestBody AuthDto authDto) {
        return apUserRealnameService.listAuth(authDto);
    }

    @PostMapping("/authFail")
    public ResponseResult<?> authFail(@RequestBody AuthDto authDto) {
        return apUserRealnameService.authFail(authDto);
    }

    @PostMapping("/authPass")
    public ResponseResult<?> authPass(@RequestBody AuthDto authDto) {
        return apUserRealnameService.authPass(authDto);
    }
}
