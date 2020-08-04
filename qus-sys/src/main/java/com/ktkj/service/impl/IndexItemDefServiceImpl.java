/*
 * 类名称:IndexItemDefServiceImpl.java
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
import com.ktkj.entity.IndexItemDefAddEntity;
import com.ktkj.utils.QueryPlus;
import com.ktkj.dao.IndexItemDefDao;
import com.ktkj.entity.IndexItemDefEntity;
import com.ktkj.service.IndexItemDefService;
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
@Service("indexItemDefService")
public class IndexItemDefServiceImpl extends ServiceImpl<IndexItemDefDao, IndexItemDefEntity> implements IndexItemDefService {

    @Override
    public List<IndexItemDefEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

    @Override
    public Page queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "T.index_item_id");
        params.put("asc", false);
        Page<IndexItemDefEntity> page = new QueryPlus<IndexItemDefEntity>(params).getPage();
        return page.setRecords(baseMapper.selectIndexItemDefPage(page, params));
    }

    @Override
    public boolean add(IndexItemDefEntity indexItemDef) {
        return this.save(indexItemDef);
    }

    @Override
    public boolean update(IndexItemDefEntity indexItemDef) {
        return this.updateById(indexItemDef);
    }

    @Override
    public boolean delete(Integer indexItemId) {
        return this.removeById(indexItemId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBatch(Integer[] indexItemIds) {
        return this.removeByIds(Arrays.asList(indexItemIds));
    }

    @Override
    public List<IndexItemDefAddEntity> queryExamIndex(Map<String, Object> params){
        return baseMapper.queryExamIndex(params);
    }

}
