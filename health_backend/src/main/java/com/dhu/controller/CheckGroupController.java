package com.dhu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.dhu.constant.MessageConstant;
import com.dhu.entity.PageResult;
import com.dhu.entity.QueryPageBean;
import com.dhu.entity.Result;
import com.dhu.pojo.CheckGroup;
import com.dhu.service.CheckGroupService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author zhou
 * @create 2020/4/30
 */
@Controller
@RequestMapping("/checkgroup")
public class CheckGroupController {

    @Reference
    private CheckGroupService cgs;

    @ResponseBody
    @RequestMapping("/add")
    public Result add(int[] checkitemIds, @RequestBody CheckGroup checkGroup) { //checkitemIds参数名需要和前端保持一致，才能够封装
        try {
            cgs.add(checkitemIds, checkGroup);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
        }
        return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);
    }

    @RequestMapping("/findPage")
    @ResponseBody
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        try {
            PageResult pg = cgs.findPage(queryPageBean);
            return pg;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("查询失败");
        }
    }

    @RequestMapping("/findById")
    @ResponseBody
    public Result findById(Integer id) {
        try {
            CheckGroup checkGroup = cgs.findById(id);
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, checkGroup);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }

    @ResponseBody
    @RequestMapping("/findCheckItemIdsByCheckGroupId")
    public Result findCheckItemIdsByCheckGroupId(Integer id) {
        try {
            List<Integer> list = cgs.findCheckItemIdsByCheckGroupId(id);
            return new Result(true, MessageConstant.QUERY_CHECKITEM_FAIL, list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKITEM_SUCCESS);
        }
    }

    @RequestMapping("/edit")
    @ResponseBody
    public Result edit(int[] checkitemIds, @RequestBody CheckGroup formData) {
        try {
            cgs.edit(checkitemIds, formData);
            return new Result(true, MessageConstant.EDIT_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
    }

    @ResponseBody
    @RequestMapping("/findAll")
    public Result findAll() {
        try {
            List<CheckGroup> list = cgs.findAll();
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }

}
