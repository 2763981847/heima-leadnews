package com.heima.behavior.service.impl;

import com.heima.behavior.service.BehaviorService;
import com.heima.common.constants.BehaviorConstants;
import com.heima.common.redis.CacheService;
import com.heima.model.behavior.dto.LikesBehaviorDto;
import com.heima.model.behavior.dto.ReadBehaviorDto;
import com.heima.model.behavior.dto.UnlikesBehaviorDto;
import com.heima.model.common.dto.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.user.entity.ApUser;
import com.heima.util.thread.AppThreadLocalUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Fu Qiujie
 * @since 2023/8/16
 */
@Service
public class BehaviorServiceImpl implements BehaviorService {

    @Resource
    private CacheService cacheService;

    @Override
    public ResponseResult<?> likesBehavior(LikesBehaviorDto likesBehaviorDto) {
        ApUser user = AppThreadLocalUtil.getUser();
        if (user == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }
        String key = BehaviorConstants.LIKE + user.getId();
        Short operation = likesBehaviorDto.getOperation();
        Long articleId = likesBehaviorDto.getArticleId();
        if (operation == 0) {
            // 点赞
            cacheService.sAdd(key, articleId.toString());
        } else {
            // 取消点赞
            cacheService.sRemove(key, articleId.toString());
        }
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Override
    public ResponseResult<?> readBehavior(ReadBehaviorDto readBehaviorDto) {
        ApUser user = AppThreadLocalUtil.getUser();
        if (user == null) {
            return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
        }
        String key = BehaviorConstants.READ + user.getId();
        cacheService.zIncrementScore(key, readBehaviorDto.getArticleId(), readBehaviorDto.getCount());
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Override
    public ResponseResult<?> unLikesBehavior(UnlikesBehaviorDto unlikesBehaviorDto) {
        ApUser user = AppThreadLocalUtil.getUser();
        if (user == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }
        String key = BehaviorConstants.UNLIKE + user.getId();
        Short type = unlikesBehaviorDto.getType();
        Long articleId = unlikesBehaviorDto.getArticleId();
        if (type == 0) {
            // 不喜欢
            cacheService.sAdd(key, articleId.toString());
        } else {
            // 取消不喜欢
            cacheService.sRemove(key, articleId.toString());
        }
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
}
