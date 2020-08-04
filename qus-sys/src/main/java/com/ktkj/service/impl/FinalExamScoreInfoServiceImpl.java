/*
 * 类名称:FinalExamScoreInfoServiceImpl.java
 * 包名称:com.ktkj.service.impl
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-10-24 11:58:43        lipengjun     初版做成
 *
 * Copyright (c) 2019-2019 厦门继续想科技有限公司
 */
package com.ktkj.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ktkj.utils.QueryPlus;
import com.ktkj.dao.FinalExamScoreInfoDao;
import com.ktkj.entity.FinalExamScoreInfoEntity;
import com.ktkj.service.FinalExamScoreInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Service实现类
 *
 * @author lipengjun
 * @date 2019-10-24 11:58:43
 */
@Service("finalExamScoreInfoService")
public class FinalExamScoreInfoServiceImpl extends ServiceImpl<FinalExamScoreInfoDao, FinalExamScoreInfoEntity> implements FinalExamScoreInfoService {

    @Override
    public List<FinalExamScoreInfoEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

    @Override
    public Page queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "T.exam_result_id");
        params.put("asc", false);
        Page<FinalExamScoreInfoEntity> page = new QueryPlus<FinalExamScoreInfoEntity>(params).getPage();
        return page.setRecords(baseMapper.selectFinalExamScoreInfoPage(page, params));
    }

    @Override
    public boolean add(FinalExamScoreInfoEntity finalExamScoreInfo) {
        return this.save(finalExamScoreInfo);
    }

    @Override
    public boolean update(FinalExamScoreInfoEntity finalExamScoreInfo) {
        return this.updateById(finalExamScoreInfo);
    }

    @Override
    public boolean delete(Integer examResultId) {
        return this.removeById(examResultId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBatch(Integer[] examResultIds) {
        return this.removeByIds(Arrays.asList(examResultIds));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertBatch(List<FinalExamScoreInfoEntity>list){
        //先删除
        baseMapper.deleteAll();
        baseMapper.insertBatch(list);
    }
}
