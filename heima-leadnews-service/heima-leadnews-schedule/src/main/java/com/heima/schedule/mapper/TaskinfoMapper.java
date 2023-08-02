package com.heima.schedule.mapper;

import com.heima.model.schedule.entity.Taskinfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Oreki
 * @description 针对表【taskinfo】的数据库操作Mapper
 * @createDate 2023-08-01 11:31:28
 * @Entity com.heima.model.schedule.entity.Taskinfo
 */
public interface TaskinfoMapper extends BaseMapper<Taskinfo> {
    List<Taskinfo> queryFutureTime(@Param("taskType") int type, @Param("priority") int priority, @Param("future") LocalDateTime future);
}




