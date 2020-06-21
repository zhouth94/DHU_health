package com.dhu.controller;

import com.aliyuncs.exceptions.ClientException;
import com.dhu.constant.MessageConstant;
import com.dhu.constant.RedisMessageConstant;
import com.dhu.entity.Result;
import com.dhu.utils.SMSUtils;
import com.dhu.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.JedisPool;

/**
 * @author zhou
 * @create 2020/5/22
 */
@Controller
@RequestMapping("/validateCode")
public class ValidateCodeController {

    @Autowired
    private JedisPool jedisPool; //保存短信验证码

    /**
     * 用户在线体检预约发送验证码
     */
    @ResponseBody
    @RequestMapping("/send4Order")
    public Result send4Order(String telephone) {
        //随机生成4位数验证码
        Integer code = ValidateCodeUtils.generateValidateCode(4);
        try {
            //给用户发送验证码
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, String.valueOf(code));
        } catch (ClientException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        //将验证码保存至redis(5分钟),方便比对
        jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_ORDER, 5*60, code.toString());
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }

    //用户手机快速登录时发送验证码
    @RequestMapping("/send4Login")
    @ResponseBody
    public Result send4Login(String telephone) {
        Integer code = ValidateCodeUtils.generateValidateCode(6);
        System.out.println(code);
        try {
            //给用户发送验证码
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, String.valueOf(code));
        } catch (ClientException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        //将验证码保存至redis(5分钟),方便比对
        jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_LOGIN, 5*60, code.toString());
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }
}
