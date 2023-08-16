package com.heima.wemedia.controller.v1;

import com.heima.model.common.dto.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.wemedia.dto.SensitiveDto;
import com.heima.model.wemedia.entity.WmSensitive;
import com.heima.wemedia.service.WmSensitiveService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author Fu Qiujie
 * @since 2023/8/16
 */

@RestController
@RequestMapping("/api/v1/sensitive")
public class WmSensitiveController {

    @Resource
    private WmSensitiveService wmSensitiveService;

    @DeleteMapping("/del/{id}")
    public ResponseResult<?> deleteSensitiveWord(@PathVariable Integer id) {
        return wmSensitiveService.removeById(id) ?
                ResponseResult.okResult(AppHttpCodeEnum.SUCCESS)
                : ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
    }

    @PostMapping("/list")
    public ResponseResult<?> listSensitiveWords(@RequestBody SensitiveDto sensitiveDto) {
        return wmSensitiveService.listSensitiveWords(sensitiveDto);
    }

    @PostMapping("/save")
    public ResponseResult<?> saveSensitiveWord(@RequestBody WmSensitive wmSensitive) {
        return wmSensitiveService.saveSensitiveWord(wmSensitive);
    }

    @PostMapping("/update")
    public ResponseResult<?> updateSensitiveWord(@RequestBody WmSensitive wmSensitive) {
        return wmSensitiveService.updateSensitiveWord(wmSensitive);
    }
}
