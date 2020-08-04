/*
 * 类名称:TypeDefService.java
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
import com.ktkj.entity.TypeDefEntity;

import java.util.List;
import java.util.Map;

/**
 * 信息分类Service接口
 *
 * @author lipengjun
 * @date 2019-08-24 14:29:31
 */
public interface TypeDefService extends IService<TypeDefEntity> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<TypeDefEntity> queryAll(Map<String, Object> params);

    /**
     * 分页查询信息分类
     *
     * @param params 查询参数
     * @return Page
     */
    Page queryPage(Map<String, Object> params);

    /**
     * 新增信息分类
     *
     * @param typeDef 信息分类
     * @return 新增结果
     */
    boolean add(TypeDefEntity typeDef);

    /**
     * 根据主键更新信息分类
     *
     * @param typeDef 信息分类
     * @return 更新结果
     */
    boolean update(TypeDefEntity typeDef);

    /**
     * 根据主键删除信息分类
     *
     * @param typeId typeId
     * @return 删除结果
     */
    boolean delete(Integer typeId);

    /**
     * 根据主键批量删除
     *
     * @param typeIds typeIds
     * @return 删除结果
     */
    boolean deleteBatch(Integer[] typeIds);
}
