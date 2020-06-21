package com.dhu.mapper;

import com.dhu.entity.PageResult;
import com.dhu.entity.QueryPageBean;
import com.dhu.pojo.CheckGroup;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

/**
 * @author zhou
 * @create 2020/4/30
 */
public interface CheckGroupMapper {
    //新增检查组
    void insert(CheckGroup checkGroup);

    void setCheckGroupAndCheckItem(Map<String,Integer> map);

    Page<CheckGroup> queryPageByCondition(String queryString);

    CheckGroup queryById(Integer id);

    List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

    void update(CheckGroup formData);

    void deleteAssociation(Integer id);

    List<CheckGroup> queryAll();

    //通过套餐id查询对应的检查项
    List<CheckGroup> findCheckGroupsBySetmealId(Integer id);
}
