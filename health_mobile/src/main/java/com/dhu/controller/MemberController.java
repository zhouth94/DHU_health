package com.dhu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.dhu.constant.MessageConstant;
import com.dhu.constant.RedisMessageConstant;
import com.dhu.entity.Result;
import com.dhu.pojo.Member;
import com.dhu.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * 会员相关操作
 * @author zhou
 * @create 2020/5/27
 */
@Controller
@RequestMapping("/member")
public class MemberController {
    @Autowired
    private JedisPool jedisPool;
    @Reference
    private MemberService memberService;

    @RequestMapping("/login")
    @ResponseBody
    public Result login(@RequestBody Map map, HttpServletResponse response) {
        String telephone = (String) map.get("telephone");
        System.out.println("telephone --- " + telephone);
        //从Redis中获取保存的验证码
        String validateCodeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);
        String validateCode = (String) map.get("validateCode");
        if(validateCode!=null && validateCodeInRedis!=null && validateCode.equals(validateCodeInRedis)) {
            //判断当前用户是否会员
            Member member = memberService.findByTelephone(telephone);
            if(member == null) {
                member = new Member();
                //自动完成注册
                member.setPhoneNumber(telephone);
                member.setRegTime(new Date());
                memberService.add(member);
            }
            //向客户端浏览器写入Cookie，内容为手机号，用于跟踪用户，系统知道是哪个用户
            Cookie cookie = new Cookie("login_member_telephone", telephone);
            cookie.setPath("/");
            cookie.setMaxAge(60*60*24*30); //一个月
            response.addCookie(cookie);
            //将会员信息保存到Redis --- 键 时间(秒) 值
            //Redis不能直接存java对象，先将member对象序列化为json串
            /*Object o = JSON.toJSON(member);
            String json = o.toString();*/
            String json = JSON.toJSONString(member); //json串
            jedisPool.getResource().setex(telephone, 60*30, json);
            return new Result(true, MessageConstant.LOGIN_SUCCESS);
        }else{
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }

    }
}
