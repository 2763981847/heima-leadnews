package com.heima.behavior.service;

import com.heima.model.behavior.dto.LikesBehaviorDto;
import com.heima.model.behavior.dto.ReadBehaviorDto;
import com.heima.model.behavior.dto.UnlikesBehaviorDto;
import com.heima.model.common.dto.ResponseResult;

/**
 * @author Fu Qiujie
 * @since 2023/8/16
 */
public interface BehaviorService {
    ResponseResult<?> likesBehavior(LikesBehaviorDto likesBehaviorDto);

    ResponseResult<?> readBehavior(ReadBehaviorDto readBehaviorDto);

    ResponseResult<?> unLikesBehavior(UnlikesBehaviorDto unlikesBehaviorDto);
}
