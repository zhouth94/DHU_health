package com.dhu.service;

import com.dhu.entity.PageResult;
import com.dhu.entity.QueryPageBean;
import com.dhu.pojo.CheckGroup;

import java.util.List;

/**
 * @author zhou
 * @create 2020/4/30
 */
public interface CheckGroupService {
    void add(int[] checkitemIds,CheckGroup checkGroup);

    PageResult findPage(QueryPageBean queryPageBean);

    CheckGroup findById(Integer id);

    List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

    void edit(int[] checkitemIds, CheckGroup formData);

    List<CheckGroup> findAll();
}
