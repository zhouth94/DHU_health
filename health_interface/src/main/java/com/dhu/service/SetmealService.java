package com.dhu.service;

import com.dhu.entity.PageResult;
import com.dhu.entity.QueryPageBean;
import com.dhu.pojo.Setmeal;

import java.util.List;

/**
 * @author zhou
 * @create 2020/5/5
 */
public interface SetmealService {
    void add(Integer[] checkgroupIds, Setmeal setmeal);

    PageResult findPage(QueryPageBean queryPageBean);

    Setmeal findById(Integer id);

    List<Setmeal> findAll();
}
