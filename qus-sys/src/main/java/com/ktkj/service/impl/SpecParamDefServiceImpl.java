/*
 * 类名称:SpecParamDefServiceImpl.java
 * 包名称:com.ktkj.service.impl
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-08-27 16:57:03        lipengjun     初版做成
 *
 * Copyright (c) 2019-2019 厦门继续想科技有限公司
 */
package com.ktkj.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ktkj.utils.QueryPlus;
import com.ktkj.dao.SpecParamDefDao;
import com.ktkj.entity.SpecParamDefEntity;
import com.ktkj.service.SpecParamDefService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Service实现类
 *
 * @author lipengjun
 * @date 2019-08-27 16:57:03
 */
@Service("specParamDefService")
public class SpecParamDefServiceImpl extends ServiceImpl<SpecParamDefDao, SpecParamDefEntity> implements SpecParamDefService {

    @Override
    public List<SpecParamDefEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

    @Override
    public Page queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "T.spec_seq");
        params.put("asc", false);
        Page<SpecParamDefEntity> page = new QueryPlus<SpecParamDefEntity>(params).getPage();
        return page.setRecords(baseMapper.selectSpecParamDefPage(page, params));
    }

    @Override
    public boolean add(SpecParamDefEntity specParamDef) {
        return this.save(specParamDef);
    }

    @Override
    public boolean update(SpecParamDefEntity specParamDef) {
        return this.updateById(specParamDef);
    }

    @Override
    public boolean delete(Integer specId) {
        return this.removeById(specId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBatch(Integer[] specIds) {
        return this.removeByIds(Arrays.asList(specIds));
    }
}
