package com.heima.admin.service;

import com.heima.model.admin.dto.AdUserDto;
import com.heima.model.admin.entity.AdUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.dto.ResponseResult;

/**
* @author Oreki
* @description 针对表【ad_user(管理员用户信息表)】的数据库操作Service
* @createDate 2023-08-15 17:09:03
*/
public interface AdUserService extends IService<AdUser> {

    ResponseResult<?> login(AdUserDto adUserDto);
}
