/*
 * 类名称:SelectionInfoServiceImpl.java
 * 包名称:com.ktkj.service.impl
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2020-02-21 22:35:28        lipengjun     初版做成
 *
 * Copyright (c) 2019-2019 厦门继续想科技有限公司
 */
package com.ktkj.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ktkj.dao.SelectionDefDao;
import com.ktkj.entity.SelRetEntity;
import com.ktkj.entity.SelectionDefEntity;
import com.ktkj.service.SelectionDefService;
import com.ktkj.utils.QueryPlus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Service实现类
 *
 * @author lipengjun
 * @date 2020-02-21 22:35:28
 */
@Service("selectionDefService")
public class SelectionDefServiceImpl extends ServiceImpl<SelectionDefDao, SelectionDefEntity> implements SelectionDefService {

    @Override
    public List<SelectionDefEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

    @Override
    public Page queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "T.sel_def_id");
        params.put("asc", false);
        Page<SelectionDefEntity> page = new QueryPlus<SelectionDefEntity>(params).getPage();
        return page.setRecords(baseMapper.selectSelectionDefPage(page, params));
    }

    @Override
    public List<SelectionDefEntity> querySelCity() {
        return baseMapper.querySelCity();
    }

    @Override
    public boolean add(SelectionDefEntity selectionDefEntity) {
        return this.save(selectionDefEntity);
    }

    @Override
    public boolean update(SelectionDefEntity selectionDefEntity) {
        return this.updateById(selectionDefEntity);
    }

    @Override
    public boolean delete(Integer selId) {
        return this.removeById(selId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBatch(Integer[] selIds) {
        return this.removeByIds(Arrays.asList(selIds));
    }

}
