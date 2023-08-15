package com.heima.wemedia.service;

import com.heima.model.common.dto.ResponseResult;
import com.heima.model.wemedia.dto.ChannelDto;
import com.heima.model.wemedia.entity.WmChannel;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author Oreki
 * @description 针对表【wm_channel(频道信息表)】的数据库操作Service
 * @createDate 2023-07-29 12:00:08
 */
public interface WmChannelService extends IService<WmChannel> {

    ResponseResult<?> listChannelPages(ChannelDto channelDto);

    ResponseResult<?> saveChannel(WmChannel wmChannel);

    ResponseResult<?> updateChannel(WmChannel wmChannel);

    ResponseResult<?> removeChannel(Integer id);
}
