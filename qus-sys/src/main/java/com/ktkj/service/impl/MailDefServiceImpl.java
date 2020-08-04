/*
 * 类名称:MailDefServiceImpl.java
 * 包名称:com.ktkj.service.impl
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-10-17 20:33:03        lipengjun     初版做成
 *
 * Copyright (c) 2019-2019 厦门继续想科技有限公司
 */
package com.ktkj.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ktkj.utils.QueryPlus;
import com.ktkj.dao.MailDefDao;
import com.ktkj.entity.MailDefEntity;
import com.ktkj.service.MailDefService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Service实现类
 *
 * @author lipengjun
 * @date 2019-10-17 20:33:03
 */
@Service("mailDefService")
public class MailDefServiceImpl extends ServiceImpl<MailDefDao, MailDefEntity> implements MailDefService {

    @Override
    public List<MailDefEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

    @Override
    public Page queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "T.mail_id");
        params.put("asc", false);
        Page<MailDefEntity> page = new QueryPlus<MailDefEntity>(params).getPage();
        return page.setRecords(baseMapper.selectMailDefPage(page, params));
    }

    @Override
    public boolean add(MailDefEntity mailDef) {
        return this.save(mailDef);
    }

    @Override
    public boolean update(MailDefEntity mailDef) {
        return this.updateById(mailDef);
    }

    @Override
    public boolean delete(Integer mailId) {
        return this.removeById(mailId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBatch(Integer[] mailIds) {
        return this.removeByIds(Arrays.asList(mailIds));
    }
}
