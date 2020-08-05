/*
 * 类名称:StaffInfoServiceImpl.java
 * 包名称:com.ktkj.service.impl
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-10-01 16:23:16        lipengjun     初版做成
 *
 * Copyright (c) 2019-2019 厦门继续想科技有限公司
 */
package com.ktkj.service.impl;

import com.alipay.api.domain.StaffInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ktkj.cache.J2CacheUtils;
import com.ktkj.dao.StaffDeptRelDao;
import com.ktkj.entity.*;
import com.ktkj.service.DictDefService;
import com.ktkj.service.StaffDeptRelService;
import com.ktkj.service.SysDeptService;
import com.ktkj.utils.Constant;
import com.ktkj.utils.QueryPlus;
import com.ktkj.dao.StaffInfoDao;
import com.ktkj.service.StaffInfoService;
import com.ktkj.utils.R;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Service实现类
 *
 * @author lipengjun
 * @date 2019-10-01 16:23:16
 */
@Service("staffInfoService")
public class StaffInfoServiceImpl extends ServiceImpl<StaffInfoDao, StaffInfoEntity> implements StaffInfoService {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private StaffDeptRelService staffDeptRelService;
    @Autowired
    private StaffDeptRelDao staffDeptRelDao;
    @Autowired
    private DictDefService dictDefService;
    @Autowired
    private SysDeptService sysDeptService;
    @Override
    public List<StaffInfoEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

