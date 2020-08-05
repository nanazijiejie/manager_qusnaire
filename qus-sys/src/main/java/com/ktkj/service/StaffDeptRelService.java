/*
 * 类名称:StaffDeptRelService.java
 * 包名称:com.ktkj.service
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-10-14 15:00:32        lipengjun     初版做成
 *
 * Copyright (c) 2019-2019 厦门继续想科技有限公司
 */
package com.ktkj.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ktkj.entity.StaffDeptRelEntity;
import com.ktkj.entity.StaffInfoEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Service接口
 *
 * @author lipengjun
 * @date 2019-10-14 15:00:32
 */
public interface StaffDeptRelService extends IService<StaffDeptRelEntity> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<StaffDeptRelEntity> queryAll(Map<String, Object> params);

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
     * @param staffDeptRel 
     * @return 新增结果
     */
    boolean add(StaffDeptRelEntity staffDeptRel);

    /**
     * 根据主键更新
     *
     * @param staffDeptRel 
     * @return 更新结果
     */
    boolean update(StaffDeptRelEntity staffDeptRel);

    /**
     * 根据主键删除
     *
     * @param id id
     * @return 删除结果
     */
    boolean delete(Integer id);

    /**
     * 根据主键批量删除
     *
     * @param ids ids
     * @return 删除结果
     */
    boolean deleteBatch(Integer[] ids);

    void delByStaffId(Integer staffId);

    List<StaffInfoEntity> selectViceDeptByStaff(Integer staffId);

    List<StaffInfoEntity> selectViceManagerByDept(Integer staffId);
}
