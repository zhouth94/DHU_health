package com.dhu.mapper;

import com.dhu.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author zhou
 * @create 2020/5/7
 */
public interface OrderSettingMapper {
    long findCountByOrderDate(Date orderDate);

    void insert(OrderSetting orderSetting);

    void editNumberByOrderDate(OrderSetting orderSetting);

    List<OrderSetting> getOrderSettingByMonth(Map<String, String> map);

    OrderSetting findByOrderDate(Date orderDate);

    //更新已预约人数
    public void editReservationsByOrderDate(OrderSetting orderSetting);
}
