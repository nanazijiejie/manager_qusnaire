/*
 * 类名称:OrderInfoServiceImpl.java
 * 包名称:com.ktkj.service.impl
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-08-27 16:57:03        lipengjun     初版做成
 *
 * Copyright (c) 2019-2019 厦门继续想科技有限公司
 */
package com.ktkj.service;

import com.ktkj.dao.ApiOrderInfoMapper;
import com.ktkj.entity.OrderInfoVo;
import com.ktkj.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service实现类
 *
 * @author lipengjun
 * @date 2019-08-27 16:57:03
 */
@Service("apiOrderInfoService")
public class ApiOrderInfoService {
    @Autowired
    private ApiOrderInfoMapper orderInfoDao;

    public List<OrderInfoVo> queryAll(Map<String, Object> params) {
        return orderInfoDao.queryAll(params);
    }

    public OrderInfoVo add(OrderInfoVo orderInfo) {
        orderInfo.setOrderId(generateOrderId(orderInfo));
        orderInfo.setOrderStatus("0");
        orderInfo.setCreateTime(new Date());
        orderInfoDao.insert(orderInfo);
        return orderInfo;
    }

    private String generateOrderId(OrderInfoVo orderInfo) {
        Date nowDate = new Date();
        String orderId = DateUtils.format(nowDate, "yyyyMMdd");
        orderId += orderInfo.getContactPhone().substring(7, 11);
        String timeStr = String.valueOf(System.currentTimeMillis());
        orderId += timeStr.substring(timeStr.length() - 4, timeStr.length());
        return orderId;
    }

    public OrderInfoVo queryById(String orderId) {
        OrderInfoVo orderInfoVo = new OrderInfoVo();
        Map<String, Object> params = new HashMap<>();
        params.put("orderId", orderId);
        List<OrderInfoVo> orderInfoVoList = orderInfoDao.queryAll(params);
        if (orderInfoVoList.size() > 0) {
            orderInfoVo = orderInfoVoList.get(0);
        }
        return orderInfoVo;
    }

    public int update(OrderInfoVo orderInfo) {
        return orderInfoDao.update(orderInfo);
    }

}
