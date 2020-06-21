package com.dhu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.aliyuncs.exceptions.ClientException;
import com.dhu.constant.MessageConstant;
import com.dhu.constant.RedisMessageConstant;
import com.dhu.entity.Result;
import com.dhu.pojo.Order;
import com.dhu.service.OrderService;
import com.dhu.utils.SMSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/**
 * 体检预约处理
 * @author zhou
 * @create 2020/5/26
 */
@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private JedisPool jedisPool;
    @Reference
    private OrderService orderService;

    @ResponseBody
    @RequestMapping("/submit")
    public Result submit(@RequestBody Map map) {
        //从Redis中获取保存的验证码，进行比对
        String telephone = (String) map.get("telephone");
        String validateCodeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
        String validateCode = (String) map.get("validateCode");
        if(validateCodeInRedis!=null && validateCode!=null && validateCodeInRedis.equals(validateCode)){
            map.put("orderType", Order.ORDERTYPE_WEIXIN); //设置预约类型
            Result result = null;
            try{
                result = orderService.order(map); //dubbo远程调用服务
            }catch(Exception e){
                e.printStackTrace();
                return result;
            }
            if(result.isFlag()) {
                //预约成功，给用户发送短信通知
                try {
                    SMSUtils.sendShortMessage(SMSUtils.ORDER_NOTICE, telephone, map.get("orderDate").toString());
                } catch (ClientException e) {
                    e.printStackTrace();
                }
            }
            return result;
        }else {
            //验证码比对失败
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
    }

    //根据预约ID查询预约信息
    @RequestMapping("/findById")
    @ResponseBody
    public Result findById(Integer id) {
        //注意：需要根据页面的显示数据类型来确定返回值的类型
        try{
            Map map = orderService.findById(id);
            return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS, map); //由SpringMVC将Map转为json
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_ORDER_FAIL);
        }
    }
}
