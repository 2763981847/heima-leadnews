package com.heima.common.constants;

/**
 * @author Fu Qiujie
 * @since 2023/7/26
 */
public class ArticleConstants {

    public static final String PREFIX = "leadnews:article:";
    public static final Short LOAD_MORE = 1;
    public static final Short LOAD_NEW = 2;
    public static final String DEFAULT_TAG = "__all__";

    public static final int DEFAULT_PAGE_SIZE = 10;

    public static final int MAX_PAGE_SIZE = 50;


    public static final String ARTICLE_ES_SYNC = "articleEsSync";

    public static final Integer HOT_ARTICLE_LIKE_WEIGHT = 3;
    public static final Integer HOT_ARTICLE_COMMENT_WEIGHT = 5;
    public static final Integer HOT_ARTICLE_COLLECTION_WEIGHT = 8;

    public static final String HOT_ARTICLE = PREFIX + "hotArticle:";
}
