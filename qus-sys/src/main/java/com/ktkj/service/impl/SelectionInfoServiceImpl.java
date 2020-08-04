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
import com.ktkj.entity.FinalSelRetEntity;
import com.ktkj.entity.SelRetEntity;
import com.ktkj.utils.QueryPlus;
import com.ktkj.dao.SelectionInfoDao;
import com.ktkj.entity.SelectionInfoEntity;
import com.ktkj.service.SelectionInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * Service实现类
 *
 * @author lipengjun
 * @date 2020-02-21 22:35:28
 */
@Service("selectionInfoService")
public class SelectionInfoServiceImpl extends ServiceImpl<SelectionInfoDao, SelectionInfoEntity> implements SelectionInfoService {

    @Override
    public List<SelectionInfoEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

    @Override
    public Page queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "T.sel_id");
        params.put("asc", false);
        Page<SelectionInfoEntity> page = new QueryPlus<SelectionInfoEntity>(params).getPage();
        return page.setRecords(baseMapper.selectSelectionInfoPage(page, params));
    }

    @Override
    public boolean add(SelectionInfoEntity selectionInfo) {
        return this.save(selectionInfo);
    }

    @Override
    public boolean update(SelectionInfoEntity selectionInfo) {
        return this.updateById(selectionInfo);
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

    @Override
    public List<SelRetEntity> querySelRet() {
        return baseMapper.querySelRet();
    }

    @Override
    public Integer selCountByStation(Map<String, Object> params){
        return baseMapper.selCountByStation(params);
    }

    /**
     * 获取某个地市下某个职位投票的人总数
     * @param cityId
     * @param selSttaionId
     * @param selCountList
     * @return
     */
    private Integer getSelTotalCount(String cityId,String selSttaionId,List<SelRetEntity> selCountList){
        for (SelRetEntity selRetEntity:selCountList
             ) {
            if(cityId.equals(selRetEntity.getCityId())&&selSttaionId.equals(selRetEntity.getSelStationId())){
                return selRetEntity.getTotalCount();
            }
        }
        return 0;
    }


    /**
     * 计算得票结果
     * @return
     */
    @Override
    public List<FinalSelRetEntity> calSel() throws Exception{
        List<SelRetEntity> selCountList = baseMapper.querySelCount();
        List<SelRetEntity> retList = baseMapper.querySelRet();
        if(retList.size()<1){
            throw new Exception("投票结果为空！");
        }
        String tmpStaffId = "";
        List<FinalSelRetEntity> finalList = new ArrayList<FinalSelRetEntity>();
        Map<String,Integer> qusInfo = new HashMap<String,Integer>();
        FinalSelRetEntity finalSelRetEntity = new FinalSelRetEntity();
        Integer totalGet = 0;
        Integer totalCount = 0;
        SelRetEntity selRetEntityTmp = null;
        for (SelRetEntity selRetEntity : retList
        ) {
            if (!"".equals(tmpStaffId) && !tmpStaffId.equals(selRetEntity.getStaffId())) {//不是同一个人的信息
                finalSelRetEntity.setCity(selRetEntityTmp.getCity());
                finalSelRetEntity.setCityId(selRetEntityTmp.getCityId());
                finalSelRetEntity.setDept(selRetEntityTmp.getDept());
                double percentGet = new BigDecimal((float)totalGet/totalCount).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                finalSelRetEntity.setPercentGet(percentGet);
                finalSelRetEntity.setQusInfo(qusInfo);
                finalSelRetEntity.setSelStation(selRetEntityTmp.getSelStation());
                finalSelRetEntity.setSelStationId(selRetEntityTmp.getSelStationId());
                finalSelRetEntity.setStaffId(selRetEntityTmp.getStaffId());
                finalSelRetEntity.setStaffName(selRetEntityTmp.getStaffName());
                finalSelRetEntity.setStation(selRetEntityTmp.getStation());
                finalSelRetEntity.setStationId(selRetEntityTmp.getStationId());
                finalSelRetEntity.setTotalCount(totalCount);
                finalSelRetEntity.setTotalGet(totalGet);
                finalList.add(finalSelRetEntity);
                finalSelRetEntity = new FinalSelRetEntity();
                qusInfo = new HashMap<String, Integer>();
                qusInfo.put(selRetEntity.getQusStation(), selRetEntity.getTotalCount());
                totalGet = selRetEntity.getTotalCount();
                totalCount = getSelTotalCount(selRetEntity.getCityId(), selRetEntity.getSelStationId(), selCountList);
            } else {
                qusInfo.put(selRetEntity.getQusStation(), selRetEntity.getTotalCount());
                totalGet += selRetEntity.getTotalCount();
                totalCount = getSelTotalCount(selRetEntity.getCityId(), selRetEntity.getSelStationId(), selCountList);
            }
            tmpStaffId = selRetEntity.getStaffId();
            selRetEntityTmp = selRetEntity;
        }
        qusInfo.put(selRetEntityTmp.getQusStation(), selRetEntityTmp.getTotalCount());
        //totalGet += selRetEntityTmp.getTotalCount();
        totalCount = getSelTotalCount(selRetEntityTmp.getCityId(), selRetEntityTmp.getSelStationId(), selCountList);
        finalSelRetEntity.setCity(selRetEntityTmp.getCity());
        finalSelRetEntity.setCityId(selRetEntityTmp.getCityId());
        finalSelRetEntity.setDept(selRetEntityTmp.getDept());
        double percentGet = new BigDecimal((float)totalGet/totalCount).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        finalSelRetEntity.setPercentGet(percentGet);
        finalSelRetEntity.setQusInfo(qusInfo);
        finalSelRetEntity.setSelStation(selRetEntityTmp.getSelStation());
        finalSelRetEntity.setSelStationId(selRetEntityTmp.getSelStationId());
        finalSelRetEntity.setStaffId(selRetEntityTmp.getStaffId());
        finalSelRetEntity.setStaffName(selRetEntityTmp.getStaffName());
        finalSelRetEntity.setStation(selRetEntityTmp.getStation());
        finalSelRetEntity.setStationId(selRetEntityTmp.getStationId());
        finalSelRetEntity.setTotalCount(totalCount);
        finalSelRetEntity.setTotalGet(totalGet);
        finalList.add(finalSelRetEntity);

        return finalList;
    }
}
