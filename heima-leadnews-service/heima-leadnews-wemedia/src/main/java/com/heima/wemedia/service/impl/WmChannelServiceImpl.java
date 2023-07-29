package com.heima.wemedia.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.model.wemedia.entity.WmChannel;
import com.heima.wemedia.service.WmChannelService;
import com.heima.wemedia.mapper.WmChannelMapper;
import org.springframework.stereotype.Service;

/**
* @author Oreki
* @description 针对表【wm_channel(频道信息表)】的数据库操作Service实现
* @createDate 2023-07-29 12:00:08
*/
@Service
public class WmChannelServiceImpl extends ServiceImpl<WmChannelMapper, WmChannel>
    implements WmChannelService{

}




