package com.dhu.service;

import com.dhu.entity.Result;

import java.util.Map;

/**
 * 预约
 * @author zhou
 * @create 2020/5/26
 */
public interface OrderService {
    Result order(Map map) throws Exception;

    Map findById(Integer id) throws Exception;
}
