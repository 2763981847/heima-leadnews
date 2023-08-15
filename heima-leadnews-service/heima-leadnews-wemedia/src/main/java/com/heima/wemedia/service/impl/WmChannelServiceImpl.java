package com.heima.wemedia.service.impl;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.model.common.dto.PageResponseResult;
import com.heima.model.common.dto.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.wemedia.dto.ChannelDto;
import com.heima.model.wemedia.entity.WmChannel;
import com.heima.model.wemedia.entity.WmNews;
import com.heima.wemedia.service.WmChannelService;
import com.heima.wemedia.mapper.WmChannelMapper;
import com.heima.wemedia.service.WmNewsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;

/**
 * @author Oreki
 * @description 针对表【wm_channel(频道信息表)】的数据库操作Service实现
 * @createDate 2023-07-29 12:00:08
 */
@Service
public class WmChannelServiceImpl extends ServiceImpl<WmChannelMapper, WmChannel>
        implements WmChannelService {

    @Override
    public ResponseResult<?> listChannelPages(ChannelDto channelDto) {
        channelDto.checkParam();
        String name = channelDto.getName();
        Page<WmChannel> page = this.lambdaQuery()
                .like(name != null, WmChannel::getName, name)
                .orderByDesc(WmChannel::getCreatedTime)
                .page(new Page<>(channelDto.getPage(), channelDto.getSize()));
        return new PageResponseResult<>(channelDto.getPage(), channelDto.getSize(), (int) page.getTotal(), page.getRecords());
    }

    @Resource
    private WmNewsService wmNewsService;

    @Override
    public ResponseResult<?> saveChannel(WmChannel wmChannel) {
        String name = wmChannel.getName();
        if (StringUtils.isBlank(name)) {
            // 频道名称不能为空
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "频道名称不能为空");
        }
        name = name.trim();
        Integer count = this.lambdaQuery()
                .eq(WmChannel::getName, name).count();
        if (count > 0) {
            // 频道名称不能重复
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_EXIST, "频道名称已存在");
        }
        // 设置默认值
        if (wmChannel.getOrd() == null) {
            wmChannel.setOrd(1);
        }
        boolean success = this.save(wmChannel);
        return success ? ResponseResult.okResult(AppHttpCodeEnum.SUCCESS)
                : ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR);
    }

    @Override
    public ResponseResult<?> updateChannel(WmChannel wmChannel) {
        String name = wmChannel.getName();
        if (StringUtils.isBlank(name)) {
            // 频道名称不能为空
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "频道名称不能为空");
        }
        name = name.trim();
        Integer count = this.lambdaQuery()
                .eq(WmChannel::getName, name)
                .ne(WmChannel::getId, wmChannel.getId())
                .count();
        if (count > 0) {
            // 频道名称不能重复
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_EXIST, "频道名称已存在");
        }
        Integer status = wmChannel.getStatus();
        if (status == 1) {
            this.updateById(wmChannel);
            return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
        }
        // 如果是要禁用频道，需要判断该频道是否被引用
        count = wmNewsService.lambdaQuery()
                .eq(WmNews::getChannelId, wmChannel.getId())
                .count();
        if (count > 0) {
            // 该频道已被引用，不能禁用
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "该频道已被引用，不能禁用");
        }
        this.updateById(wmChannel);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Override
    public ResponseResult<?> removeChannel(Integer id) {
        if (id == null || id < 0) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        // 只有禁用的频道才能删除
        WmChannel wmChannel = this.getById(id);
        if (wmChannel == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        if (wmChannel.getStatus() == 1) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "只有禁用的频道才能删除");
        }
        this.removeById(id);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
}




