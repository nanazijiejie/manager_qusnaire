/*
 * 类名称:ExamScoreInfoDao.java
 * 包名称:com.ktkj.dao
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-10-09 15:21:52        lipengjun     初版做成
 *
 * Copyright (c) 2019-2019 厦门继续想科技有限公司
 */
package com.ktkj.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ktkj.entity.ExamScoreInfoEntity;
import com.ktkj.entity.FinalExamScoreInfoEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Dao
 *
 * @author lipengjun
 * @date 2019-10-09 15:21:52
 */
@Mapper
public interface ExamScoreInfoDao extends BaseMapper<ExamScoreInfoEntity> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<ExamScoreInfoEntity> queryAll(@Param("params") Map<String, Object> params);

    List<ExamScoreInfoEntity> queryDeptAll(@Param("params") Map<String, Object> params);

    /**
     * 自定义分页查询
     *
     * @param page   分页参数
     * @param params 查询参数
     * @return List
     */
    List<ExamScoreInfoEntity> selectExamScoreInfoPage(IPage page, @Param("params") Map<String, Object> params);

    /**
     * 批量插入得分数据
     * @param list
     */
    void insertBatch(@Param("list")List<ExamScoreInfoEntity>list);

    /**
     * 删除
     * @param qusStaffId
     */
    void deleteByQusStaffId(@Param("qusStaffId") Integer qusStaffId);

    List<ExamScoreInfoEntity> queryDeptScore(@Param("examStation") String examStation);

    /**
     * 计算最终考核结果
     * @return
     */
    List<FinalExamScoreInfoEntity>queryFinalScore();

}
