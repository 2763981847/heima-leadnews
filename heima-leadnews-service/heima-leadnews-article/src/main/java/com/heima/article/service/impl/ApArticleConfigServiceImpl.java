package com.heima.article.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.model.article.entity.ApArticleConfig;
import com.heima.article.service.ApArticleConfigService;
import com.heima.article.mapper.ApArticleConfigMapper;
import org.springframework.stereotype.Service;

/**
* @author Oreki
* @description 针对表【ap_article_config(APP已发布文章配置表)】的数据库操作Service实现
* @createDate 2023-07-26 10:26:14
*/
@Service
public class ApArticleConfigServiceImpl extends ServiceImpl<ApArticleConfigMapper, ApArticleConfig>
    implements ApArticleConfigService{

}




