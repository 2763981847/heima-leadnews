package com.heima.wemedia.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.model.common.dto.PageResponseResult;
import com.heima.model.common.dto.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.wemedia.dto.SensitiveDto;
import com.heima.model.wemedia.entity.WmSensitive;
import com.heima.model.wemedia.entity.WmSensitive;
import com.heima.wemedia.service.WmSensitiveService;
import com.heima.wemedia.mapper.WmSensitiveMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author Oreki
 * @description 针对表【wm_sensitive(敏感词信息表)】的数据库操作Service实现
 * @createDate 2023-07-31 20:48:55
 */
@Service
public class WmSensitiveServiceImpl extends ServiceImpl<WmSensitiveMapper, WmSensitive>
        implements WmSensitiveService {

    @Override
    public ResponseResult<?> listSensitiveWords(SensitiveDto sensitiveDto) {
        sensitiveDto.checkParam();
        String name = sensitiveDto.getName();
        Page<WmSensitive> page = this.lambdaQuery()
                .like(StringUtils.isNoneBlank(name), WmSensitive::getSensitives, name)
                .orderByDesc(WmSensitive::getCreatedTime)
                .page(new Page<>(sensitiveDto.getPage(), sensitiveDto.getSize()));
        return new PageResponseResult<>(sensitiveDto.getPage(), sensitiveDto.getSize(), (int) page.getTotal(), page.getRecords());
    }

    @Override
    public ResponseResult<?> saveSensitiveWord(WmSensitive wmSensitive) {
        String sensitives = wmSensitive.getSensitives();
        sensitives = sensitives.trim();
        if (StringUtils.isBlank(sensitives)) {
            // 敏感词不能为空
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "敏感词不能为空");
        }
        Integer count = this.lambdaQuery()
                .eq(WmSensitive::getSensitives, sensitives)
                .count();
        if (count > 0) {
            // 敏感词不能重复
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_EXIST, "敏感词已存在");
        }
        this.save(wmSensitive);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Override
    public ResponseResult<?> updateSensitiveWord(WmSensitive wmSensitive) {
        String sensitives = wmSensitive.getSensitives();
        sensitives = sensitives.trim();
        if (StringUtils.isBlank(sensitives)) {
            // 敏感词不能为空
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "敏感词不能为空");
        }
        Integer count = this.lambdaQuery()
                .eq(WmSensitive::getSensitives, sensitives)
                .ne(WmSensitive::getId, wmSensitive.getId())
                .count();
        if (count > 0) {
            // 敏感词不能重复
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_EXIST, "敏感词已存在");
        }
        return this.updateById(wmSensitive) ?
                ResponseResult.okResult(AppHttpCodeEnum.SUCCESS)
                : ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
    }
}




