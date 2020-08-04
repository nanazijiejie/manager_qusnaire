/*
 * 类名称:IndexItemDefDao.java
 * 包名称:com.ktkj.dao
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-10-09 15:21:53        lipengjun     初版做成
 *
 * Copyright (c) 2019-2019 厦门继续想科技有限公司
 */
package com.ktkj.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ktkj.entity.IndexItemDefAddEntity;
import com.ktkj.entity.IndexItemDefEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Dao
 *
 * @author lipengjun
 * @date 2019-10-09 15:21:53
 */
@Mapper
public interface IndexItemDefDao extends BaseMapper<IndexItemDefEntity> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<IndexItemDefEntity> queryAll(@Param("params") Map<String, Object> params);

    /**
     * 自定义分页查询
     *
     * @param page   分页参数
     * @param params 查询参数
     * @return List
     */
    List<IndexItemDefEntity> selectIndexItemDefPage(IPage page, @Param("params") Map<String, Object> params);

    /**
     * 查询考核项对应的指标项
     * @param params
     * @return
     */
    List<IndexItemDefAddEntity> queryExamIndex(@Param("params") Map<String, Object> params);
}
