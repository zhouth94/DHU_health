package com.dhu.mapper;

import com.dhu.entity.PageResult;
import com.dhu.entity.QueryPageBean;
import com.dhu.entity.Result;
import com.dhu.pojo.CheckGroup;
import com.dhu.pojo.CheckItem;
import com.github.pagehelper.Page;

import java.util.List;

/**
 * @author zhou
 * @create 2020/4/27
 */
public interface CheckItemMapper {
    int add(CheckItem checkItem);

    Page<CheckItem> queryByCondition(String condition);

    long findCountByCheckItemId(Integer id);
    int deleteById(Integer id);

    CheckItem findById(Integer id);

    void update(CheckItem checkItem);

    List<CheckItem> queryAll();

    //通过检查组id查询对应的检查项
    List<CheckItem> findCheckItemsByCheckGroupId(Integer id);
}
