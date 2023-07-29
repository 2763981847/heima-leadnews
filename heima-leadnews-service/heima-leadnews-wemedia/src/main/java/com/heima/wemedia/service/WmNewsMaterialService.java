package com.heima.wemedia.service;

import com.heima.model.wemedia.entity.WmNewsMaterial;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author Oreki
 * @description 针对表【wm_news_material(自媒体图文引用素材信息表)】的数据库操作Service
 * @createDate 2023-07-29 17:12:43
 */
public interface WmNewsMaterialService extends IService<WmNewsMaterial> {
    boolean saveRelations(List<Integer> materialIds, Integer newsId, Short type);

}
