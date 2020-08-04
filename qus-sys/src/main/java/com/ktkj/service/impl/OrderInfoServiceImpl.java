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
package com.ktkj.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ktkj.utils.QueryPlus;
import com.ktkj.dao.OrderInfoDao;
import com.ktkj.entity.OrderInfoEntity;
import com.ktkj.service.OrderInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Service实现类
 *
 * @author lipengjun
 * @date 2019-08-27 16:57:03
 */
@Service("orderInfoService")
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoDao, OrderInfoEntity> implements OrderInfoService {

    @Override
    public List<OrderInfoEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

    @Override
    public Page queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "T.order_id");
        params.put("asc", false);
        Page<OrderInfoEntity> page = new QueryPlus<OrderInfoEntity>(params).getPage();
        return page.setRecords(baseMapper.selectOrderInfoPage(page, params));
    }

    @Override
    public boolean add(OrderInfoEntity orderInfo) {
        return this.save(orderInfo);
    }

    @Override
    public boolean update(OrderInfoEntity orderInfo) {
        return this.updateById(orderInfo);
    }

    @Override
    public void scanExpress(OrderInfoEntity orderInfo){
        baseMapper.scanExpress(orderInfo);
    }

    @Override
    public boolean delete(String orderId) {
        return this.removeById(orderId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBatch(String[] orderIds) {
        return this.removeByIds(Arrays.asList(orderIds));
    }
}
