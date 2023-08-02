package com.heima.schedule.test;


import com.heima.common.redis.CacheService;
import com.heima.model.schedule.dto.Task;
import com.heima.schedule.ScheduleApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Set;


@SpringBootTest(classes = ScheduleApplication.class)
@RunWith(SpringRunner.class)
public class RedisTest {

    @Resource
    private CacheService cacheService;

    @Test
    public void testList() {

        //在list的左边添加元素
//        cacheService.lLeftPush("list_001","hello,redis");

//        //在list的右边获取元素，并删除
        Task task = cacheService.lRightPop("leadnews:schedule:topic:0:0", Task.class);
        System.out.println(task);
    }

    @Test
    public void testZset() {
        //添加数据到zset中  分值
        /*cacheService.zAdd("zset_key_001","hello zset 001",1000);
        cacheService.zAdd("zset_key_001","hello zset 002",8888);
        cacheService.zAdd("zset_key_001","hello zset 003",7777);
        cacheService.zAdd("zset_key_001","hello zset 004",999999);*/


    }
}