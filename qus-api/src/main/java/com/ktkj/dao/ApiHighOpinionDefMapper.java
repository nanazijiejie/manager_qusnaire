/*
 * 类名称:HighOpinionDefDao.java
 * 包名称:com.ktkj.dao
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-08-27 16:57:02        lipengjun     初版做成
 *
 * Copyright (c) 2019-2019 厦门继续想科技有限公司
 */
package com.ktkj.dao;

import com.ktkj.entity.HighOpinionDefVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Dao
 *
 * @author lipengjun
 * @date 2019-08-27 16:57:02
 */
@Mapper
public interface ApiHighOpinionDefMapper extends BaseDao<HighOpinionDefVo> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<HighOpinionDefVo> queryAll(@Param("params") Map<String, Object> params);
}
