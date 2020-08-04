/*
 * 类名称:ExamScoreInfoServiceImpl.java
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
import com.ktkj.entity.FinalExamScoreInfoEntity;
import com.ktkj.entity.StaffInfoEntity;
import com.ktkj.service.StaffInfoService;
import com.ktkj.utils.QueryPlus;
import com.ktkj.dao.ExamScoreInfoDao;
import com.ktkj.entity.ExamScoreInfoEntity;
import com.ktkj.service.ExamScoreInfoService;
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
@Service("examScoreInfoService")
public class ExamScoreInfoServiceImpl extends ServiceImpl<ExamScoreInfoDao, ExamScoreInfoEntity> implements ExamScoreInfoService {
    @Autowired
    StaffInfoService staffInfoService;
    private final static int batchCount = 200;
    @Override
    public List<ExamScoreInfoEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }
    @Override
    public List<ExamScoreInfoEntity> queryDeptAll(Map<String, Object> params) {
        return baseMapper.queryDeptAll(params);
    }

    @Override
    public Page queryPage(Map<String, Object> params) {
        //排序
        //params.put("sidx", "T.exam_result_id");
        //params.put("asc", false);
        Page<ExamScoreInfoEntity> page = new QueryPlus<ExamScoreInfoEntity>(params).getPage();
        return page.setRecords(baseMapper.selectExamScoreInfoPage(page, params));
    }

    @Override
    public boolean add(ExamScoreInfoEntity examScoreInfo) {
        return this.save(examScoreInfo);
    }

    @Override
    public boolean update(ExamScoreInfoEntity examScoreInfo) {
        return this.updateById(examScoreInfo);
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
    public void insertBatch(List<ExamScoreInfoEntity>list, StaffInfoEntity staffInfoEntity){
        //先删除之前的填写信息，重新再入库
        if(list!=null&&list.size()!=0){
            baseMapper.deleteByQusStaffId(list.get(0).getQusNaireStaffId());
            int partCount0 = list.size()/batchCount +1;
            for(int i=0;i<partCount0;i++){
                List<ExamScoreInfoEntity> finalPartList = null;
                if(i==partCount0-1){
                    finalPartList = list.subList(i*batchCount,list.size());
                }else{
                    finalPartList = list.subList(i*batchCount,(i+1)*batchCount);
                }
                if(finalPartList!=null&&finalPartList.size()!=0){
                    baseMapper.insertBatch(list);
                }
            }
            if("1".equals(staffInfoEntity.getIsSubmit())){
                staffInfoService.updateById(staffInfoEntity);
            }
        }
    }
    @Override
    public List<FinalExamScoreInfoEntity>queryFinalScore(){
        return baseMapper.queryFinalScore();
    }

    @Override
    public List<ExamScoreInfoEntity> queryDeptScore(String examStation) {
        return baseMapper.queryDeptScore(examStation);
    }
}
