package com.heima.wemedia.controller.v1;

import com.heima.model.common.dto.PageResponseResult;
import com.heima.model.common.dto.ResponseResult;
import com.heima.model.wemedia.dto.WmMaterialDto;
import com.heima.wemedia.service.WmMaterialService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @author Fu Qiujie
 * @since 2023/7/28
 */
@RestController
@RequestMapping("/api/v1/material")
public class WmMaterialController {
    @Resource
    private WmMaterialService wmMaterialService;

    @PostMapping("/upload_picture")
    public ResponseResult<?> uploadPicture(MultipartFile multipartFile) {
        return wmMaterialService.uploadPicture(multipartFile);
    }

    @PostMapping("/list")
    public ResponseResult<?> listMaterials(@RequestBody WmMaterialDto wmMaterialDto) {
        return wmMaterialService.listMaterials(wmMaterialDto);
    }

    @GetMapping("/del_picture/{id}")
    public ResponseResult<?> delete(@PathVariable("id") Integer id) {
        return wmMaterialService.delete(id);
    }

    @GetMapping("/collect/{id}")
    public ResponseResult<?> collect(@PathVariable("id") Integer id) {
        return wmMaterialService.collect(id);
    }

    @GetMapping("/cancel_collect/{id}")
    public ResponseResult<?> cancelCollect(@PathVariable("id") Integer id) {
        return wmMaterialService.cancelCollect(id);
    }
}
