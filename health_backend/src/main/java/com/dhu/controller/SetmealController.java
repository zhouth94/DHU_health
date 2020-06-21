package com.dhu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.dhu.constant.MessageConstant;
import com.dhu.constant.RedisConstant;
import com.dhu.entity.PageResult;
import com.dhu.entity.QueryPageBean;
import com.dhu.entity.Result;
import com.dhu.pojo.Setmeal;
import com.dhu.service.SetmealService;
import com.dhu.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.util.UUID;

/**
 * @author zhou
 * @create 2020/5/2
 */
@Controller
@RequestMapping("/setmeal")
public class SetmealController {
    @Reference
    private SetmealService ss;
    @Autowired
    private JedisPool jedisPool; //使用jedisPool操作redis服务

    /**
     * 文件上传
     * @param imgFile
     */
    @ResponseBody
    @RequestMapping("/upload")
    public Result upload(@RequestParam("imgFile") MultipartFile imgFile) {
        String filename = imgFile.getOriginalFilename();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        filename = uuid + filename.substring(filename.lastIndexOf("."));
        try {
            //文件上传到七牛云服务器
            QiniuUtils.upload2Qiniu(imgFile.getBytes(), filename);
            //将图片存入redis服务器
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES, filename);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
        return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS, filename); //filename -> data
    }

    @ResponseBody
    @PostMapping("/add")
    public Result add(Integer[] checkgroupIds, @RequestBody Setmeal setmeal) {
        try {
            ss.add(checkgroupIds, setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            new Result(false, MessageConstant.ADD_SETMEAL_FAIL);
        }
        return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
    }

    @ResponseBody
    @PostMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult= ss.findPage(queryPageBean);
        return pageResult;
    }
}
