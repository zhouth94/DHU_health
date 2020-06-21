package com.dhu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.dhu.constant.MessageConstant;
import com.dhu.entity.PageResult;
import com.dhu.entity.QueryPageBean;
import com.dhu.entity.Result;
import com.dhu.pojo.CheckItem;
import com.dhu.service.CheckItemService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 检查项管理
 * @author zhou
 * @create 2020/4/27
 */
@Controller
@RequestMapping("/checkitem")
public class CheckItemController {
    @Reference //通过dubbo去zookeeper服务注册中心查找服务
    private CheckItemService cis;

    @ResponseBody
    @RequestMapping("/add")
    @PreAuthorize("hasAuthority('CHECKITEM_ADD')")//权限校验
    public Result add(@RequestBody CheckItem checkItem) {
        try {
            cis.add(checkItem);
        }catch (Exception e){
            e.printStackTrace();
            //服务调用失败
            return new Result(false, MessageConstant.ADD_CHECKITEM_FAIL);
        }
        return new Result(true, MessageConstant.ADD_CHECKITEM_SUCCESS);
    }

    @ResponseBody
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult = cis.queryPage(queryPageBean);
        return pageResult;
    }

    @PreAuthorize("hasAuthority('CHECKITEM_DELETE')") //权限校验
    @RequestMapping("/delete")
    @ResponseBody
    public Result deleteOne(Integer id) {
        try {
            cis.deleteById(id);
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_CHECKITEM_FAIL);
        }
        return new Result(true, MessageConstant.DELETE_CHECKITEM_SUCCESS);
    }

    @RequestMapping("/findById")
    @ResponseBody
    public Result findById(Integer id) {
        CheckItem checkItem;
        try {
            checkItem = cis.findById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
        }
        return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItem);
    }

    @PostMapping("/edit")
    @PreAuthorize("hasAuthority('CHECKITEM_EDIT')")//权限校验
    @ResponseBody
    public Result edit(@RequestBody CheckItem checkItem) {
        try {
            cis.editCheckItem(checkItem);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_CHECKITEM_FAIL);
        }
        return new Result(true, MessageConstant.EDIT_CHECKITEM_SUCCESS);
    }

    @RequestMapping("/findAll")
    @ResponseBody
    public Result findAll() {
        try {
            List<CheckItem> list =  cis.findAll();
            return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }


}
