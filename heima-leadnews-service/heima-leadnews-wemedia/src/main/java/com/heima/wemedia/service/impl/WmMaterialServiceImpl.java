package com.heima.wemedia.service.impl;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.file.service.FileStorageService;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.wemedia.entity.WmMaterial;
import com.heima.model.wemedia.entity.WmUser;
import com.heima.util.thread.WmThreadLocalUtil;
import com.heima.wemedia.service.WmMaterialService;
import com.heima.wemedia.mapper.WmMaterialMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * @author Oreki
 * @description 针对表【wm_material(自媒体图文素材信息表)】的数据库操作Service实现
 * @createDate 2023-07-28 21:16:59
 */
@Service
public class WmMaterialServiceImpl extends ServiceImpl<WmMaterialMapper, WmMaterial>
        implements WmMaterialService {
    @Resource
    private FileStorageService fileStorageService;

    @Override
    public ResponseResult<?> uploadPicture(MultipartFile multipartFile) {
        //1.检查参数
        if (multipartFile == null || multipartFile.isEmpty()) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //2.上传图片到minIO中
        // 生成文件名
        String filename = UUID.randomUUID().toString().replace("-", "");
        String originalFilename = multipartFile.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String url = null;
        try {
            InputStream inputStream = multipartFile.getInputStream();
            url = fileStorageService.uploadImgFile("", filename + suffix, inputStream);
        } catch (IOException e) {
            return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR);
        }
        //3.保存到数据库中
        WmUser user = WmThreadLocalUtil.getUser();
        WmMaterial wmMaterial = new WmMaterial();
        wmMaterial.setUserId(user.getId());
        wmMaterial.setUrl(url);
        wmMaterial.setType(0);
        wmMaterial.setIsCollection(0);
        this.save(wmMaterial);
        //4.返回结果
        return ResponseResult.okResult(wmMaterial);
    }
}




