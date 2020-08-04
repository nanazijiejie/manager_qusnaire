/*
 * 类名称:StaffInfoDao.java
 * 包名称:com.ktkj.dao
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-10-01 16:23:16        lipengjun     初版做成
 *
 * Copyright (c) 2019-2019 厦门继续想科技有限公司
 */
package com.ktkj.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ktkj.entity.SelectionDefEntity;
import com.ktkj.entity.StaffInfoEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Dao
 *
 * @author lipengjun
 * @date 2019-10-01 16:23:16
 */
@Mapper
public interface StaffInfoDao extends BaseMapper<StaffInfoEntity> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<StaffInfoEntity> queryAll(@Param("params") Map<String, Object> params);

    /**
     * 自定义分页查询
     *
     * @param page   分页参数
     * @param params 查询参数
     * @return List
     */
    List<StaffInfoEntity> selectStaffInfoPage(IPage page, @Param("params") Map<String, Object> params);

    List<StaffInfoEntity> qryExamStaff(@Param("params") Map<String, Object> params);

    List<StaffInfoEntity> qrySelStaff(@Param("params") Map<String, Object> params);

    List<SelectionDefEntity> qrySelDef(@Param("params") Map<String, Object> params);

    List<StaffInfoEntity>  qryStaffInfoByMobiles(@Param("list")List<String>mobile);

    List<StaffInfoEntity>  qryStaffInfoByEmails(@Param("list")List<String>emails);

    void insertBatch(@Param("list")List<StaffInfoEntity>mobile);

    int saveKeys(StaffInfoEntity staffInfoEntity);

    int updateBatch(Map<String, Object> map);
    int updateIsSelectionBatch(Map<String, Object> map);

    int updateBatchByEmail(Map<String, Object> map);
    int updateIsSelectionBatchByEmail(Map<String, Object> map);

    void updateDeptNull(@Param("params") Map<String, Object> params);



}
