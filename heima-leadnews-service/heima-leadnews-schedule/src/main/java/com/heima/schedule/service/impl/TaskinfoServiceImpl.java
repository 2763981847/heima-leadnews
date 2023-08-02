package com.heima.schedule.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.model.schedule.entity.Taskinfo;
import com.heima.schedule.service.TaskinfoService;
import com.heima.schedule.mapper.TaskinfoMapper;
import org.springframework.stereotype.Service;

/**
* @author Oreki
* @description 针对表【taskinfo】的数据库操作Service实现
* @createDate 2023-08-01 11:31:28
*/
@Service
public class TaskinfoServiceImpl extends ServiceImpl<TaskinfoMapper, Taskinfo>
    implements TaskinfoService{

}




