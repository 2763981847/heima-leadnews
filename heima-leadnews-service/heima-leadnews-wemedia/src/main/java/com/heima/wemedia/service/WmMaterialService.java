package com.heima.wemedia.service;

import com.heima.model.common.dto.PageResponseResult;
import com.heima.model.common.dto.ResponseResult;
import com.heima.model.wemedia.dto.WmMaterialDto;
import com.heima.model.wemedia.entity.WmMaterial;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author Oreki
 * @description 针对表【wm_material(自媒体图文素材信息表)】的数据库操作Service
 * @createDate 2023-07-28 21:16:59
 */
public interface WmMaterialService extends IService<WmMaterial> {
    ResponseResult<?> uploadPicture(MultipartFile multipartFile);

    ResponseResult<?> listMaterials(WmMaterialDto wmMaterialDto);

    List<WmMaterial> listMaterialsByUrl(List<String> urlList);

    ResponseResult<?> delete(Integer id);

    ResponseResult<?> collect(Integer id);

    ResponseResult<?> cancelCollect(Integer id);
}
