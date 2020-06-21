package com.dhu.controller;

import com.dhu.constant.MessageConstant;
import com.dhu.entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author zhou
 * @create 2020/5/28
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @RequestMapping("/getUsername")
    @ResponseBody
    public Result getUsername() {
        //当Spring Security完成认证后，会将当前用户信息保存到框架提供的上下文，底层基于HttpSession
        User user = null; //框架的User
        try {
            user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(user != null) {
                System.out.println(user);
                String username = user.getUsername();
                return new Result(true, MessageConstant.GET_USERNAME_SUCCESS, username);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false, MessageConstant.GET_USERNAME_FAIL);
    }
}
