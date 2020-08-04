/*
 * 类名称:HighOpinionDefServiceImpl.java
 * 包名称:com.ktkj.service.impl
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-08-27 16:57:02        lipengjun     初版做成
 *
 * Copyright (c) 2019-2019 厦门继续想科技有限公司
 */
package com.ktkj.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ktkj.utils.QueryPlus;
import com.ktkj.dao.HighOpinionDefDao;
import com.ktkj.entity.HighOpinionDefEntity;
import com.ktkj.service.HighOpinionDefService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Service实现类
 *
 * @author lipengjun
 * @date 2019-08-27 16:57:02
 */
@Service("highOpinionDefService")
public class HighOpinionDefServiceImpl extends ServiceImpl<HighOpinionDefDao, HighOpinionDefEntity> implements HighOpinionDefService {

    @Override
    public List<HighOpinionDefEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

    @Override
    public Page queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "T.opinion_id");
        params.put("asc", false);
        Page<HighOpinionDefEntity> page = new QueryPlus<HighOpinionDefEntity>(params).getPage();
        return page.setRecords(baseMapper.selectHighOpinionDefPage(page, params));
    }

    @Override
    public boolean add(HighOpinionDefEntity highOpinionDef) {
        return this.save(highOpinionDef);
    }

    @Override
    public boolean update(HighOpinionDefEntity highOpinionDef) {
        return this.updateById(highOpinionDef);
    }

    @Override
    public boolean delete(Integer opinionId) {
        return this.removeById(opinionId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBatch(Integer[] opinionIds) {
        return this.removeByIds(Arrays.asList(opinionIds));
    }
}
