package com.dhu.jobs;

import com.dhu.constant.RedisConstant;
import com.dhu.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Set;

/**
 * 定时清理垃圾图片
 * @author zhou
 * @create 2020/5/7
 */
public class ClearImgJob {

    @Autowired
    private JedisPool jedisPool;

    public void clearImg() {
        //计算两个set集合的差值，删除垃圾图片
        Set<String> set = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        if(set != null && set.size() > 0) {
            for (String picName : set) {
                QiniuUtils.deleteFileFromQiniu(picName);
                jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES, picName);
                System.out.println("清理垃圾图片：" + picName);
            }
        }

    }
}
