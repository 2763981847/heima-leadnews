package com.heima.wemedia.service.impl;

import java.time.LocalDate;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.common.constants.WemediaConstants;
import com.heima.common.exception.CustomException;
import com.heima.model.common.dto.PageResponseResult;
import com.heima.model.common.dto.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.wemedia.dto.WmNewsDto;
import com.heima.model.wemedia.dto.WmNewsPageReqDto;
import com.heima.model.wemedia.dto.WmNewsUpDownDto;
import com.heima.model.wemedia.entity.WmMaterial;
import com.heima.model.wemedia.entity.WmNews;
import com.heima.model.wemedia.entity.WmNewsMaterial;
import com.heima.util.thread.WmThreadLocalUtil;
import com.heima.wemedia.service.WmMaterialService;
import com.heima.wemedia.service.WmNewsMaterialService;
import com.heima.wemedia.service.WmNewsService;
import com.heima.wemedia.mapper.WmNewsMapper;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Oreki
 * @description 针对表【wm_news(自媒体图文内容信息表)】的数据库操作Service实现
 * @createDate 2023-07-29 12:10:53
 */
@Service
public class WmNewsServiceImpl extends ServiceImpl<WmNewsMapper, WmNews>
        implements WmNewsService {

    @Resource
    private WmNewsMaterialService wmNewsMaterialService;

    @Resource
    private WmMaterialService wmMaterialService;

    @Override
    public ResponseResult<List<WmNews>> listNews(WmNewsPageReqDto wmNewsPageReqDto) {
        wmNewsPageReqDto.checkParam();
        Short status = wmNewsPageReqDto.getStatus();
        LocalDate beginPubDate = wmNewsPageReqDto.getBeginPubDate();
        LocalDate endPubDate = wmNewsPageReqDto.getEndPubDate();
        Integer channelId = wmNewsPageReqDto.getChannelId();
        String keyword = wmNewsPageReqDto.getKeyword();
        Integer size = wmNewsPageReqDto.getSize();
        Integer page = wmNewsPageReqDto.getPage();
        Page<WmNews> wmNewsPage = this.lambdaQuery()
                .eq(WmNews::getUserId, WmThreadLocalUtil.getUser().getId())
                .eq(status != null, WmNews::getStatus, status)
                .gt(beginPubDate != null, WmNews::getPublishTime, beginPubDate)
                .lt(endPubDate != null, WmNews::getPublishTime, endPubDate)
                .eq(channelId != null, WmNews::getChannelId, channelId)
                .like(keyword != null, WmNews::getTitle, keyword)
                .orderByDesc(WmNews::getPublishTime)
                .page(new Page<>(page, size));
        return new PageResponseResult<>(page, size, (int) wmNewsPage.getTotal(), wmNewsPage.getRecords());
    }

    @Override
    @Transactional
    public ResponseResult<?> submitNews(WmNewsDto dto) {
        // 参数检验
        if(dto == null || StrUtil.isBlank(dto.getContent())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        // 设置文章属性
        List<String> images = dto.getImages();
        WmNews wmNews = BeanUtil.copyProperties(dto, WmNews.class);
        wmNews.setUserId(WmThreadLocalUtil.getUser().getId());
        wmNews.setImages(images == null?null : String.join(",", images));
        // 封面类型为自动时， type = -1
        if(WemediaConstants.WM_NEWS_TYPE_AUTO.equals(wmNews.getType())){
            wmNews.setType(null);
        }
        if(dto.getId() == null){
            // 新增文章
            saveNews(wmNews);
        }else {
            // 修改文章
            updateNews(wmNews);
        }
        // 如果是草稿直接返回
        if(WmNews.Status.NORMAL.getCode().equals(wmNews.getStatus())){
            return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
        }
        // 不是草稿，保存文章图片与素材的关系后返回
        return saveRelations(dto, wmNews.getId());
    }

    @Override
    @Transactional
    public ResponseResult<?> deleteNews(Integer id) {
        // 参数检验
        if (id == null || id < 1) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        // 查询文章
        WmNews wmNews = getById(id);
        if (wmNews == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST,"文章不存在");
        }
        // 判断文章状态
        if (WmNews.Status.PUBLISHED.getCode().equals(wmNews.getStatus())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID,"已发布的文章不能删除");
        }
        // 删除文章
        removeById(id);
        // 删除文章与素材的关系
        wmNewsMaterialService.lambdaUpdate()
                .eq(WmNewsMaterial::getNewsId, id)
                .remove();
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Override
    public ResponseResult<?> downOrUp(WmNewsUpDownDto wmNewsUpDownDto) {
        Integer id = wmNewsUpDownDto.getId();
        if (id == null || id < 1) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        WmNews wmNews = getById(id);
        if (wmNews == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST,"文章不存在");
        }
        // 判断文章状态，只有已发布的文章才能上下架
        if (!WmNews.Status.PUBLISHED.getCode().equals(wmNews.getStatus())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID,"当前文章不是已发布状态，不能上下架");
        }
        // 上下架文章
        boolean success = this.lambdaUpdate()
                .eq(WmNews::getId, id)
                .set(WmNews::getStatus, wmNewsUpDownDto.getEnable())
                .update();
        return success ? ResponseResult.okResult(AppHttpCodeEnum.SUCCESS) :
                ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR);
    }

    private ResponseResult<?> saveRelations(WmNewsDto dto, Integer newsId) {
        // 提取文章内容中的图片
        List<String> contentImages = extractImagesFromContent(dto.getContent());
        // 保存内容图片与素材的关系
        saveRelationsHelper(contentImages,newsId,WemediaConstants.WM_CONTENT_REFERENCE);
        // 处理封面图片
        coverHandler(dto,contentImages,newsId);
        // 保存封面图片与素材的关系
        saveRelationsHelper(dto.getImages(),newsId,WemediaConstants.WM_COVER_REFERENCE);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    private void coverHandler(WmNewsDto dto, List<String> contentImages, Integer newsId) {
        List<String> images =dto.getImages();
        //如果当前封面类型为自动，则设置封面类型的数据
        if(WemediaConstants.WM_NEWS_TYPE_AUTO.equals(dto.getType())){
            //多图
            images = contentImages.stream().limit(3).collect(Collectors.toList());
            if(contentImages.size() >= 3){
                this.lambdaUpdate().eq(WmNews::getId,newsId)
                        .set(WmNews::getType,WemediaConstants.WM_NEWS_MANY_IMAGE)
                        .set(WmNews::getImages,String.join(",",images))
                        .update();
            }else if(contentImages.size() >= 1){
                //单图
                images = contentImages.stream().limit(1).collect(Collectors.toList());
                this.lambdaUpdate().eq(WmNews::getId,newsId)
                        .set(WmNews::getType,WemediaConstants.WM_NEWS_SINGLE_IMAGE)
                        .set(WmNews::getImages,String.join(",",images))
                        .update();
            }else {
                //无图
                this.lambdaUpdate().eq(WmNews::getId,newsId)
                        .set(WmNews::getType,WemediaConstants.WM_NEWS_NONE_IMAGE)
                        .update();
            }
        }
        dto.setImages(images);
    }

    private void saveRelationsHelper(List<String> images, Integer newsId, Short Type) {
        if (CollectionUtils.isEmpty(images)) {
            return;
        }
        // 根据图片url查询素材表
        List<WmMaterial> wmMaterials = wmMaterialService.listMaterialsByUrl(images);
        if (wmMaterials.size() != images.size()){
            // 有图片不存在,抛出异常进行数据回滚
            throw new CustomException(AppHttpCodeEnum.MATERIAL_REFERENCE_FAIL);
        }
        wmNewsMaterialService.saveRelations(
                wmMaterials.stream().map(WmMaterial::getId).collect(Collectors.toList()),
                newsId,
                Type);
    }

    private List<String> extractImagesFromContent(String content) {
        List<Map> maps = JSON.parseArray(content, Map.class);
        return maps.stream()
                .filter(map -> WemediaConstants.WM_NEWS_TYPE_IMAGE.equals(map.get("type")))
                .map(map -> (String) map.get("value")).collect(Collectors.toList());
    }

    private boolean updateNews(WmNews wmNews) {
        if (WmNews.Status.SUBMIT.getCode().equals(wmNews.getStatus())){
            wmNews.setSubmitedTime(LocalDateTime.now());
        }
        // 保存并删除文章图片与素材的关系
        return this.updateById(wmNews) && wmNewsMaterialService.lambdaUpdate()
                .eq(WmNewsMaterial::getNewsId, wmNews.getId())
                .remove();
    }

    private boolean saveNews(WmNews wmNews) {
        // 设置默认值
        wmNews.setEnable(1);
        wmNews.setUserId(WmThreadLocalUtil.getUser().getId());
        if(WmNews.Status.SUBMIT.getCode().equals(wmNews.getStatus())){
            wmNews.setSubmitedTime(LocalDateTime.now());
        }
        return this.save(wmNews);
    }
}




