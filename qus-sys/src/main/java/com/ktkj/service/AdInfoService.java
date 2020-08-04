/*
 * 类名称:AdInfoService.java
 * 包名称:com.ktkj.service
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-08-24 14:29:31        lipengjun     初版做成
 *
 * Copyright (c) 2019-2019 厦门继续想科技有限公司
 */
package com.ktkj.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ktkj.entity.AdInfoEntity;

import java.util.List;
import java.util.Map;

/**
 * 广告信息表Service接口
 *
 * @author lipengjun
 * @date 2019-08-24 14:29:31
 */
public interface AdInfoService extends IService<AdInfoEntity> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<AdInfoEntity> queryAll(Map<String, Object> params);

    /**
     * 分页查询广告信息表
     *
     * @param params 查询参数
     * @return Page
     */
    Page queryPage(Map<String, Object> params);

    /**
     * 新增广告信息表
     *
     * @param adInfo 广告信息表
     * @return 新增结果
     */
    boolean add(AdInfoEntity adInfo);

    /**
     * 根据主键更新广告信息表
     *
     * @param adInfo 广告信息表
     * @return 更新结果
     */
    boolean update(AdInfoEntity adInfo);

    /**
     * 根据主键删除广告信息表
     *
     * @param addId addId
     * @return 删除结果
     */
    boolean delete(Integer addId);

    /**
     * 根据主键批量删除
     *
     * @param addIds addIds
     * @return 删除结果
     */
    boolean deleteBatch(Integer[] addIds);
}
