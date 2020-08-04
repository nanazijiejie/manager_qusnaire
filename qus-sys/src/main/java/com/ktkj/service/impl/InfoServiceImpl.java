/*
 * 类名称:InfoServiceImpl.java
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
import com.ktkj.dao.InfoDao;
import com.ktkj.entity.InfoEntity;
import com.ktkj.service.InfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Service实现类
 *
 * @author lipengjun
 * @date 2019-08-24 14:29:31
 */
@Service("infoService")
public class InfoServiceImpl extends ServiceImpl<InfoDao, InfoEntity> implements InfoService {

    @Override
    public List<InfoEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

    @Override
    public Page queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "T.info_id");
        params.put("asc", false);
        Page<InfoEntity> page = new QueryPlus<InfoEntity>(params).getPage();
        return page.setRecords(baseMapper.selectInfoPage(page, params));
    }

    @Override
    public boolean add(InfoEntity info) {
        return this.save(info);
    }

    @Override
    public boolean update(InfoEntity info) {
        return this.updateById(info);
    }

    @Override
    public boolean delete(Integer infoId) {
        return this.removeById(infoId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBatch(Integer[] infoIds) {
        return this.removeByIds(Arrays.asList(infoIds));
    }
}
