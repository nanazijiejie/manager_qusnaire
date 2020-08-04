/*
 * 类名称:AdInfoServiceImpl.java
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
import com.ktkj.dao.AdInfoDao;
import com.ktkj.entity.AdInfoEntity;
import com.ktkj.service.AdInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 广告信息表Service实现类
 *
 * @author lipengjun
 * @date 2019-08-24 14:29:31
 */
@Service("adInfoService")
public class AdInfoServiceImpl extends ServiceImpl<AdInfoDao, AdInfoEntity> implements AdInfoService {

    @Override
    public List<AdInfoEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

    @Override
    public Page queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "T.add_id");
        params.put("asc", false);
        Page<AdInfoEntity> page = new QueryPlus<AdInfoEntity>(params).getPage();
        return page.setRecords(baseMapper.selectAdInfoPage(page, params));
    }

    @Override
    public boolean add(AdInfoEntity adInfo) {
        return this.save(adInfo);
    }

    @Override
    public boolean update(AdInfoEntity adInfo) {
        return this.updateById(adInfo);
    }

    @Override
    public boolean delete(Integer addId) {
        return this.removeById(addId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBatch(Integer[] addIds) {
        return this.removeByIds(Arrays.asList(addIds));
    }
}
