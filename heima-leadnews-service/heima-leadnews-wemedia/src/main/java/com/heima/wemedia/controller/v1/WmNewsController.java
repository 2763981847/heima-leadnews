package com.heima.wemedia.controller.v1;

import cn.hutool.core.bean.BeanUtil;
import com.heima.model.common.dto.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.wemedia.dto.NewsAuthDto;
import com.heima.model.wemedia.dto.WmNewsDto;
import com.heima.model.wemedia.dto.WmNewsPageReqDto;
import com.heima.model.wemedia.dto.WmNewsUpDownDto;
import com.heima.model.wemedia.entity.WmNews;
import com.heima.model.wemedia.vo.WmNewsVo;
import com.heima.wemedia.service.WmNewsService;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("one/{id}")
    public ResponseResult<?> getById(@PathVariable("id") Integer id){
        WmNews wmNews = wmNewsService.getById(id);
        if (wmNews == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST ,"文章不存在");
        }
        return ResponseResult.okResult(wmNews);
    }

    @GetMapping("del_news/{id}")
    public ResponseResult<?> deleteNews(@PathVariable("id") Integer id){
       return wmNewsService.deleteNews(id);
    }

    @PostMapping("down_or_up")
    public ResponseResult<?> downOrUp(@RequestBody WmNewsUpDownDto wmNewsUpDownDto){
        return wmNewsService.downOrUp(wmNewsUpDownDto);
    }

    @PostMapping("/list_vo")
    public ResponseResult<?> listNewsVo(@RequestBody NewsAuthDto newsAuthDto) {
        return wmNewsService.listNewsVo(newsAuthDto);
    }

    @GetMapping("/one_vo/{id}")
    public ResponseResult<?> getNewsVoById(@PathVariable("id") Integer id){
        return wmNewsService.getNewsVoById(id);
    }

    @PostMapping("/auth_fail")
    public ResponseResult<?> authFail(@RequestBody NewsAuthDto newsAuthDto){
        newsAuthDto.setStatus(Integer.valueOf(WmNews.Status.FAIL.getCode()));
        return wmNewsService.authNews(newsAuthDto);
    }

    @PostMapping("/auth_pass")
    public ResponseResult<?> authPass(@RequestBody NewsAuthDto newsAuthDto){
        newsAuthDto.setStatus(Integer.valueOf(WmNews.Status.SUCCESS.getCode()));
        return wmNewsService.authNews(newsAuthDto);
    }
}
