/*
 * 类名称:EvaluateInfoServiceImpl.java
 * 包名称:com.ktkj.service.impl
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2020-03-24 09:58:44        lipengjun     初版做成
 *
 * Copyright (c) 2019-2019 厦门继续想科技有限公司
 */
package com.ktkj.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ktkj.entity.StaffInfoEntity;
import com.ktkj.service.StaffInfoService;
import com.ktkj.utils.QueryPlus;
import com.ktkj.dao.EvaluateInfoDao;
import com.ktkj.entity.EvaluateInfoEntity;
import com.ktkj.service.EvaluateInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service实现类
 *
 * @author lipengjun
 * @date 2020-03-24 09:58:44
 */
@Service("evaluateInfoService")
public class EvaluateInfoServiceImpl extends ServiceImpl<EvaluateInfoDao, EvaluateInfoEntity> implements EvaluateInfoService {
    @Autowired
    private StaffInfoService staffInfoService;

    @Override
    public List<EvaluateInfoEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

    @Override
    public Page queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "T.evaluate_id");
        params.put("asc", false);
        Page<EvaluateInfoEntity> page = new QueryPlus<EvaluateInfoEntity>(params).getPage();
        return page.setRecords(baseMapper.selectEvaluateInfoPage(page, params));
    }

    @Override
    public boolean add(EvaluateInfoEntity evaluateInfo) {
        return this.save(evaluateInfo);
    }

    @Override
    public boolean update(EvaluateInfoEntity evaluateInfo) {
        return this.updateById(evaluateInfo);
    }

    @Override
    public boolean delete(Integer evaluateId) {
        return this.removeById(evaluateId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBatch(Integer[] evaluateIds) {
        return this.removeByIds(Arrays.asList(evaluateIds));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertBatch(List<EvaluateInfoEntity>list, StaffInfoEntity staffInfoEntity){
        //先删除之前的填写信息，重新再入库
        if(list!=null&&list.size()!=0){
            Map<String,Object> params = new HashMap<String,Object>();
            params.put("staffId",list.get(0).getStaffId());
            params.put("evaluateType",list.get(0).getEvaluateType());
            baseMapper.deleteByStaffId(params);
            baseMapper.insertBatch(list);
            if("0".equals(list.get(0).getEvaluateType())){
                staffInfoEntity.setIsResponseSubmit("1");
            }else{
                staffInfoEntity.setIsClearSubmit("1");
            }
            staffInfoService.updateById(staffInfoEntity);
        }
    }
}
