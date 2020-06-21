package com.dhu.mapper;

import com.dhu.pojo.Setmeal;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

/**
 * @author zhou
 * @create 2020/5/5
 */
public interface SetmealMapper {
    void insert(Setmeal setmeal);

    void setSetmealAndCheckGroup(Map<String, Integer> map);

    Page<Setmeal> queryByCondition(String queryString);

    List<Setmeal> findAll();

    Setmeal findById(Integer id);
}
