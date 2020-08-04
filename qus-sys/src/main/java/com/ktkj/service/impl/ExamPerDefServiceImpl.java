/*
 * 类名称:ExamPerDefServiceImpl.java
 * 包名称:com.ktkj.service.impl
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-10-09 15:21:53        lipengjun     初版做成
 *
 * Copyright (c) 2019-2019 厦门继续想科技有限公司
 */
package com.ktkj.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ktkj.utils.QueryPlus;
import com.ktkj.dao.ExamPerDefDao;
import com.ktkj.entity.ExamPerDefEntity;
import com.ktkj.service.ExamPerDefService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Service实现类
 *
 * @author lipengjun
 * @date 2019-10-09 15:21:53
 */
@Service("examPerDefService")
public class ExamPerDefServiceImpl extends ServiceImpl<ExamPerDefDao, ExamPerDefEntity> implements ExamPerDefService {

    @Override
    public List<ExamPerDefEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

    @Override
    public Page queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "T.percent_id");
        params.put("asc", false);
        Page<ExamPerDefEntity> page = new QueryPlus<ExamPerDefEntity>(params).getPage();
        return page.setRecords(baseMapper.selectExamPerDefPage(page, params));
    }

    @Override
    public boolean add(ExamPerDefEntity examPerDef) {
        return this.save(examPerDef);
    }

    @Override
    public boolean update(ExamPerDefEntity examPerDef) {
        return this.updateById(examPerDef);
    }

    @Override
    public boolean delete(Integer percentId) {
        return this.removeById(percentId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBatch(Integer[] percentIds) {
        return this.removeByIds(Arrays.asList(percentIds));
    }
}
