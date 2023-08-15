package com.heima.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.heima.common.constants.ArticleConstants;
import com.heima.common.constants.SearchConstants;
import com.heima.common.redis.CacheService;
import com.heima.model.common.dto.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.search.dto.ApUserSearch;
import com.heima.model.search.dto.HistorySearchDto;
import com.heima.model.search.dto.UserSearchDto;
import com.heima.model.search.vo.SearchArticleVo;
import com.heima.model.user.entity.ApUser;
import com.heima.search.service.ArticleSearchService;
import com.heima.util.thread.AppThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import sun.util.resources.LocaleData;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

@Service
@Slf4j
public class ArticleSearchServiceImpl implements ArticleSearchService {

    @Resource
    private RestHighLevelClient restHighLevelClient;

    /**
     * es文章分页检索
     *
     * @param dto
     * @return
     */
    @Override
    public ResponseResult<?> search(UserSearchDto dto) throws IOException {

        //1.检查参数
        if (dto == null || StringUtils.isBlank(dto.getSearchWords())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        if (AppThreadLocalUtil.getUser() != null && dto.getFromIndex() == 0) {
            // 异步调用保存搜索记录
            insertHistory(dto.getSearchWords(), AppThreadLocalUtil.getUser().getId());
        }

        //2.设置查询条件
        SearchRequest searchRequest = new SearchRequest("app_info_article");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        //布尔查询
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        //关键字的分词之后查询
        QueryStringQueryBuilder queryStringQueryBuilder = QueryBuilders.queryStringQuery(dto.getSearchWords()).field("title").field("content").defaultOperator(Operator.OR);
        boolQueryBuilder.must(queryStringQueryBuilder);

        //查询小于mindate的数据
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("publishTime").lt(dto.getMinBehotTime().getTime());
        boolQueryBuilder.filter(rangeQueryBuilder);

        //分页查询
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(dto.getPageSize());

        //按照发布时间倒序查询
        searchSourceBuilder.sort("publishTime", SortOrder.DESC);

        //设置高亮  title
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("title");
        highlightBuilder.preTags("<font style='color: red; font-size: inherit;'>");
        highlightBuilder.postTags("</font>");
        searchSourceBuilder.highlighter(highlightBuilder);


        searchSourceBuilder.query(boolQueryBuilder);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);


        //3.结果封装返回

        List<Map<String, String>> list = new ArrayList<>();

        SearchHit[] hits = searchResponse.getHits().getHits();
        for (SearchHit hit : hits) {
            String json = hit.getSourceAsString();
            Map map = JSON.parseObject(json, Map.class);
            //处理高亮
            if (hit.getHighlightFields() != null && !hit.getHighlightFields().isEmpty()) {
                Text[] titles = hit.getHighlightFields().get("title").getFragments();
                String title = StringUtils.join(titles);
                //高亮标题
                map.put("h_title", title);
            } else {
                //原始标题
                map.put("h_title", map.get("title"));
            }
            list.add(map);
        }
        return ResponseResult.okResult(list);
    }

    @Resource
    private CacheService cacheService;

    @Override
    @Async
    public void insertHistory(String keyWord, Integer userId) {
        if (StringUtils.isBlank(keyWord) || userId == null) {
            return;
        }
        String key = SearchConstants.HISTORY + userId;
        cacheService.zAdd(key, keyWord, System.currentTimeMillis());
        Long size = cacheService.zZCard(key);
        if (size > 10) {
            cacheService.zRemoveRange(key, 0, size - 11);
        }
    }


    @RabbitListener(queues = ArticleConstants.ARTICLE_ES_SYNC)
    public void syncArticleListener(String message) throws IOException {
        if (StringUtils.isBlank(message)) {
            return;
        }
        SearchArticleVo searchArticleVo = JSON.parseObject(message, SearchArticleVo.class);
        if (searchArticleVo.getId() == null) return;
        IndexRequest indexRequest = new IndexRequest("app_info_article");
        indexRequest.id(searchArticleVo.getId().toString())
                .source(message, XContentType.JSON);
        restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
    }

    @Override
    public ResponseResult<?> listSearchHistory() {
        ApUser user = AppThreadLocalUtil.getUser();
        if (user == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }
        String key = SearchConstants.HISTORY + user.getId();
        Set<String> keyWords = cacheService.zReverseRangeAll(key);
        return ResponseResult.okResult(keyWords
                .stream()
                .map(ApUserSearch::new)
                .toArray());
    }

    @Override
    public ResponseResult<?> removeSearchHistory(HistorySearchDto historySearchDto) {
        ApUser user = AppThreadLocalUtil.getUser();
        if (user == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }
        String key = SearchConstants.HISTORY + user.getId();
        cacheService.zRemove(key, historySearchDto.getId());
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
}