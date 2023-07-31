package com.heima.wemedia.service.impl;

import java.time.LocalDateTime;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.heima.api.article.ArticleClient;
import com.heima.common.aliyun.GreenImageScan;
import com.heima.common.aliyun.GreenTextScan;
import com.heima.common.constants.WemediaConstants;
import com.heima.model.article.dto.ArticleDto;
import com.heima.model.common.dto.ResponseResult;
import com.heima.model.wemedia.entity.WmChannel;
import com.heima.model.wemedia.entity.WmNews;
import com.heima.model.wemedia.entity.WmUser;
import com.heima.wemedia.service.WmChannelService;
import com.heima.wemedia.service.WmNewsAutoScanService;
import com.heima.wemedia.service.WmNewsService;
import com.heima.wemedia.service.WmUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Fu Qiujie
 * @since 2023/7/31
 */
@Service
public class WmNewsAutoScanServiceImpl implements WmNewsAutoScanService {

    @Resource
    private WmNewsService wmNewsService;
    @Resource
    private GreenTextScan greenTextScan;
    @Resource
    private GreenImageScan greenImageScan;

    @Resource
    private ArticleClient articleClient;

    @Resource
    private WmUserService wmUserService;

    @Resource
    private WmChannelService wmChannelService;

    @Override
    @Transactional
    public void autoScanWmNews(Integer id) {
        // 查询自媒体文章是否存在
        WmNews wmNews = wmNewsService.getById(id);
        if (wmNews == null) {
            throw new RuntimeException("WmNewsAutoScanServiceImpl-文章不存在");
        }
        // 判断文章状态是否为待审核
        if (WmNews.Status.SUBMIT.getCode().equals(wmNews.getStatus())) {
            //1.从内容中提取纯文本内容和图片
            Map<String, Object> textAndImage = this.extractTextAndImage(wmNews);
            //2.审核文本内容  阿里云接口
            String text = (String) textAndImage.get(WemediaConstants.WM_NEWS_TYPE_TEXT);
            boolean success = this.handleTextScan(wmNews, text);
            if (!success) {
                return;
            }
            //3.审核图片  阿里云接口
            List<String> images = (List<String>) textAndImage.get(WemediaConstants.WM_NEWS_TYPE_IMAGE);
            success = this.handleImageScan(wmNews, images);
            if (!success) {
                return;
            }
            //4.审核成功，保存app端的相关的文章数据
            this.saveApArticle(wmNews);
            this.updateScanInfo(wmNews.getId(), WmNews.Status.PUBLISHED, "审核通过");
        }
    }

    private void saveApArticle(WmNews wmNews) {
        ArticleDto articleDto = BeanUtil.copyProperties(wmNews, ArticleDto.class);
        articleDto.setId(wmNews.getArticleId());
        articleDto.setAuthorId(wmNews.getUserId());
        String authorName = wmUserService.lambdaQuery()
                .select(WmUser::getName)
                .eq(WmUser::getId, wmNews.getUserId())
                .one()
                .getName();
        articleDto.setAuthorName(authorName);
        String channelName = wmChannelService.lambdaQuery()
                .select(WmChannel::getName)
                .eq(WmChannel::getId, wmNews.getChannelId())
                .one()
                .getName();
        articleDto.setChannelName(channelName);
        articleDto.setLayout(Integer.valueOf(wmNews.getType()));
        articleDto.setPublishTime(LocalDateTime.now());
        ResponseResult<?> responseResult = articleClient.saveArticle(articleDto);
        if (responseResult.getCode().equals(200)) {
            // 保存成功,更新自媒体文章的文章id
            wmNewsService.lambdaUpdate().eq(WmNews::getId, wmNews.getId())
                    .set(WmNews::getArticleId, responseResult.getData())
                    .update();
        } else {
            // 保存失败
            throw new RuntimeException("WmNewsAutoScanServiceImpl-保存app端文章失败");
        }
    }

    private boolean handleImageScan(WmNews wmNews, List<String> images) {
        if (CollectionUtil.isEmpty(images)) {
            return true;
        }
        // 图片去重
        images = CollectionUtil.distinct(images);
        try {
            Map<String, String> resultMap = greenImageScan.imageScanByUrls(images);
            // 由于开通不了阿里云的审核服务，所以这里直接返回true
            if (resultMap == null) return true;
            String suggestion = resultMap.get("suggestion");
            if ("block".equals(suggestion)) {
                // 审核不通过
                updateScanInfo(wmNews.getId(), WmNews.Status.FAIL, "文章存在违规图片");
                return false;
            } else if ("review".equals(suggestion)) {
                // 不确定信息  需要人工审核
                updateScanInfo(wmNews.getId(), WmNews.Status.ADMIN_AUTH, "文章存在不确定图片，需要人工审核");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean handleTextScan(WmNews wmNews, String text) {
        if (StrUtil.isBlank(text)) {
            return true;
        }
        try {
            Map<String, String> resultMap = greenTextScan.greeTextScan(text);
            // 由于开通不了阿里云的审核服务，所以这里直接返回true
            if (resultMap == null) return true;
            String suggestion = resultMap.get("suggestion");
            if ("block".equals(suggestion)) {
                // 审核不通过
                updateScanInfo(wmNews.getId(), WmNews.Status.FAIL, "文章存在违规内容");
                return false;
            } else if ("review".equals(suggestion)) {
                // 不确定信息  需要人工审核
                updateScanInfo(wmNews.getId(), WmNews.Status.ADMIN_AUTH, "文章存在不确定内容，需要人工审核");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void updateScanInfo(Integer newsId, WmNews.Status status, String reason) {
        wmNewsService.lambdaUpdate()
                .eq(WmNews::getId, newsId)
                .set(WmNews::getStatus, status.getCode())
                .set(WmNews::getReason, reason)
                .update();
    }

    private Map<String, Object> extractTextAndImage(WmNews wmNews) {
        String content = wmNews.getContent();
        String images = wmNews.getImages();
        List<Map> parsedContent = JSON.parseArray(content, Map.class);
        List<String> imageList = Arrays.stream(images.split(",")).collect(Collectors.toList());
        HashMap<String, Object> resultMap = new HashMap<>();
        StringBuilder text = new StringBuilder(wmNews.getTitle());
        parsedContent.forEach(map -> {
            if (WemediaConstants.WM_NEWS_TYPE_TEXT.equals(map.get("type"))) {
                text.append(map.get("value"));
            } else {
                imageList.add((String) map.get("value"));
            }
        });
        resultMap.put(WemediaConstants.WM_NEWS_TYPE_TEXT, text.toString());
        resultMap.put(WemediaConstants.WM_NEWS_TYPE_IMAGE, imageList);
        return resultMap;
    }
}
