package com.heima.wemedia.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.model.wemedia.entity.WmSensitive;
import com.heima.wemedia.service.WmSensitiveService;
import com.heima.wemedia.mapper.WmSensitiveMapper;
import org.springframework.stereotype.Service;

/**
* @author Oreki
* @description 针对表【wm_sensitive(敏感词信息表)】的数据库操作Service实现
* @createDate 2023-07-31 20:48:55
*/
@Service
public class WmSensitiveServiceImpl extends ServiceImpl<WmSensitiveMapper, WmSensitive>
    implements WmSensitiveService{

}




