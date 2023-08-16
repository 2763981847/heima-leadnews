package com.heima.model.behavior.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JacksonAnnotation;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author Fu Qiujie
 * @since 2023/8/16
 */
@Data
public class BehaviorInfoVo {
    @JsonProperty("islike")
    private Boolean isLike;
    @JsonProperty("isunlike")
    private Boolean isUnlike;
    @JsonProperty("iscollecion")
    private Boolean isCollection;
    @JsonProperty("isfollow")
    private Boolean isFollow;
}
