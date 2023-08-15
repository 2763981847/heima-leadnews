package com.heima.wemedia.controller.v1;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.BooleanUtil;
import com.heima.model.common.dto.ResponseResult;
import com.heima.model.wemedia.dto.AdChannel;
import com.heima.model.wemedia.dto.ChannelDto;
import com.heima.model.wemedia.entity.WmChannel;
import com.heima.wemedia.service.WmChannelService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Fu Qiujie
 * @since 2023/7/29
 */
@RestController
@RequestMapping("/api/v1/channel")
public class WmChannelController {
    @Resource
    private WmChannelService wmChannelService;

    @GetMapping("/channels")
    public ResponseResult<List<WmChannel>> listAll() {
        return ResponseResult.okResult(wmChannelService.list());
    }

    @PostMapping("/list")
    public ResponseResult<?> listChannelPages(@RequestBody ChannelDto channelDto) {
        return wmChannelService.listChannelPages(channelDto);
    }

    @PostMapping("/save")
    public ResponseResult<?> saveChannel(@RequestBody AdChannel adChannel) {
        WmChannel wmChannel = BeanUtil.copyProperties(adChannel, WmChannel.class);
        wmChannel.setStatus(BooleanUtil.isFalse(adChannel.getStatus()) ? 0 : 1);
        wmChannel.setIsDefault(BooleanUtil.isFalse(adChannel.getIsDefault()) ? 0 : 1);
        return wmChannelService.saveChannel(wmChannel);
    }

    @PostMapping("/update")
    public ResponseResult<?> updateChannel(@RequestBody AdChannel adChannel) {
        WmChannel wmChannel = BeanUtil.copyProperties(adChannel, WmChannel.class);
        wmChannel.setStatus(BooleanUtil.isFalse(adChannel.getStatus()) ? 0 : 1);
        wmChannel.setIsDefault(BooleanUtil.isFalse(adChannel.getIsDefault()) ? 0 : 1);
        return wmChannelService.updateChannel(wmChannel);
    }

    @GetMapping("/del/{id}")
    public ResponseResult<?> removeChannel(@PathVariable("id") Integer id) {
        return wmChannelService.removeChannel(id);
    }
}
