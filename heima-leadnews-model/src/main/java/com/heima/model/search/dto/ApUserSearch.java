package com.heima.model.search.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * APP用户搜索信息表
 * </p>
 *
 * @author itheima
 */
@Data
@NoArgsConstructor
public class ApUserSearch implements Serializable {

    private static final long serialVersionUID = 1L;

    public ApUserSearch(String keyword) {
        this.keyword = keyword;
        this.id = keyword;
    }

    /**
     * 主键
     */
    private String id;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 搜索词
     */
    private String keyword;

    /**
     * 创建时间
     */
    private Date createdTime;

}