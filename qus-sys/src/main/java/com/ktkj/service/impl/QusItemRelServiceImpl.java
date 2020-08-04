/*
 * 类名称:QusItemRelServiceImpl.java
 * 包名称:com.ktkj.service.impl
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-10-09 15:21:52        lipengjun     初版做成
 *
 * Copyright (c) 2019-2019 厦门继续想科技有限公司
 */
package com.ktkj.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ktkj.utils.QueryPlus;
import com.ktkj.dao.QusItemRelDao;
import com.ktkj.entity.QusItemRelEntity;
import com.ktkj.service.QusItemRelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Service实现类
 *
 * @author lipengjun
 * @date 2019-10-09 15:21:52
 */
@Service("qusItemRelService")
public class QusItemRelServiceImpl extends ServiceImpl<QusItemRelDao, QusItemRelEntity> implements QusItemRelService {

    @Override
    public List<QusItemRelEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

    @Override
    public Page queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "T.qus_naire_id");
        params.put("asc", false);
        Page<QusItemRelEntity> page = new QueryPlus<QusItemRelEntity>(params).getPage();
        return page.setRecords(baseMapper.selectQusItemRelPage(page, params));
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addList(List<QusItemRelEntity> list){
        if(list!=null&&list.size()>0){
            baseMapper.deleteByQusNaireId(list.get(0).getQusNaireId());
        }
        baseMapper.insertBatch(list);

    }

    @Override
    public boolean add(QusItemRelEntity qusItemRel) {
        return this.save(qusItemRel);
    }

    @Override
    public boolean update(QusItemRelEntity qusItemRel) {
        return this.updateById(qusItemRel);
    }

    @Override
    public boolean delete(Integer qusNaireId) {
        return this.removeById(qusNaireId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBatch(Integer[] qusNaireIds) {
        return this.removeByIds(Arrays.asList(qusNaireIds));
    }
    @Override
    public List<QusItemRelEntity> selectByQusNaireId( Integer qusNaireId){
        return baseMapper.selectByQusNaireId(qusNaireId);
    }
}
