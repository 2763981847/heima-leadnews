package com.heima.search.service;


import com.heima.model.common.dto.ResponseResult;
import com.heima.model.search.dto.HistorySearchDto;
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

    ;

    void insertHistory(String keyWord, Integer userId);

    void syncArticleListener(String message) throws IOException;

    ResponseResult<?> listSearchHistory();

    ResponseResult<?> removeSearchHistory(HistorySearchDto historySearchDto);
}