package com.heima.model.user.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author Fu Qiujie
 * @since 2023/7/24
 */
@Data
public class LoginUserVo {
    private Integer id;

    private String name;

    private String phone;
}
