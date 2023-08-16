package com.heima.behavior.controller.v1;

import com.heima.behavior.service.BehaviorService;
import com.heima.model.behavior.dto.LikesBehaviorDto;
import com.heima.model.behavior.dto.ReadBehaviorDto;
import com.heima.model.behavior.dto.UnlikesBehaviorDto;
import com.heima.model.common.dto.ResponseResult;
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
@RequestMapping("/api/v1")
public class BehaviorController {
    @Resource
    private BehaviorService behaviorService;

    @PostMapping("/likes_behavior")
    public ResponseResult<?> likesBehavior(@RequestBody LikesBehaviorDto likesBehaviorDto) {
        return behaviorService.likesBehavior(likesBehaviorDto);
    }

    @PostMapping("/read_behavior")
    public ResponseResult<?> readBehavior(@RequestBody ReadBehaviorDto readBehaviorDto) {
        return behaviorService.readBehavior(readBehaviorDto);
    }

    @PostMapping("/un_likes_behavior")
    public ResponseResult<?> unLikesBehavior(@RequestBody UnlikesBehaviorDto unlikesBehaviorDto) {
        return behaviorService.unLikesBehavior(unlikesBehaviorDto);
    }
}
