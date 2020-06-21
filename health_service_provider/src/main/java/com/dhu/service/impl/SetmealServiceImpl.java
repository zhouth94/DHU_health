package com.dhu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.dhu.constant.RedisConstant;
import com.dhu.entity.PageResult;
import com.dhu.entity.QueryPageBean;
import com.dhu.mapper.CheckGroupMapper;
import com.dhu.mapper.CheckItemMapper;
import com.dhu.mapper.SetmealMapper;
import com.dhu.pojo.CheckGroup;
import com.dhu.pojo.CheckItem;
import com.dhu.pojo.Setmeal;
import com.dhu.service.SetmealService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhou
 * @create 2020/5/5
 */
@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private CheckGroupMapper checkGroupMapper;
    @Autowired
    private CheckItemMapper checkItemMapper;
    @Autowired
    private JedisPool jedisPool; //使用jedisPool操作redis服务
    @Override
    public void add(Integer[] checkgroupIds, Setmeal setmeal) {
        setmealMapper.insert(setmeal);
        this.setSetmealAndCheckGroup(checkgroupIds, setmeal);
        //将图片保存到redis
        String filename = setmeal.getImg();
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES, filename);
    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        Page<Setmeal> page = setmealMapper.queryByCondition(queryPageBean.getQueryString());
        PageResult pageResult = new PageResult(page.getTotal(), page.getResult());
        return pageResult;
    }

    @Override
    //根据套餐id查询套餐详情（套餐基本信息、套餐包含的检查组信息、检查组包含的检查项信息）
    public Setmeal findById(Integer id) {
        Setmeal setmeal =  setmealMapper.findById(id);
        //通过套餐id查询对应的检查组
        List<CheckGroup> checkGroups = checkGroupMapper.findCheckGroupsBySetmealId(id);
        //通过检查组id查询对应的检查项
        for (int i = 0; i < checkGroups.size(); i++) {
            List<CheckItem> checkItems = checkItemMapper.findCheckItemsByCheckGroupId(checkGroups.get(i).getId());
            checkGroups.get(i).setCheckItems(checkItems);
        }
        setmeal.setCheckGroups(checkGroups);
        return setmeal;
    }

    @Override
    public List<Setmeal> findAll() {
        List<Setmeal> list = setmealMapper.findAll();
        return list;
    }

    public void setSetmealAndCheckGroup(Integer[] checkgroupIds, Setmeal setmeal) {
        if(checkgroupIds != null && checkgroupIds.length > 0) {
            Map<String, Integer> map = null;
            for (Integer checkgroupId : checkgroupIds) {
                map = new HashMap<>();
                map.put("setmealId", setmeal.getId());
                map.put("checkgroupId", checkgroupId);
                setmealMapper.setSetmealAndCheckGroup(map);
            }
        }
    }
}
