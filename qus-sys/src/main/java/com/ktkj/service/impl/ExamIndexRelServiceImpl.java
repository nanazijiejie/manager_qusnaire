/*
 * 类名称:ExamIndexRelServiceImpl.java
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
import com.ktkj.utils.QueryPlus;
import com.ktkj.dao.ExamIndexRelDao;
import com.ktkj.entity.ExamIndexRelEntity;
import com.ktkj.service.ExamIndexRelService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Service实现类
 *
 * @author lipengjun
 * @date 2019-10-09 15:21:52
 */
@Service("examIndexRelService")
public class ExamIndexRelServiceImpl extends ServiceImpl<ExamIndexRelDao, ExamIndexRelEntity> implements ExamIndexRelService {

    @Override
    public List<ExamIndexRelEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

    @Override
    public Page queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "T.exam_item_id");
        params.put("asc", false);
        Page<ExamIndexRelEntity> page = new QueryPlus<ExamIndexRelEntity>(params).getPage();
        return page.setRecords(baseMapper.selectExamIndexRelPage(page, params));
    }

    @Override
    public boolean add(ExamIndexRelEntity examIndexRel) {
        return this.save(examIndexRel);
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addList(List<ExamIndexRelEntity> list){
        if(list!=null&&list.size()>0){
            baseMapper.deleteByExamId(list.get(0).getExamItemId());
        }
        baseMapper.insertBatch(list);

    }

    @Override
    public boolean update(ExamIndexRelEntity examIndexRel) {
        return this.updateById(examIndexRel);
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
            baseMapper.deleteByExamId(examItemId);
        }
        return this.removeByIds(Arrays.asList(examItemIds));
    }
}
