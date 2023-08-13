package com.heima.article.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.article.service.ApArticleConfigService;
import com.heima.article.service.ApArticleContentService;
import com.heima.article.service.ApArticleFreemarkerService;
import com.heima.common.constants.ArticleConstants;
import com.heima.model.article.dto.ArticleDto;
import com.heima.model.article.dto.ArticleHomeDto;
import com.heima.model.article.entity.ApArticle;
import com.heima.article.service.ApArticleService;
import com.heima.article.mapper.ApArticleMapper;
import com.heima.model.article.entity.ApArticleConfig;
import com.heima.model.article.entity.ApArticleContent;
import com.heima.model.common.dto.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Oreki
 * @description 针对表【ap_article(文章信息表，存储已发布的文章)】的数据库操作Service实现
 * @createDate 2023-07-26 10:26:14
 */
@Service
public class ApArticleServiceImpl extends ServiceImpl<ApArticleMapper, ApArticle>
        implements ApArticleService {

    @Resource
    private ApArticleContentService apArticleContentService;

    @Resource
    private ApArticleConfigService apArticleConfigService;

    @Resource
    private ApArticleFreemarkerService apArticleFreemarkerService;

    @Override
    public List<ApArticle> load(Short loadType, ArticleHomeDto dto) {
        // 1.参数校验并设置默认值
        if (dto == null) {
            dto = new ArticleHomeDto();
        }
        // 分页大小校验
        Integer size = dto.getSize();
        if (size == null || size < 0 || size > ArticleConstants.MAX_PAGE_SIZE) {
            dto.setSize(ArticleConstants.DEFAULT_PAGE_SIZE);
        }
        // 最大时间校验
        if (dto.getMaxBehotTime() == null) {
            dto.setMaxBehotTime(LocalDateTime.now());
        }
        // 最小时间校验
        if (dto.getMinBehotTime() == null) {
            dto.setMinBehotTime(LocalDateTime.now());
        }
        // tag校验
        String tag = dto.getTag();
        if (tag == null) {
            dto.setTag(ArticleConstants.DEFAULT_TAG);
        }
        // loadType校验
        if (!ArticleConstants.LOAD_MORE.equals(loadType) &&
                !ArticleConstants.LOAD_NEW.equals(loadType)) {
            loadType = ArticleConstants.LOAD_MORE;
        }
        // 2.根据参数查询文章列表
        return baseMapper.loadArticleList(dto, loadType);
    }

    @Override
    @Transactional
    public ResponseResult<?> saveArticle(ArticleDto dto) {
        // 1.参数校验
        if (dto == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        // 2.拷贝属性到ApArticle对象
        ApArticle apArticle = BeanUtil.copyProperties(dto, ApArticle.class);
        // 3.判断是否存在id，如果存在则更新，否则新增
        if (apArticle.getId() != null) {
            // 更新
            // 更新文章表
            boolean success = this.updateById(apArticle);
            if (!success) {
                return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "文章不存在");
            }
            // 更新文章内容表
            apArticleContentService.lambdaUpdate()
                    .eq(ApArticleContent::getArticleId, apArticle.getId())
                    .set(ApArticleContent::getContent, dto.getContent())
                    .update();
        } else {
            // 新增
            // 1.保存文章表
            this.save(apArticle);
            // 2.保存文章配置表
            ApArticleConfig apArticleConfig = new ApArticleConfig(apArticle.getId());
            apArticleConfigService.save(apArticleConfig);
            // 3.保存文章内容表
            ApArticleContent apArticleContent = new ApArticleContent(apArticle.getId(), dto.getContent());
            apArticleContentService.save(apArticleContent);
        }
        apArticleFreemarkerService.buildArticleToMinIO(apArticle, dto.getContent());
        // 4.返回文章id
        return ResponseResult.okResult(apArticle.getId());
    }
}




