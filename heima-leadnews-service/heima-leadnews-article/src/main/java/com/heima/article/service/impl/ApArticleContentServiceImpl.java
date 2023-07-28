package com.heima.article.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.model.article.entity.ApArticleContent;
import com.heima.article.service.ApArticleContentService;
import com.heima.article.mapper.ApArticleContentMapper;
import org.springframework.stereotype.Service;

/**
* @author Oreki
* @description 针对表【ap_article_content(APP已发布文章内容表)】的数据库操作Service实现
* @createDate 2023-07-26 10:26:14
*/
@Service
public class ApArticleContentServiceImpl extends ServiceImpl<ApArticleContentMapper, ApArticleContent>
    implements ApArticleContentService{

}




