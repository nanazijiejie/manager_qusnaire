/*
 * 类名称:TypeDefServiceImpl.java
 * 包名称:com.ktkj.service.impl
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-08-24 14:29:31        lipengjun     初版做成
 *
 * Copyright (c) 2019-2019 厦门继续想科技有限公司
 */
package com.ktkj.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ktkj.utils.QueryPlus;
import com.ktkj.dao.TypeDefDao;
import com.ktkj.entity.TypeDefEntity;
import com.ktkj.service.TypeDefService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 信息分类Service实现类
 *
 * @author lipengjun
 * @date 2019-08-24 14:29:31
 */
@Service("typeDefService")
public class TypeDefServiceImpl extends ServiceImpl<TypeDefDao, TypeDefEntity> implements TypeDefService {

    @Override
    public List<TypeDefEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

    @Override
    public Page queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "T.type_id");
        params.put("asc", false);
        Page<TypeDefEntity> page = new QueryPlus<TypeDefEntity>(params).getPage();
        return page.setRecords(baseMapper.selectTypeDefPage(page, params));
    }

    @Override
    public boolean add(TypeDefEntity typeDef) {
        return this.save(typeDef);
    }

    @Override
    public boolean update(TypeDefEntity typeDef) {
        return this.updateById(typeDef);
    }

    @Override
    public boolean delete(Integer typeId) {
        return this.removeById(typeId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBatch(Integer[] typeIds) {
        return this.removeByIds(Arrays.asList(typeIds));
    }
}
