package com.heima.user.service;

import com.heima.model.common.dto.ResponseResult;
import com.heima.model.user.dto.LoginDto;
import com.heima.model.user.entity.ApUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author Oreki
 * @description 针对表【ap_user(APP用户信息表)】的数据库操作Service
 * @createDate 2023-07-24 10:27:41
 */
public interface ApUserService extends IService<ApUser> {

    ResponseResult<?> login(LoginDto loginDto);

}
