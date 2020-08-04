/*
 * 类名称:OrderInfoDao.java
 * 包名称:com.ktkj.dao
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-08-27 16:57:03        lipengjun     初版做成
 *
 * Copyright (c) 2019-2019 厦门继续想科技有限公司
 */
package com.ktkj.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ktkj.entity.OrderInfoEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Dao
 *
 * @author lipengjun
 * @date 2019-08-27 16:57:03
 */
@Mapper
public interface OrderInfoDao extends BaseMapper<OrderInfoEntity> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<OrderInfoEntity> queryAll(@Param("params") Map<String, Object> params);

    /**
     * 自定义分页查询
     *
     * @param page   分页参数
     * @param params 查询参数
     * @return List
     */
    List<OrderInfoEntity> selectOrderInfoPage(IPage page, @Param("params") Map<String, Object> params);

    void scanExpress(OrderInfoEntity orderInfo);
}
