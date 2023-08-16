package com.heima.wemedia.service;

import com.heima.model.common.dto.ResponseResult;
import com.heima.model.wemedia.dto.SensitiveDto;
import com.heima.model.wemedia.entity.WmSensitive;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Oreki
* @description 针对表【wm_sensitive(敏感词信息表)】的数据库操作Service
* @createDate 2023-07-31 20:48:55
*/
public interface WmSensitiveService extends IService<WmSensitive> {

    ResponseResult<?> listSensitiveWords(SensitiveDto sensitiveDto);

    ResponseResult<?> saveSensitiveWord(WmSensitive wmSensitive);

    ResponseResult<?> updateSensitiveWord(WmSensitive wmSensitive);
}
