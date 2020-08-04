/*
 * 类名称:SelectionInfoService.java
 * 包名称:com.ktkj.service
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2020-02-21 22:35:28        lipengjun     初版做成
 *
 * Copyright (c) 2019-2019 厦门继续想科技有限公司
 */
package com.ktkj.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ktkj.entity.FinalSelRetEntity;
import com.ktkj.entity.SelRetEntity;
import com.ktkj.entity.SelectionDefEntity;

import java.util.List;
import java.util.Map;

/**
 * Service接口
 *
 * @author lipengjun
 * @date 2020-02-21 22:35:28
 */
public interface SelectionDefService extends IService<SelectionDefEntity> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<SelectionDefEntity> queryAll(Map<String, Object> params);

    /**
     * 分页查询
     *
     * @param params 查询参数
     * @return Page
     */
    Page queryPage(Map<String, Object> params);

    /**
     * 获取归属单位/地市
     *
     * @return Page
     */
    List<SelectionDefEntity> querySelCity();


    /**
     * 新增
     *
     * @param selectionDefEntity
     * @return 新增结果
     */
    boolean add(SelectionDefEntity selectionDefEntity);

    /**
     * 根据主键更新
     *
     * @param selectionDefEntity
     * @return 更新结果
     */
    boolean update(SelectionDefEntity selectionDefEntity);

    /**
     * 根据主键删除
     *
     * @param selId selId
     * @return 删除结果
     */
    boolean delete(Integer selId);

    /**
     * 根据主键批量删除
     *
     * @param selIds selIds
     * @return 删除结果
     */
    boolean deleteBatch(Integer[] selIds);
}
