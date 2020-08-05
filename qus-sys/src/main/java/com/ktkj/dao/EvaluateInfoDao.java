/*
 * 类名称:EvaluateInfoDao.java
 * 包名称:com.ktkj.dao
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2020-03-24 09:58:44        lipengjun     初版做成
 *
 * Copyright (c) 2019-2019 厦门继续想科技有限公司
 */
package com.ktkj.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ktkj.entity.EvaluateInfoEntity;
import com.ktkj.entity.ExamScoreInfoEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Dao
 *
 * @author lipengjun
 * @date 2020-03-24 09:58:44
 */
@Mapper
public interface EvaluateInfoDao extends BaseMapper<EvaluateInfoEntity> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<EvaluateInfoEntity> queryAll(@Param("params") Map<String, Object> params);

    /**
     * 自定义分页查询
     *
     * @param page   分页参数
     * @param params 查询参数
     * @return List
     */
    List<EvaluateInfoEntity> selectEvaluateInfoPage(IPage page, @Param("params") Map<String, Object> params);

    /**
     * 删除
     * @param params
     */
    void deleteByStaffId(@Param("params") Map<String, Object> params);

    /**
     * 批量插入得分数据
     * @param list
     */
    void insertBatch(@Param("list") List<EvaluateInfoEntity> list);
}
