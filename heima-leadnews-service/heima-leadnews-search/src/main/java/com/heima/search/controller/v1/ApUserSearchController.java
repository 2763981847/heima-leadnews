package com.heima.search.controller.v1;

import com.heima.model.common.dto.ResponseResult;
import com.heima.model.search.dto.HistorySearchDto;
import com.heima.search.service.ArticleSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * APP用户搜索信息表 前端控制器
 * </p>
 *
 * @author itheima
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/history")
public class ApUserSearchController {

    @Resource
    private ArticleSearchService articleSearchService;

    @PostMapping("/load")
    public ResponseResult<?> findUserSearch() {
        return articleSearchService.listSearchHistory();
    }

    @PostMapping("/del")
    public ResponseResult<?> delUserSearch(@RequestBody HistorySearchDto historySearchDto) {
        return articleSearchService.removeSearchHistory(historySearchDto);
    }
}