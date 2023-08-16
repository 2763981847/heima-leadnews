package com.heima.model.wemedia.vo;

import com.heima.model.wemedia.entity.WmNews;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Fu Qiujie
 * @since 2023/8/16
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WmNewsVo extends WmNews {
    private String authorName;
}
