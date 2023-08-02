package com.heima.wemedia.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.file.service.FileStorageService;
import com.heima.model.common.dto.PageResponseResult;
import com.heima.model.common.dto.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.wemedia.dto.WmMaterialDto;
import com.heima.model.wemedia.entity.WmMaterial;
import com.heima.model.wemedia.entity.WmNewsMaterial;
import com.heima.model.wemedia.entity.WmUser;
import com.heima.util.thread.WmThreadLocalUtil;
import com.heima.wemedia.service.WmMaterialService;
import com.heima.wemedia.mapper.WmMaterialMapper;
import com.heima.wemedia.service.WmNewsMaterialService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
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

    @Resource
    private WmNewsMaterialService wmNewsMaterialService;

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

    @Override
    public ResponseResult<?> listMaterials(WmMaterialDto wmMaterialDto) {
        wmMaterialDto.checkParam();
        Integer size = wmMaterialDto.getSize();
        Integer page = wmMaterialDto.getPage();
        Short isCollection = wmMaterialDto.getIsCollection();
        Page<WmMaterial> materialPage = this.lambdaQuery()
                .eq(isCollection != null && isCollection == 1, WmMaterial::getIsCollection, 1)
                .eq(WmMaterial::getUserId, WmThreadLocalUtil.getUser().getId())
                .orderByDesc(WmMaterial::getCreatedTime)
                .page(new Page<>(page, size));
        return new PageResponseResult<>(
                page,
                size,
                (int) materialPage.getTotal(),
                materialPage.getRecords());
    }

    @Override
    public List<WmMaterial> listMaterialsByUrl(List<String> urlList) {
        if (CollectionUtils.isEmpty(urlList)) {
            return Collections.emptyList();
        }
        return this.lambdaQuery()
                .in(WmMaterial::getUrl, urlList)
                .list();
    }

    @Override
    @Transactional
    public ResponseResult<?> delete(Integer id) {
        // 检查参数
        if(id == null || id < 1) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        WmMaterial wmMaterial = this.getById(id);
        if (wmMaterial == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        // 判断图片是否被引用
        int count = wmNewsMaterialService.lambdaQuery()
                .eq(WmNewsMaterial::getMaterialId, id)
                .count();
        if (count > 0) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID,"图片被引用，不能删除");
        }
        // 删除图片
        boolean success = this.removeById(id);
        if (!success) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID,"删除图片失败");
        }
        // 删除minIO中的图片
        fileStorageService.delete(wmMaterial.getUrl());
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Override
    public ResponseResult<?> collect(Integer id) {
        return collectHelper(id, 1);
    }

    @Override
    public ResponseResult<?> cancelCollect(Integer id) {
        return collectHelper(id, 0);
    }

    private ResponseResult<?> collectHelper(Integer id,int isCollection){
        if (id == null || id < 1) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        boolean success = this.lambdaUpdate().eq(WmMaterial::getId, id)
                .set(WmMaterial::getIsCollection, isCollection)
                .update();
        return success ? ResponseResult.okResult(AppHttpCodeEnum.SUCCESS) : ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR);
    }
}




