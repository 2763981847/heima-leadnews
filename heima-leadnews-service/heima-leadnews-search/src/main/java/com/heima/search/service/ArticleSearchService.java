package com.heima.search.service;


import com.heima.model.common.dto.ResponseResult;
import com.heima.model.search.dto.UserSearchDto;

import java.io.IOException;

/**
 * @author Oreki
 */
public interface ArticleSearchService {

    /**
     * ES文章分页搜索
     *
     * @return
     */
    ResponseResult<?> search(UserSearchDto userSearchDto) throws IOException;
}