package com.heima.wemedia.controller.v1;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.wemedia.service.WmMaterialService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.xml.ws.Response;

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
}
