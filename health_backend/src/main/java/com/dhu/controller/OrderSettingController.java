package com.dhu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.dhu.constant.MessageConstant;
import com.dhu.entity.Result;
import com.dhu.pojo.OrderSetting;
import com.dhu.service.OrderSettingService;
import com.dhu.utils.POIUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author zhou
 * @create 2020/5/7
 */
@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {

    @Reference
    private OrderSettingService oss;

    @RequestMapping("/upload")
    public Result upload(@RequestParam("excelFile") MultipartFile excel) {
        try {
            List<String[]> list = POIUtils.readExcel(excel);
            List<OrderSetting> orders = new ArrayList<>();
            for (String[] s : list) {
                String orderDateStr = s[0];
                Date orderDate = new SimpleDateFormat("yyyy/MM/dd").parse(orderDateStr);
                int number = Integer.parseInt(s[1]);
                OrderSetting orderSetting = new OrderSetting(orderDate, number);
                orders.add(orderSetting);
            }
            //通过dubbo远程调用服务实现数据批量导入到数据库
            oss.add(orders);
            return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
    }

    @PostMapping("/getOrderSettingByMonth")
    public Result getOrderSettingByMonth(String date) {
        try {
            List<Map> list = oss.getOrderSettingByMonth(date);
            return new Result(true, MessageConstant.GET_ORDERSETTING_SUCCESS, list);
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_ORDERSETTING_FAIL);
        }
    }

    @RequestMapping("/editNumberByDate")
    public Result editNumberByDate(@RequestBody OrderSetting orderSetting) {
        try{
            oss.editNumberByDate(orderSetting);
            return new Result(true,MessageConstant.ORDERSETTING_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.ORDERSETTING_FAIL);
        }
    }
}
