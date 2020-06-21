package com.dhu.controller;

import com.alibaba.dubbo.config.annotation .Reference;
import com.dhu.constant.MessageConstant;
import com.dhu.entity.Result;
import com.dhu.pojo.Setmeal;
import com.dhu.service.SetmealService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author zhou
 * @create 2020/5/21
 */
@Controller
@RequestMapping("/setmeal")
public class SetmealController {

    @Reference
    private SetmealService setmealService;

    @ResponseBody
    @GetMapping("/getAllSetmeal")
    public Result getAllSetmeal() {
        try {
            List<Setmeal> list = setmealService.findAll();
            return new Result(true, MessageConstant.GET_SETMEAL_LIST_SUCCESS, list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_SETMEAL_LIST_FAIL);
        }
    }

    /**
     * 根据套餐id查询套餐详情（套餐基本信息、套餐包含的检查组信息、检查组包含的检查项信息）
     * @param id
     * @return
     */
    @RequestMapping("/findById")
    @ResponseBody
    public Result findById(Integer id) {
        try {
            Setmeal setmeal = setmealService.findById(id);
            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }
}
