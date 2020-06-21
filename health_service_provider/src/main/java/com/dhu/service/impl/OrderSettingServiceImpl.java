package com.dhu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.dhu.entity.Result;
import com.dhu.mapper.OrderSettingMapper;
import com.dhu.pojo.OrderSetting;
import com.dhu.service.OrderSettingService;
import org.apache.poi.util.SystemOutLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author zhou
 * @create 2020/5/7
 */
@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    private OrderSettingMapper mapper;

    @Override
    public void add(List<OrderSetting> orders) {
        if(orders != null && orders.size() > 0) {
            for (OrderSetting order : orders) {
                long count = mapper.findCountByOrderDate(order.getOrderDate());
                if(count > 0) {
                    //若之前已经进行了预约设置，则执行更新
                    mapper.editNumberByOrderDate(order);
                }else {
                    mapper.insert(order);
                }
            }
        }
    }

    //根据月份查询预约设置数据
    @Override
    public List<Map> getOrderSettingByMonth(String date) { //格式：yyyy-MM
        String begin = date + "-1"; //2020-6-1
        String end = date + "-31";
        Map<String,String> map = new HashMap<>();
        map.put("begin", begin);
        map.put("end", end);
        List<Map> result = new ArrayList<>(); //返回结果集合

        List<OrderSetting> list = mapper.getOrderSettingByMonth(map); //查询每月日期显示到页面
        if(list != null && list.size() > 0) {
            Map<String,Object> target = null; //根据页面的json对象封装对应的map
            for (OrderSetting os : list) {
                target = new HashMap<>(); //每一次遍历对应一个map
                target.put("date", os.getOrderDate().getDate()); //返回此 Date 对象表示的月份中的某一天
                target.put("number", os.getNumber());
                target.put("reservations", os.getReservations());
                result.add(target);
            }
        }
        return result;
    }

    //根据日期设置对应的预约设置数据
    @Override
    public void editNumberByDate(OrderSetting orderSetting) {
        Date orderDate = orderSetting.getOrderDate();
        //根据日期查询是否已经进行了预约设置
        long count = mapper.findCountByOrderDate(orderDate);
        if(count > 0){
            //当前日期已经进行了预约设置，需要执行更新操作
            mapper.editNumberByOrderDate(orderSetting);
        }else{
            //当前日期没有就那些预约设置，需要执行插入操作
            mapper.insert(orderSetting);
        }
    }
}
