/*
 * 类名称:ExamItemDefServiceImpl.java
 * 包名称:com.ktkj.service.impl
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-10-09 15:21:52        lipengjun     初版做成
 *
 * Copyright (c) 2019-2019 厦门继续想科技有限公司
 */
package com.ktkj.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ktkj.dao.ExamIndexRelDao;
import com.ktkj.entity.ExamIndexRelEntity;
import com.ktkj.utils.QueryPlus;
import com.ktkj.dao.ExamItemDefDao;
import com.ktkj.entity.ExamItemDefEntity;
import com.ktkj.service.ExamItemDefService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Service实现类
 *
 * @author lipengjun
 * @date 2019-10-09 15:21:52
 */
@Service("examItemDefService")
public class ExamItemDefServiceImpl extends ServiceImpl<ExamItemDefDao, ExamItemDefEntity> implements ExamItemDefService {

    @Autowired
    ExamIndexRelDao examIndexRelDao;
    @Override
    public List<ExamItemDefEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

    @Override
    public Page queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "T.exam_item_id");
        params.put("asc", false);
        Page<ExamItemDefEntity> page = new QueryPlus<ExamItemDefEntity>(params).getPage();
        return page.setRecords(baseMapper.selectExamItemDefPage(page, params));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(ExamItemDefEntity examItemDef,Integer[] rels) {
        //Integer[] rels = examItemDef.getExamIndexRels();
//        for (Integer str:rels){
//            ExamIndexRelEntity entity = new ExamIndexRelEntity();
//            entity.setCreateOperator(examItemDef.getCreateOperator());
//            entity.setCreateTime(new Date());
//            entity.setExamItemId(examItemDef.getExamItemId());
//            entity.setIndexItemId(str);
//            //entity.setPercent();
//            examIndexRelDao.insert(entity);
//        }

        return this.save(examItemDef);
    }

    @Override
    public boolean update(ExamItemDefEntity examItemDef) {
        return this.updateById(examItemDef);
    }

    @Override
    public boolean delete(Integer examItemId) {
        return this.removeById(examItemId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBatch(Integer[] examItemIds) {
        for (Integer examItemId:examItemIds
             ) {
            examIndexRelDao.deleteByExamId(examItemId);
        }
        return this.removeByIds(Arrays.asList(examItemIds));
    }
    @Override
    public List<ExamItemDefEntity> qryExamItem(Map<String, Object> params){
        return baseMapper.qryExamItem(params);
    }
}
