package com.heima.wemedia.controller.v1;

import com.heima.model.common.dto.PageResponseResult;
import com.heima.model.common.dto.ResponseResult;
import com.heima.model.wemedia.dto.WmNewsDto;
import com.heima.model.wemedia.dto.WmNewsPageReqDto;
import com.heima.model.wemedia.entity.WmNews;
import com.heima.wemedia.service.WmNewsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Fu Qiujie
 * @since 2023/7/29
 */
@RestController
@RequestMapping("/api/v1/news")
public class WmNewsController {

    @Resource
    private WmNewsService wmNewsService;

    @PostMapping("/list")
    public ResponseResult<List<WmNews>> listNews(@RequestBody WmNewsPageReqDto wmNewsPageReqDto) {
        return wmNewsService.listNews(wmNewsPageReqDto);
    }

    @PostMapping("/submit")
    public ResponseResult<?> submitNews(@RequestBody WmNewsDto dto) {
        return wmNewsService.submitNews(dto);
    }
}
