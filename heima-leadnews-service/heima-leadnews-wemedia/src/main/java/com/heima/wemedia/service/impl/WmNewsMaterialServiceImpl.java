package com.heima.wemedia.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.model.wemedia.entity.WmNewsMaterial;
import com.heima.wemedia.service.WmNewsMaterialService;
import com.heima.wemedia.mapper.WmNewsMaterialMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Oreki
 * @description 针对表【wm_news_material(自媒体图文引用素材信息表)】的数据库操作Service实现
 * @createDate 2023-07-29 17:12:43
 */
@Service
public class WmNewsMaterialServiceImpl extends ServiceImpl<WmNewsMaterialMapper, WmNewsMaterial>
        implements WmNewsMaterialService {

    @Override
    @Transactional
    public boolean saveRelations(List<Integer> materialIds, Integer newsId, Short type) {
        List<WmNewsMaterial> wmNewsMaterials = IntStream.range(0, materialIds.size()).mapToObj(
                i -> {
                    WmNewsMaterial wmNewsMaterial = new WmNewsMaterial();
                    wmNewsMaterial.setMaterialId(materialIds.get(i));
                    wmNewsMaterial.setNewsId(newsId);
                    wmNewsMaterial.setType(Integer.valueOf(type));
                    wmNewsMaterial.setOrd(i);
                    return wmNewsMaterial;
                }
        ).collect(Collectors.toList());
        return this.saveBatch(wmNewsMaterials);
    }
}




