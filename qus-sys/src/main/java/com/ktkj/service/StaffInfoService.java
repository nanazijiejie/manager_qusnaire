/*
 * 类名称:StaffInfoService.java
 * 包名称:com.ktkj.service
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-10-01 16:23:16        lipengjun     初版做成
 *
 * Copyright (c) 2019-2019 厦门继续想科技有限公司
 */
package com.ktkj.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ktkj.entity.SelectionDefEntity;
import com.ktkj.entity.StaffInfoEntity;
import com.ktkj.utils.R;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;
import java.util.Map;

/**
 * Service接口
 *
 * @author lipengjun
 * @date 2019-10-01 16:23:16
 */
public interface StaffInfoService extends IService<StaffInfoEntity> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<StaffInfoEntity> queryAll(Map<String, Object> params);

    /**
     * 分页查询
     *
     * @param params 查询参数
     * @return Page
     */
    Page queryPage(Map<String, Object> params);

    /**
     * 新增
     *
     * @param staffInfo 
     * @return 新增结果
     */
    void add(StaffInfoEntity staffInfo);

    /**
     * 根据主键更新
     *
     * @param staffInfo 
     * @return 更新结果
     */
    boolean update(StaffInfoEntity staffInfo);

    /**
     * 根据主键删除
     *
     * @param staffId staffId
     * @return 删除结果
     */
    boolean delete(Integer staffId);

    /**
     * 根据主键批量删除
     *
     * @param staffIds staffIds
     * @return 删除结果
     */
    boolean deleteBatch(Integer[] staffIds);

    /**
     * 批量修改
     * @param list
     * @return
     */
    public boolean updateBatch(List<StaffInfoEntity> list);

    /**
     * 批量上传
     * @param sheet
     * @param entity
     * @return
     */

    public R batchSave(Sheet sheet, StaffInfoEntity entity);

    /**
     * 批量选员工代表
     * @param sheet
     * @param entity
     * @return
     */

    public R batchSave2(Sheet sheet, StaffInfoEntity entity);

    /**
     * 选员工代表
     * @param staffIds
     * @param status
     */

    public void updateBatch(Integer[] staffIds, String status);

    /**
     * 查询用户需要评价的人员
     * @param params
     * @return
     */
    List<StaffInfoEntity> qryExamStaff(Map<String, Object> params);

    /**
     * 查询参与选举的人
     * @param params
     * @return
     */
    List<StaffInfoEntity> qrySelStaff(Map<String, Object> params);

    /**
     * 查询选举配置
     * @param params
     * @return
     */

    List<SelectionDefEntity> qrySelDef(Map<String, Object> params);

    /**
     * 查询需要被考核的人
     * @param qusStaffName
     * @return
     */
    public List<StaffInfoEntity> qryExamStaffName(String qusStaffName);
}
