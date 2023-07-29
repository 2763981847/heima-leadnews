package com.heima.wemedia.controller.v1;

import com.heima.model.common.dto.ResponseResult;
import com.heima.model.wemedia.entity.WmChannel;
import com.heima.wemedia.service.WmChannelService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
