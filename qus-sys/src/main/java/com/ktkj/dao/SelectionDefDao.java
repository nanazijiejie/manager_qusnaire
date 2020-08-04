/*
 * 类名称:SelectionInfoDao.java
 * 包名称:com.ktkj.dao
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2020-02-21 22:35:28        lipengjun     初版做成
 *
 * Copyright (c) 2019-2019 厦门继续想科技有限公司
 */
package com.ktkj.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ktkj.entity.SelRetEntity;
import com.ktkj.entity.SelectionDefEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Dao
 *
 * @author lipengjun
 * @date 2020-02-21 22:35:28
 */
@Mapper
public interface SelectionDefDao extends BaseMapper<SelectionDefEntity> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<SelectionDefEntity> queryAll(@Param("params") Map<String, Object> params);

    /**
     * 自定义分页查询
     *
     * @param page   分页参数
     * @param params 查询参数
     * @return List
     */
    List<SelectionDefEntity> selectSelectionDefPage(IPage page, @Param("params") Map<String, Object> params);

    /**
     * 获取归属单位地市
     *
     * @return List
     */
    List<SelectionDefEntity> querySelCity();

    Integer selCountByStation(@Param("params") Map<String, Object> params);

    List<SelRetEntity> querySelRet();

    List<SelRetEntity> querySelCount();
}
