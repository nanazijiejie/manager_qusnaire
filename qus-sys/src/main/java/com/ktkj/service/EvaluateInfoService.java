/*
 * 类名称:EvaluateInfoService.java
 * 包名称:com.ktkj.service
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2020-03-24 09:58:44        lipengjun     初版做成
 *
 * Copyright (c) 2019-2019 厦门继续想科技有限公司
 */
package com.ktkj.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ktkj.entity.EvaluateInfoEntity;
import com.ktkj.entity.StaffInfoEntity;

import java.util.List;
import java.util.Map;

/**
 * Service接口
 *
 * @author lipengjun
 * @date 2020-03-24 09:58:44
 */
public interface EvaluateInfoService extends IService<EvaluateInfoEntity> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<EvaluateInfoEntity> queryAll(Map<String, Object> params);

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
     * @param evaluateInfo 
     * @return 新增结果
     */
    boolean add(EvaluateInfoEntity evaluateInfo);

    /**
     * 根据主键更新
     *
     * @param evaluateInfo 
     * @return 更新结果
     */
    boolean update(EvaluateInfoEntity evaluateInfo);

    /**
     * 根据主键删除
     *
     * @param evaluateId evaluateId
     * @return 删除结果
     */
    boolean delete(Integer evaluateId);

    /**
     * 根据主键批量删除
     *
     * @param evaluateIds evaluateIds
     * @return 删除结果
     */
    boolean deleteBatch(Integer[] evaluateIds);

    /**
     * 批量插入
     * @param list
     * @param staffInfoEntity
     */
    public void insertBatch(List<EvaluateInfoEntity> list, StaffInfoEntity staffInfoEntity);
}
