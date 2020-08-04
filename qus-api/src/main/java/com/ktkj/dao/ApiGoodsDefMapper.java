/*
 * 类名称:GoodsDefDao.java
 * 包名称:com.ktkj.dao
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-08-27 16:57:02        lipengjun     初版做成
 *
 * Copyright (c) 2019-2019 厦门继续想科技有限公司
 */
package com.ktkj.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ktkj.entity.GoodsDefVo;
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
public interface ApiGoodsDefMapper extends BaseDao<GoodsDefVo> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<GoodsDefVo> queryAll(@Param("params") Map<String, Object> params);

    /**
     * 自定义分页查询
     *
     * @param page   分页参数
     * @param params 查询参数
     * @return List
     */
    List<GoodsDefVo> selectGoodsDefPage(IPage page, @Param("params") Map<String, Object> params);
}
