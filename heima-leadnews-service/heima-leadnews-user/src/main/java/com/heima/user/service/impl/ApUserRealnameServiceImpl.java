package com.heima.user.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.model.common.dto.PageResponseResult;
import com.heima.model.common.dto.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.user.dto.AuthDto;
import com.heima.model.user.entity.ApUserRealname;
import com.heima.user.service.ApUserRealnameService;
import com.heima.user.mapper.ApUserRealnameMapper;
import org.springframework.stereotype.Service;

/**
 * @author Oreki
 * @description 针对表【ap_user_realname(APP实名认证信息表)】的数据库操作Service实现
 * @createDate 2023-08-16 09:42:32
 */
@Service
public class ApUserRealnameServiceImpl extends ServiceImpl<ApUserRealnameMapper, ApUserRealname>
        implements ApUserRealnameService {

    @Override
    public ResponseResult<?> listAuth(AuthDto authDto) {
        authDto.checkParam();
        Page<ApUserRealname> page = this.lambdaQuery()
                .eq(authDto.getStatus() != null, ApUserRealname::getStatus, authDto.getStatus())
                .orderByDesc(ApUserRealname::getCreatedTime)
                .page(new Page<>(authDto.getPage(), authDto.getSize()));
        return new PageResponseResult<>(authDto.getPage(), authDto.getSize(), (int) page.getTotal(), page.getRecords());
    }

    /**
     * 状态
     * 0 创建中
     * 1 待审核
     * 2 审核失败
     * 9 审核通过
     */
    @Override
    public ResponseResult<?> authFail(AuthDto authDto) {
        authDto.setStatus(2);
        updateStatus(authDto);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }


    @Override
    public ResponseResult<?> authPass(AuthDto authDto) {
        authDto.setStatus(9);
        updateStatus(authDto);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }


    private void updateStatus(AuthDto authDto) {
        String msg = authDto.getMsg();
        Integer id = authDto.getId();
        this.lambdaUpdate()
                .eq(ApUserRealname::getId, id)
                .set(ApUserRealname::getStatus, authDto.getStatus())
                .set(ApUserRealname::getReason, msg)
                .update();
    }
}




