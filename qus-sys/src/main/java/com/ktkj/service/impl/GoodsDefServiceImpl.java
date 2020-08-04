/*
 * 类名称:GoodsDefServiceImpl.java
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
import com.ktkj.dao.GoodsDefDao;
import com.ktkj.entity.GoodsDefEntity;
import com.ktkj.service.GoodsDefService;
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
@Service("goodsDefService")
public class GoodsDefServiceImpl extends ServiceImpl<GoodsDefDao, GoodsDefEntity> implements GoodsDefService {

    @Override
    public List<GoodsDefEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

    @Override
    public Page queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "T.goods_id");
        params.put("asc", false);
        Page<GoodsDefEntity> page = new QueryPlus<GoodsDefEntity>(params).getPage();
        return page.setRecords(baseMapper.selectGoodsDefPage(page, params));
    }

    @Override
    public boolean add(GoodsDefEntity goodsDef) {
        return this.save(goodsDef);
    }

    @Override
    public boolean update(GoodsDefEntity goodsDef) {
        return this.updateById(goodsDef);
    }

    @Override
    public boolean delete(Integer goodsId) {
        return this.removeById(goodsId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBatch(Integer[] goodsIds) {
        return this.removeByIds(Arrays.asList(goodsIds));
    }
}
