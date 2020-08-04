/*
 * 类名称:QuestionnaireDefServiceImpl.java
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
import com.ktkj.dao.QusItemRelDao;
import com.ktkj.utils.QueryPlus;
import com.ktkj.dao.QuestionnaireDefDao;
import com.ktkj.entity.QuestionnaireDefEntity;
import com.ktkj.service.QuestionnaireDefService;
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
@Service("questionnaireDefService")
public class QuestionnaireDefServiceImpl extends ServiceImpl<QuestionnaireDefDao, QuestionnaireDefEntity> implements QuestionnaireDefService {

    @Override
    public List<QuestionnaireDefEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }
    @Autowired
    private QusItemRelDao qusItemRelDao;
    @Override
    public Page queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "T.qus_naire_id");
        params.put("asc", false);
        Page<QuestionnaireDefEntity> page = new QueryPlus<QuestionnaireDefEntity>(params).getPage();
        return page.setRecords(baseMapper.selectQuestionnaireDefPage(page, params));
    }

    @Override
    public boolean add(QuestionnaireDefEntity questionnaireDef) {
        return this.save(questionnaireDef);
    }

    @Override
    public boolean update(QuestionnaireDefEntity questionnaireDef) {
        return this.updateById(questionnaireDef);
    }

    @Override
    public boolean delete(Integer qusNaireId) {
        return this.removeById(qusNaireId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBatch(Integer[] qusNaireIds) {
        for (Integer qusNaireId:qusNaireIds
        ) {
            qusItemRelDao.deleteByQusNaireId(qusNaireId);
        }
        return this.removeByIds(Arrays.asList(qusNaireIds));
    }
}
