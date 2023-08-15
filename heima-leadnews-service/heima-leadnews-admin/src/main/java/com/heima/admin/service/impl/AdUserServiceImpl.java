package com.heima.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.model.admin.dto.AdUserDto;
import com.heima.model.admin.entity.AdUser;
import com.heima.admin.service.AdUserService;
import com.heima.admin.mapper.AdUserMapper;
import com.heima.model.common.dto.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.user.entity.ApUser;
import com.heima.model.user.vo.LoginUserVo;
import com.heima.util.common.AppJwtUtil;
import com.heima.util.common.LoginUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Oreki
 * @description 针对表【ad_user(管理员用户信息表)】的数据库操作Service实现
 * @createDate 2023-08-15 17:09:03
 */
@Service
public class AdUserServiceImpl extends ServiceImpl<AdUserMapper, AdUser>
        implements AdUserService {

    @Override
    public ResponseResult<?> login(AdUserDto adUserDto) {
        String name = adUserDto.getName();
        if (StrUtil.isBlank(name)) {
            // 用户名为空
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE, "用户名不能为空");
        }
        String password = adUserDto.getPassword();
        if (StrUtil.isBlank(password)) {
            // 密码为空
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE, "密码不能为空");
        }
        // 根据用户名查询管理员
        AdUser adUser = this.lambdaQuery().eq(AdUser::getName, name).one();
        if (adUser == null) {
            // 用户不存在
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "用户不存在");
        }
        // 校验密码
        String salt = adUser.getSalt();
        String encryptedPassword = adUser.getPassword();
        if (!LoginUtils.getEncryptPassword(password, salt).equals(encryptedPassword)) {
            // 密码错误
            return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR);
        }
        // 密码正确，登录成功
        adUser.setSalt(null);
        adUser.setPassword(null);
        Map<String, Object> map = new HashMap<>();
        map.put("user", adUser);
        map.put("token", AppJwtUtil.getToken(adUser.getId().longValue()));
        return ResponseResult.okResult(map);
    }
}




