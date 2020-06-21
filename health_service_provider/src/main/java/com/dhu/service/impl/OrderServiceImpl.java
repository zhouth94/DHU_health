package com.dhu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.dhu.constant.MessageConstant;
import com.dhu.entity.Result;
import com.dhu.mapper.MemberMapper;
import com.dhu.mapper.OrderMapper;
import com.dhu.mapper.OrderSettingMapper;
import com.dhu.pojo.Member;
import com.dhu.pojo.Order;
import com.dhu.pojo.OrderSetting;
import com.dhu.service.OrderService;
import com.dhu.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.awt.geom.FlatteningPathIterator;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 体检预约服务
 * @author zhou
 * @create 2020/5/26
 */
@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderSettingMapper orderSettingMapper;

    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Override
    //体检预约
    public Result order(Map map) throws Exception{
        //1、检查用户所选择的预约日期是否已经提前进行了预约设置，如果没有设置则无法进行预约
        String orderDate = (String) map.get("orderDate");
        OrderSetting orderSetting = orderSettingMapper.findByOrderDate(DateUtils.parseString2Date(orderDate));
        if(orderSetting == null) { //指定日期没有进行预约设置
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }
        //2、检查用户所选择的预约日期是否已经约满，如果已经约满则无法预约
        int number = orderSetting.getNumber(); //可预约人数
        int reservations = orderSetting.getReservations();
        if(reservations >= number) { //已约满
            return new Result(false, MessageConstant.ORDER_FULL);
        }
        //3、检查用户是否重复预约（同一个用户在同一天预约了同一个套餐），如果是重复预约则无法完成再次预约
        String telephone = (String) map.get("telephone");
        Member member = memberMapper.findByTelephone(telephone);
        if(member != null) {
            //判断是否重复预约
            Integer memberId = member.getId();
            Date date = DateUtils.parseString2Date(orderDate); //预约日期
            Integer setmealId = (Integer) map.get("setmealId"); //套餐id
            Order order = new Order(memberId, date, setmealId);
            //根据条件进行动态sql查询
            List<Order> list = orderMapper.findByCondition(order);
            if(list!=null && list.size()>0) { //说明用户在重复预约
                return new Result(false, MessageConstant.HAS_ORDERED);
            }
        }else {
            //4、检查当前用户是否为会员，如果是会员则直接完成预约，如果不是会员则自动完成注册并进行预约
            member = new Member();
            member.setName((String) map.get("name"));
            member.setPhoneNumber(telephone);
            member.setIdCard((String) map.get("idCard"));
            member.setSex((String) map.get("sex"));
            member.setRegTime(new Date());
            memberMapper.add(member); //完成会员自动注册
        }
        //5、预约成功，更新当日的已预约人数
        Order order = new Order();
        order.setMemberId(member.getId());
        order.setOrderDate(DateUtils.parseString2Date(orderDate));
        order.setOrderType((String) map.get("orderType"));
        order.setOrderStatus(Order.ORDERSTATUS_NO);
        order.setSetmealId(Integer.parseInt((String)map.get("setmealId")));
        orderMapper.add(order);

        //更新orderSetting，设置已预约人数
        orderSetting.setReservations(orderSetting.getReservations() + 1);
        orderSettingMapper.editReservationsByOrderDate(orderSetting);
        return new Result(true, MessageConstant.ORDER_SUCCESS, order);
    }

    @Override
    //根据预约order的id查询相应的字段，进行封装（体检人、体检套餐、体检日期、预约类型）
    public Map findById(Integer id) throws Exception {
        Map map = orderMapper.findById4Detail(id);
        if(map != null) {
            //处理日期格式
            Date orderDate = (Date) map.get("orderDate");
            map.put("orderDate", DateUtils.parseDate2String(orderDate));
        }
        return map;
    }
}
