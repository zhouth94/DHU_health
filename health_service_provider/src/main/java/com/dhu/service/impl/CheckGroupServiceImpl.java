package com.dhu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.dhu.entity.PageResult;
import com.dhu.entity.QueryPageBean;
import com.dhu.mapper.CheckGroupMapper;
import com.dhu.pojo.CheckGroup;
import com.dhu.service.CheckGroupService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhou
 * @create 2020/4/30
 */
@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    private CheckGroupMapper checkGroupMapper;

    //新增检查组，同时需要关联检查项
    public void add(int[] checkitemIds,CheckGroup checkGroup) {
        //1.设置检查组和检查项的多对多关联关系，操作t_checkgroup_checkitem
        this.setCheckGroupAndCheckItem(checkitemIds, checkGroup);
        //2.新增检查组，操作t_checkgroup
        checkGroupMapper.insert(checkGroup);
    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());//不需要返回值
        //重点 => 返回Page对象
        Page<CheckGroup> page = checkGroupMapper.queryPageByCondition(queryPageBean.getQueryString());
        PageResult pageResult = new PageResult(page.getTotal(), page.getResult());
        return pageResult;
    }

    @Override
    public CheckGroup findById(Integer id) {
        CheckGroup checkGroup = checkGroupMapper.queryById(id);
        return checkGroup;
    }

    @Override
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id) {
        return checkGroupMapper.findCheckItemIdsByCheckGroupId(id);
    }

    @Override
    public void edit(int[] checkitemIds, CheckGroup formData) {
        //1.清除编辑操作之前的检查组和检查项的关联关系
        checkGroupMapper.deleteAssociation(formData.getId());
        //2.设置新的检查组和检查项的关联关系
        this.setCheckGroupAndCheckItem(checkitemIds, formData);
        //3.编辑检查组
        checkGroupMapper.update(formData);
    }

    @Override
    public List<CheckGroup> findAll() {
        return checkGroupMapper.queryAll();
    }

    //设置新的检查组和检查项的关联关系
    public void setCheckGroupAndCheckItem(int[] checkitemIds, CheckGroup formData) {
        if(checkitemIds != null && checkitemIds.length > 0) {
            Map<String,Integer> map = null; //变量申明在循环外，可以减少堆内存对象地址
            for (int checkItemId : checkitemIds) {
                map = new HashMap<String, Integer>();
                map.put("checkgroup_id", formData.getId()); //通过xml配置获取自增id
                map.put("checkitem_id", checkItemId);
                checkGroupMapper.setCheckGroupAndCheckItem(map);
            }
        }
    }


}