    @Override
    public Page queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "T.staff_id");
        params.put("asc", false);
        Page<StaffInfoEntity> page = new QueryPlus<StaffInfoEntity>(params).getPage();
        return page.setRecords(baseMapper.selectStaffInfoPage(page, params));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(StaffInfoEntity staffInfo) {
        //员工部门关系表入库
        baseMapper.saveKeys(staffInfo);
        String[]deptArr = staffInfo.getDeptId().split(",");
        String[]riceDeptArr = staffInfo.getRiceDeptId().split(",");
        for (String str:deptArr
        ) {
            if(StringUtils.isEmpty(str)) continue;
            StaffDeptRelEntity entity = new StaffDeptRelEntity();
            entity.setDeptId(Integer.valueOf(str));
            entity.setRelType("0");//0:归属 1:分管
            entity.setStaffId(staffInfo.getStaffId());
            entity.setCreateTime(new Date());
            entity.setCreateOperator(staffInfo.getCreateOperator());
            staffDeptRelService.save(entity);
        }
        for (String str:riceDeptArr
        ) {
            if(StringUtils.isEmpty(str)) continue;
            StaffDeptRelEntity entity = new StaffDeptRelEntity();
            entity.setDeptId(Integer.valueOf(str));
            entity.setRelType("1");//0:归属 1:分管
            entity.setStaffId(staffInfo.getStaffId());
            entity.setCreateTime(new Date());
            entity.setCreateOperator(staffInfo.getCreateOperator());
            staffDeptRelService.save(entity);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(StaffInfoEntity staffInfo) {

        staffDeptRelService.delByStaffId(staffInfo.getStaffId());
        if(staffInfo.getDeptId()!=null) {
            String[] deptArr = staffInfo.getDeptId().split(",");
            for (String str : deptArr
            ) {
                StaffDeptRelEntity entity = new StaffDeptRelEntity();
                if (StringUtils.isEmpty(str)) continue;
                entity.setDeptId(Integer.valueOf(str));
                entity.setRelType("0");//0:归属 1:分管
                entity.setStaffId(staffInfo.getStaffId());
                entity.setCreateTime(new Date());
                entity.setCreateOperator(staffInfo.getCreateOperator());
                staffDeptRelService.save(entity);
            }
        }
        if(staffInfo.getRiceDeptId()!=null) {
            String[] riceDeptArr = staffInfo.getRiceDeptId().split(",");
            for (String str : riceDeptArr
            ) {
                StaffDeptRelEntity entity = new StaffDeptRelEntity();
                if (StringUtils.isEmpty(str)) continue;
                entity.setDeptId(Integer.valueOf(str));
                entity.setRelType("1");//0:归属 1:分管
                entity.setStaffId(staffInfo.getStaffId());
                entity.setCreateTime(new Date());
                entity.setCreateOperator(staffInfo.getCreateOperator());
                staffDeptRelService.save(entity);
            }
        }
        if(StringUtils.isEmpty(staffInfo.getDeptId())){
            Map<String,Object> map= new HashMap<String,Object>();
            map.put("deptType","0");
            map.put("staffId",staffInfo.getStaffId());
            baseMapper.updateDeptNull(map);
        }
        if(StringUtils.isEmpty(staffInfo.getRiceDeptId())){
            Map<String,Object> map= new HashMap<String,Object>();
            map.put("deptType","1");
            map.put("staffId",staffInfo.getStaffId());
            baseMapper.updateDeptNull(map);
        }
        return this.updateById(staffInfo);
    }

    @Override
    public boolean delete(Integer staffId) {
        return this.removeById(staffId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBatch(Integer[] staffIds) {
        for (Integer staffId:staffIds) {
            staffDeptRelService.delByStaffId(staffId);
        }
        return this.removeByIds(Arrays.asList(staffIds));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateBatch(List<StaffInfoEntity> list){
        return this.updateBatchById(list);
    }
    @Override
    public void updateBatch(Integer[] staffIds,String status){
        Map<String, Object> map = new HashMap<String,Object>();
        map.put("list", staffIds);
        map.put("status", status);
        baseMapper.updateBatch(map);
    }
    @Override
    public List<StaffInfoEntity> qrySelStaff(Map<String, Object> params){
        return baseMapper.qrySelStaff(params);
    }

    @Override
    public List<StaffInfoEntity> qryExamStaff(Map<String, Object> params){
        return baseMapper.qryExamStaff(params);
    }

    @Override
    public List<StaffInfoEntity> qryExamStaffName(String qusStaffName){
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("staffName",qusStaffName);
        List<QusExamStaffRel> qusExamStaffRelList = baseMapper.qryExamStaffName(params);
        if(qusExamStaffRelList!=null&&qusExamStaffRelList.size()>0){
            QusExamStaffRel QusExamStaffRel = qusExamStaffRelList.get(0);
            String examStaffNameStr = "";
            String[]examStaffNameArr = QusExamStaffRel.getExamStaffName().trim().split("、");
            for(String examStaffName:examStaffNameArr){
                examStaffNameStr += "'"+examStaffName+"',";
            }
            if(StringUtils.isNotEmpty(examStaffNameStr)){
                examStaffNameStr = examStaffNameStr.substring(0,examStaffNameStr.length()-1);
            }
            params.put("staffName",examStaffNameStr);
            return baseMapper.qryExamStaff(params);
        }else{
            return null;
        }
    }

    @Override
    public List<SelectionDefEntity> qrySelDef(Map<String, Object> params){
        return baseMapper.qrySelDef(params);
    }
    private String getStationId(String stationName){
        List<DictDefEntity> dictList = null;
        Map<String, Object> params = new HashMap<>();
        if (J2CacheUtils.get(Constant.DATA_DICT) != null) {
            dictList = (List<DictDefEntity>) J2CacheUtils.get(Constant.DATA_DICT);
        } else {
            dictList = dictDefService.queryAll(params);
            J2CacheUtils.set(Constant.DATA_DICT, dictList, Constant.ALIVE_SECONDS, false);
        }
        for (DictDefEntity item:dictList
             ) {
            if("STATION".equals(item.getTypeCode())&&stationName.equals(item.getItemName())){
                return item.getItemValue();
            }
        }
        return "";
    }

    private Long getDeptId(String deptName,String cityId){
        List<SysDeptEntity> deptList = null;
        Map<String, Object> map = new HashMap<>();
        map.put("cityFilter", cityId);
        if (J2CacheUtils.get(Constant.CITY_DEPT+"_"+cityId) != null) {
            deptList = (List<SysDeptEntity>) J2CacheUtils.get(Constant.CITY_DEPT+"_"+cityId);
        } else {
            deptList = sysDeptService.queryList(map);
            J2CacheUtils.set(Constant.CITY_DEPT+"_"+cityId, deptList, Constant.ALIVE_SECONDS, false);
        }
        for (SysDeptEntity item:deptList
        ) {
            if(deptName.equals(item.getName().trim())){
                return item.getDeptId();
            }
        }
        return 0L;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R batchSave(Sheet sheet, StaffInfoEntity entity) {
        int indexRow=1;
        try {
            Iterator<Row> it = sheet.rowIterator();
            //过滤头
            if(it.hasNext()){
                it.next();
            }
            List <StaffInfoEntity> batch=new ArrayList<StaffInfoEntity>();
            Map mobileMap=new HashMap();
            Map mailMap=new HashMap();
            List<String> mobiles=new ArrayList<>();
            List<String> emails=new ArrayList<>();
            while (it.hasNext()) {
                Row row =it.next();
                StaffInfoEntity staffInfoEntity = new StaffInfoEntity();
                staffInfoEntity.setCreateOperator(entity.getCreateOperator());
                staffInfoEntity.setCreateTime(new Date());
                staffInfoEntity.setCity(entity.getCity());
                staffInfoEntity.setCityId(entity.getCityId());
                //校验姓名
                HSSFCell userName = (HSSFCell) row.getCell(0);
                if(userName!=null&&!StringUtils.isEmpty(conversion(userName))){
                    staffInfoEntity.setStaffName(conversion(userName));
                }else{
                    break;
                    //return R.error("第"+indexRow+"条数据--“姓名”不能为空！");
                }
                //校验地市
                /*XSSFCell city = (XSSFCell) row.getCell(1);
                if(city!=null&&!StringUtils.isEmpty(conversion(city))){
                    staffInfoEntity.setCity(conversion(city));
                }else{
                    return R.error("第"+indexRow+"条数据--“归属地市”不能为空！");
                }*/
                //校验部门
                HSSFCell dept = (HSSFCell) row.getCell(1);
                if(dept!=null&&!StringUtils.isEmpty(conversion(dept))){
                    staffInfoEntity.setDept(conversion(dept));
                    staffInfoEntity.setDeptId(""+getDeptId(conversion(dept),entity.getCityId()));
                }/*else{
                    return R.error("第"+indexRow+"条数据--“归属部门”不能为空！");
                }*/

                //校验职务
                HSSFCell station = (HSSFCell) row.getCell(2);
                if(station!=null&&!StringUtils.isEmpty(conversion(station))){
                    staffInfoEntity.setStation(conversion(station));
                    staffInfoEntity.setStationId(getStationId(conversion(station)));
                }else{
                    return R.error("第"+indexRow+"条数据--“职务”不能为空！");
                }

                //校验邮箱
                HSSFCell email = (HSSFCell) row.getCell(3);
                if(email!=null&&!StringUtils.isEmpty(conversion(email))){
                    String emailStr =conversion(email);
                    staffInfoEntity.setEmail(emailStr);
                    if(mailMap.containsKey(emailStr)){
                        return R.error("第"+indexRow+"条数据--“邮箱” 在导入文件中存在重复数据，请重新上传！");
                    }
                    if(!emailStr.matches("[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+")){
                        return R.error("第"+indexRow+"条数据--“邮箱”格式不正确");
                    }
                    mailMap.put(emailStr,indexRow);
                    emails.add(emailStr);
                }else{
                    return R.error("第"+indexRow+"条数据--“邮箱”不能为空！");
                }
                //校验号码
                HSSFCell mobile = (HSSFCell) row.getCell(4);
                if(mobile!=null&&!StringUtils.isEmpty(conversion(mobile))){
                    staffInfoEntity.setPhone(conversion(mobile));
                    String mobileStr = conversion(mobile);
                    if(mobileMap.containsKey(mobileStr)){
                        return R.error("第"+indexRow+"条数据--“手机号码” 在导入文件中存在重复数据，请重新上传!");
                    }
                    if(!isNumber(mobileStr)||mobileStr.length()!=11){
                        return R.error("第"+indexRow+"条数据--“手机号码”格式不正确!");
                    }
                    mobileMap.put(mobileStr, indexRow);
                    mobiles.add(mobileStr);
                }else{
                    return R.error("第"+indexRow+"条数据--“手机号码”不能为空!");
                }
                indexRow++;
                batch.add(staffInfoEntity);

            }

            List<StaffInfoEntity> qryDataByPhone = baseMapper.qryStaffInfoByMobiles(mobiles);
            if( qryDataByPhone!=null && qryDataByPhone.size()>0 ){
                for (StaffInfoEntity  staffInfo : qryDataByPhone) {
                    if(mobileMap.containsKey(staffInfo.getPhone())){
                        return R.error("第"+mobileMap.get(staffInfo.getPhone())+"条数据--“手机号码”："+staffInfo.getPhone()+"已导入过，不能重复导入!");
                    }
                }
            }
            List<StaffInfoEntity> qryDataByEmail = baseMapper.qryStaffInfoByEmails(emails);
            if( qryDataByEmail!=null && qryDataByEmail.size()>0 ){
                for (StaffInfoEntity  staffInfo : qryDataByEmail) {
                    if(mailMap.containsKey(staffInfo.getEmail())){
                        return R.error("第"+mailMap.get(staffInfo.getEmail())+"条数据--“邮箱”："+staffInfo.getEmail()+"已导入过，不能重复导入!");
                    }
                }
            }
            //批量插入数据
            baseMapper.insertBatch(batch);
            //插入部门的关系
            List<StaffDeptRelEntity> rels = new ArrayList<StaffDeptRelEntity>();
            for (StaffInfoEntity staffInfoEntity:batch
                 ) {
                StaffDeptRelEntity staffDeptRelEntity = new StaffDeptRelEntity();
                staffDeptRelEntity.setDeptId(Integer.valueOf(staffInfoEntity.getDeptId()));
                staffDeptRelEntity.setRelType("0");//0:归属 1:分管
                staffDeptRelEntity.setStaffId(staffInfoEntity.getStaffId());
                staffDeptRelEntity.setCreateTime(new Date());
                staffDeptRelEntity.setCreateOperator(entity.getCreateOperator());
                rels.add(staffDeptRelEntity);
            }
            if(rels!=null&&rels.size()!=0){
                staffDeptRelDao.insertBatch(rels);
            }
            return R.ok();
        }catch (Exception e) {
            logger.error("batchSave",e);
            return R.error("批量上传数据异常，第"+indexRow+"条数据输入不合法！");

        }
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R batchSave2(Sheet sheet, StaffInfoEntity entity) {
        int indexRow=1;
        try {
            Iterator<Row> it = sheet.rowIterator();
            //过滤头
            if(it.hasNext()){
                it.next();
            }
            List <String> batch=new ArrayList<String>();
            while (it.hasNext()) {
                Row row =it.next();
                //校验邮箱
                HSSFCell email = (HSSFCell) row.getCell(0);
                if(email!=null&&!StringUtils.isEmpty(conversion(email))){
                    String emailStr =conversion(email);
                    if(!emailStr.matches("[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+")){
                        return R.error("第"+indexRow+"条数据--“邮箱”格式不正确");
                    }
                    batch.add(emailStr);
                }else{
                    break;
                }
                indexRow++;
            }
            //批量修改
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("list", batch);
            map.put("status", "1");
            baseMapper.updateBatchByEmail(map);
            return R.ok();
        }catch (Exception e) {
            logger.error("batchSave2",e);
            return R.error("批量上传数据异常，第"+indexRow+"条数据输入不合法！");

        }
    }
    public static boolean isEmail(String email){
        if(email==null){
            return false;
        }
        Pattern pattern = Pattern.compile("[\\\\w\\\\.\\\\-]+@([\\\\w\\\\-]+\\\\.)+[\\\\w\\\\-]+");
        //邮箱校验的正则表达式
        return pattern.matcher(email).matches();
    }

    public static void main(String[]args){
        System.out.println("14907717600@qq.com".matches("[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+"));
    }

    public static boolean isNumber(String str) {
        if (str == null)
            return false;
        Pattern pattern = Pattern.compile("^-?\\d+(\\.\\d+)?$");
        return pattern.matcher(str).matches();
    }
    public static String conversion(HSSFCell cell){
        String str="";
        DecimalFormat df = new DecimalFormat("#");
        str=cell.toString();
        switch (cell.getCellType())
        {
            case HSSFCell.CELL_TYPE_NUMERIC:// 数字
                str = df.format(cell.getNumericCellValue());
                break;
            case HSSFCell.CELL_TYPE_STRING:// 字符串
                str =cell.toString();
                break;
            default:
                str = cell.toString();
                break;
        }
        return str.trim();

    }
}
