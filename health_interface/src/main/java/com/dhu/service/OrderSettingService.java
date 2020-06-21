package com.dhu.service;

import com.dhu.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

/**
 * @author zhou
 * @create 2020/5/7
 */
public interface OrderSettingService {
    void add(List<OrderSetting> orders);

    List<Map> getOrderSettingByMonth(String date);

    void editNumberByDate(OrderSetting orderSetting);
}
