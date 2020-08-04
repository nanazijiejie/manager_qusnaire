/*
 * 类名称:IndexItemDefService.java
 * 包名称:com.ktkj.service
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-10-09 15:21:53        lipengjun     初版做成
 *
 * Copyright (c) 2019-2019 厦门继续想科技有限公司
 */
package com.ktkj.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ktkj.entity.IndexItemDefAddEntity;
import com.ktkj.entity.IndexItemDefEntity;

import java.util.List;
import java.util.Map;

/**
 * Service接口
 *
 * @author lipengjun
 * @date 2019-10-09 15:21:53
 */
public interface IndexItemDefService extends IService<IndexItemDefEntity> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<IndexItemDefEntity> queryAll(Map<String, Object> params);

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
     * @param indexItemDef 
     * @return 新增结果
     */
    boolean add(IndexItemDefEntity indexItemDef);

    /**
     * 根据主键更新
     *
     * @param indexItemDef 
     * @return 更新结果
     */
    boolean update(IndexItemDefEntity indexItemDef);

    /**
     * 根据主键删除
     *
     * @param indexItemId indexItemId
     * @return 删除结果
     */
    boolean delete(Integer indexItemId);

    /**
     * 根据主键批量删除
     *
     * @param indexItemIds indexItemIds
     * @return 删除结果
     */
    boolean deleteBatch(Integer[] indexItemIds);

    /**
     * 查询考核项对应的指标项
     * @param params
     * @return
     */
    List<IndexItemDefAddEntity> queryExamIndex(Map<String, Object> params);
}
