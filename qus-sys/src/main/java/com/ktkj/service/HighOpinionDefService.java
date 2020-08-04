/*
 * 类名称:HighOpinionDefService.java
 * 包名称:com.ktkj.service
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-08-27 16:57:02        lipengjun     初版做成
 *
 * Copyright (c) 2019-2019 厦门继续想科技有限公司
 */
package com.ktkj.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ktkj.entity.HighOpinionDefEntity;

import java.util.List;
import java.util.Map;

/**
 * Service接口
 *
 * @author lipengjun
 * @date 2019-08-27 16:57:02
 */
public interface HighOpinionDefService extends IService<HighOpinionDefEntity> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<HighOpinionDefEntity> queryAll(Map<String, Object> params);

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
     * @param highOpinionDef 
     * @return 新增结果
     */
    boolean add(HighOpinionDefEntity highOpinionDef);

    /**
     * 根据主键更新
     *
     * @param highOpinionDef 
     * @return 更新结果
     */
    boolean update(HighOpinionDefEntity highOpinionDef);

    /**
     * 根据主键删除
     *
     * @param opinionId opinionId
     * @return 删除结果
     */
    boolean delete(Integer opinionId);

    /**
     * 根据主键批量删除
     *
     * @param opinionIds opinionIds
     * @return 删除结果
     */
    boolean deleteBatch(Integer[] opinionIds);
}
