/*
 * 类名称:QusItemRelService.java
 * 包名称:com.ktkj.service
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-10-09 15:21:52        lipengjun     初版做成
 *
 * Copyright (c) 2019-2019 厦门继续想科技有限公司
 */
package com.ktkj.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ktkj.entity.QusItemRelEntity;

import java.util.List;
import java.util.Map;

/**
 * Service接口
 *
 * @author lipengjun
 * @date 2019-10-09 15:21:52
 */
public interface QusItemRelService extends IService<QusItemRelEntity> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<QusItemRelEntity> queryAll(Map<String, Object> params);

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
     * @param qusItemRel 
     * @return 新增结果
     */
    boolean add(QusItemRelEntity qusItemRel);

    /**
     * 根据主键更新
     *
     * @param qusItemRel 
     * @return 更新结果
     */
    boolean update(QusItemRelEntity qusItemRel);

    /**
     * 根据主键删除
     *
     * @param qusNaireId qusNaireId
     * @return 删除结果
     */
    boolean delete(Integer qusNaireId);

    /**
     * 根据主键批量删除
     *
     * @param qusNaireIds qusNaireIds
     * @return 删除结果
     */
    boolean deleteBatch(Integer[] qusNaireIds);

    /**
     * 批量插入
     * @param list
     */
    public void addList(List<QusItemRelEntity> list);

    /**
     * 查询问卷对应的考核项
     * @param qusNaireId
     * @return
     */
    List<QusItemRelEntity> selectByQusNaireId( Integer qusNaireId);
}
