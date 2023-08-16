package com.heima.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.common.constants.BehaviorConstants;
import com.heima.common.redis.CacheService;
import com.heima.model.common.dto.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.user.UserRelationDto;
import com.heima.model.user.dto.LoginDto;
import com.heima.model.user.entity.ApUser;
import com.heima.model.user.vo.LoginUserVo;
import com.heima.user.mapper.ApUserMapper;
import com.heima.user.service.ApUserService;
import com.heima.util.common.AppJwtUtil;
import com.heima.util.common.LoginUtils;
import com.heima.util.thread.AppThreadLocalUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Oreki
 * @description 针对表【ap_user(APP用户信息表)】的数据库操作Service实现
 * @createDate 2023-07-24 10:27:41
 */
@Service
public class ApUserServiceImpl extends ServiceImpl<ApUserMapper, ApUser>
        implements ApUserService {

    @Override
    public ResponseResult<?> login(LoginDto loginDto) {
        String phone = loginDto.getPhone();
        if (StrUtil.isBlank(phone)) {
            // 手机号为空，游客登录
            return ResponseResult.okResult(MapUtil.of("token", AppJwtUtil.getToken(0L)));
        }
        String password = loginDto.getPassword();
        if (StrUtil.isBlank(password)) {
            // 密码为空
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE, "密码不能为空");
        }
        // 根据手机号查询用户
        ApUser apUser = this.lambdaQuery().eq(ApUser::getPhone, phone).one();
        if (apUser == null) {
            // 用户不存在
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "用户不存在");
        }
        // 校验密码
        String salt = apUser.getSalt();
        String encryptedPassword = apUser.getPassword();
        if (!LoginUtils.getEncryptPassword(password, salt).equals(encryptedPassword)) {
            // 密码错误
            return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR);
        }
        // 密码正确，登录成功
        LoginUserVo loginUserVo = new LoginUserVo();
        BeanUtil.copyProperties(apUser, loginUserVo);
        Map<String, Object> map = new HashMap<>();
        map.put("user", loginUserVo);
        map.put("token", AppJwtUtil.getToken(apUser.getId().longValue()));
        return ResponseResult.okResult(map);
    }

    @Resource
    private CacheService cacheService;

    @Override
    public ResponseResult<?> follow(UserRelationDto userRelationDto) {
        ApUser user = AppThreadLocalUtil.getUser();
        if (user == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }
        String key = BehaviorConstants.FOLLOW + user.getId();
        Short operation = userRelationDto.getOperation();
        Integer authorId = userRelationDto.getAuthorId();
        if (operation == 0) {
            // 关注
            cacheService.sAdd(key, authorId.toString());
        } else {
            // 取消关注
            cacheService.sRemove(key, authorId.toString());
        }
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
}




