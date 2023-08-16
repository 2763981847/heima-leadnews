package com.heima.user.controller.v1;

import com.heima.model.common.dto.ResponseResult;
import com.heima.model.user.UserRelationDto;
import com.heima.user.service.ApUserRealnameService;
import com.heima.user.service.ApUserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Fu Qiujie
 * @since 2023/8/16
 */
@RestController
@RequestMapping("/api/v1/user")
public class ApUserController {

    @Resource
    private ApUserService apUserService;

    @PostMapping("/user_follow")
    public ResponseResult<?> follow(@RequestBody UserRelationDto userRelationDto) {
        return apUserService.follow(userRelationDto);
    }
}
