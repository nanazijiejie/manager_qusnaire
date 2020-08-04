/*
 * 类名称:StaffDeptRelServiceImpl.java
 * 包名称:com.ktkj.service.impl
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-10-14 15:00:32        lipengjun     初版做成
 *
 * Copyright (c) 2019-2019 厦门继续想科技有限公司
 */
package com.ktkj.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ktkj.entity.StaffInfoEntity;
import com.ktkj.utils.QueryPlus;
import com.ktkj.dao.StaffDeptRelDao;
import com.ktkj.entity.StaffDeptRelEntity;
import com.ktkj.service.StaffDeptRelService;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Service实现类
 *
 * @author lipengjun
 * @date 2019-10-14 15:00:32
 */
@Service("staffDeptRelService")
public class StaffDeptRelServiceImpl extends ServiceImpl<StaffDeptRelDao, StaffDeptRelEntity> implements StaffDeptRelService {

    @Override
    public List<StaffDeptRelEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

    @Override
    public Page queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "T.id");
        params.put("asc", false);
        Page<StaffDeptRelEntity> page = new QueryPlus<StaffDeptRelEntity>(params).getPage();
        return page.setRecords(baseMapper.selectStaffDeptRelPage(page, params));
    }

    @Override
    public boolean add(StaffDeptRelEntity staffDeptRel) {
        return this.save(staffDeptRel);
    }

    @Override
    public boolean update(StaffDeptRelEntity staffDeptRel) {
        return this.updateById(staffDeptRel);
    }

    @Override
    public boolean delete(Integer id) {
        return this.removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBatch(Integer[] ids) {
        return this.removeByIds(Arrays.asList(ids));
    }

    @Override
    public void delByStaffId(Integer staffId){
        baseMapper.delByStaffId(staffId);
    }

    @Override
    public List<StaffInfoEntity> selectViceDeptByStaff(Integer staffId){
        return baseMapper.selectViceDeptByStaff(staffId);
    }

    public List<StaffInfoEntity> selectViceManagerByDept(Integer staffId){
        return baseMapper.selectViceManagerByDept(staffId);
    }
}
