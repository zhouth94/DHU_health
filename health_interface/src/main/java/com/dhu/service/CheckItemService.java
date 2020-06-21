package com.dhu.service;

import com.dhu.entity.PageResult;
import com.dhu.entity.QueryPageBean;
import com.dhu.entity.Result;
import com.dhu.pojo.CheckItem;

import java.util.List;

/**
 * 服务接口
 * @author zhou
 * @create 2020/4/27
 */
public interface CheckItemService {
    int add(CheckItem ci);

    PageResult queryPage(QueryPageBean queryPageBean);

    int deleteById(Integer id);

    CheckItem findById(Integer id);

    void editCheckItem(CheckItem checkItem);


    List<CheckItem> findAll();
}
