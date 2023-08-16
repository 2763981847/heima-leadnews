package com.heima.user.service;

import com.heima.model.common.dto.ResponseResult;
import com.heima.model.user.dto.AuthDto;
import com.heima.model.user.entity.ApUserRealname;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Oreki
* @description 针对表【ap_user_realname(APP实名认证信息表)】的数据库操作Service
* @createDate 2023-08-16 09:42:32
*/
public interface ApUserRealnameService extends IService<ApUserRealname> {

    ResponseResult<?> listAuth(AuthDto authDto);

    ResponseResult<?> authFail(AuthDto authDto);

    ResponseResult<?> authPass(AuthDto authDto);
}
