package com.heima.wemedia.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.model.common.dto.PageResponseResult;
import com.heima.model.common.dto.ResponseResult;
import com.heima.model.wemedia.dto.WmNewsDto;
import com.heima.model.wemedia.dto.WmNewsPageReqDto;
import com.heima.model.wemedia.entity.WmNews;
import com.heima.util.thread.WmThreadLocalUtil;
import com.heima.wemedia.service.WmNewsService;
import com.heima.wemedia.mapper.WmNewsMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Oreki
 * @description 针对表【wm_news(自媒体图文内容信息表)】的数据库操作Service实现
 * @createDate 2023-07-29 12:10:53
 */
@Service
public class WmNewsServiceImpl extends ServiceImpl<WmNewsMapper, WmNews>
        implements WmNewsService {

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
    public ResponseResult<?> submitNews(WmNewsDto dto) {

    }
}




