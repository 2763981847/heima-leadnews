package com.heima.wemedia.service;

import com.heima.model.wemedia.entity.WmNews;

/**
 * @author Oreki
 */
public interface WmNewsAutoScanService {

    /**
     * 自媒体文章审核
     *
     * @param id 自媒体文章id
     */
    void autoScanWmNews(Integer id) ;

    void saveApArticle(WmNews wmNews);
}