package com.heima.search.controller.v1;

import com.heima.model.common.dto.ResponseResult;
import com.heima.model.search.dto.UserSearchDto;
import com.heima.search.service.ArticleSearchService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author Oreki
 */
@RestController
@RequestMapping("/api/v1/article/search")
public class ArticleSearchController {
    @Resource
    private ArticleSearchService articleSearchService;


    @PostMapping("/search")
    public ResponseResult<?> search(@RequestBody UserSearchDto dto) throws IOException {
        return articleSearchService.search(dto);
    }


}
