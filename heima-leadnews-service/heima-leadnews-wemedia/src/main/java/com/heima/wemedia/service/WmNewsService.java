package com.heima.wemedia.service;

import com.heima.model.common.dto.ResponseResult;
import com.heima.model.wemedia.dto.NewsAuthDto;
import com.heima.model.wemedia.dto.WmNewsDto;
import com.heima.model.wemedia.dto.WmNewsPageReqDto;
import com.heima.model.wemedia.dto.WmNewsUpDownDto;
import com.heima.model.wemedia.entity.WmNews;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author Oreki
 * @description 针对表【wm_news(自媒体图文内容信息表)】的数据库操作Service
 * @createDate 2023-07-29 12:10:53
 */
public interface WmNewsService extends IService<WmNews> {

    ResponseResult<List<WmNews>> listNews(WmNewsPageReqDto wmNewsPageReqDto);

    ResponseResult<?> submitNews(WmNewsDto dto);

    ResponseResult<?> deleteNews(Integer id);

    ResponseResult<?> downOrUp(WmNewsUpDownDto wmNewsUpDownDto);

    ResponseResult<?> listNewsVo(NewsAuthDto newsAuthDto);

    ResponseResult<?> getNewsVoById(Integer id);

    ResponseResult<?> authNews(NewsAuthDto newsAuthDto);

    void updateScanInfo(Integer newsId, WmNews.Status status, String reason);
}
