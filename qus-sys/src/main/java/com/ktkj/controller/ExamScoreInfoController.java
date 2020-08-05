/*
 * 类名称:ExamScoreInfoController.java
 * 包名称:com.ktkj.controller
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-10-09 15:21:52        lipengjun     初版做成
 *
 * Copyright (c) 2019-2019 厦门继续想科技有限公司
 */
package com.ktkj.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ktkj.annotation.SysLog;
import com.ktkj.cache.J2CacheUtils;
import com.ktkj.entity.*;
import com.ktkj.service.*;
import com.ktkj.utils.*;
import com.ktkj.controller.AbstractController;
import com.ktkj.utils.excel.ExcelExport;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * Controller
 *
 * @author lipengjun
 * @date 2019-10-09 15:21:52
 */
@RestController
@RequestMapping("/tower/examscoreinfo")
public class ExamScoreInfoController extends AbstractController {
    @Autowired
    private ExamScoreInfoService examScoreInfoService;
    @Autowired
    private StaffInfoService staffInfoService;
    @Autowired
    private DictDefService dictDefService;
    @Autowired
    private FinalExamScoreInfoService finalExamScoreInfoService;
    @Autowired
    private StaffDeptRelService staffDeptRelService;
    @Autowired
    private MailDefService mailDefService;
    @Autowired
    private ExamPerDefService examPerDefService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return R
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("tower:examscoreinfo:list")
    public R queryAll(@RequestParam Map<String, Object> params) {
        List<ExamScoreInfoEntity> list = examScoreInfoService.queryAll(params);

        return R.ok().put("list", list);
    }

    /**
     * 分页查询
     *
     * @param params 查询参数
     * @return R
     */
    @GetMapping("/list")
    @RequiresPermissions("tower:examscoreinfo:list")
    public R list(@RequestParam Map<String, Object> params) {
        //queryDeptScore
        if(params.get("examStation")==null){
            params.put("examStation","25");
        }
        Page page = examScoreInfoService.queryPage(params);

        PageUtils pageUtil = new PageUtils(page.getRecords(), (int)page.getTotal(), (int)page.getSize(), (int)page.getCurrent());

        return R.ok().put("page", pageUtil);
    }

    /**
     * 导出会员
     */
    @RequestMapping("/export")
    @RequiresPermissions("tower:examscoreinfo:export")
    public R export(@RequestParam Map<String, Object> params, HttpServletResponse response) {
        if(params.get("examStation")==null){
            params.put("examStation","25");
        }
        List<ExamScoreInfoEntity> scoreList = examScoreInfoService.queryDeptAll(params);

        ExcelExport ee = null;//new ExcelExport("员工列表");

        String[] header = null;
        if("25".equals(params.get("examStation"))){
            ee = new ExcelExport("部门支撑服务满意度");
            header = new String[]{"被考核部门","部门支撑服务满意度得分" };
        }else if("26".equals(params.get("examStation"))){
            ee = new ExcelExport("部门协作配合满意度");
            header = new String[]{"被考核部门","部门协作配合满意度得分" };
        }else if("27".equals(params.get("examStation"))){
            ee = new ExcelExport("部门自身建设");
            header = new String[]{"被考核部门","部门自身建设得分" };
        }
        List<Map<String, Object>> list = new ArrayList<>();
        if (scoreList != null && scoreList.size() != 0) {
            for (ExamScoreInfoEntity examScoreInfoEntity : scoreList) {
                LinkedHashMap<String, Object> map = new LinkedHashMap<>();
                map.put("EXAM_NAME", examScoreInfoEntity.getExamName());
                map.put("INDEX_ITEM_SCORE", examScoreInfoEntity.getIndexItemScore());
                list.add(map);
            }
        }

        ee.addSheetByMap("部门得分", list, header);
        ee.export(response);
        return R.ok();
    }

    /**
     * 根据主键查询详情
     *
     * @param examResultId 主键
     * @return R
     */
    @RequestMapping("/info/{examResultId}")
    @RequiresPermissions("tower:examscoreinfo:info")
    public R info(@PathVariable("examResultId") Integer examResultId) {
        ExamScoreInfoEntity examScoreInfo = examScoreInfoService.getById(examResultId);

        return R.ok().put("examscoreinfo", examScoreInfo);
    }

    /**
     * 判断是否分管
     * @return
     */
    private static boolean isSameDept(String[] qusDeptId,String[] examDeptId){
        if(qusDeptId==null||examDeptId==null||qusDeptId.length==0||examDeptId.length==0) return false;
        for (String viceDeptId:qusDeptId
             ) {
            for (String deptId:examDeptId
                 ) {
                if(viceDeptId.equals(deptId)){
                    return true;
                }
            }
        }
        return false;
    }
    @RequestMapping("/checkAllSubmit")
    public R checkAllSubmit(){
        Map<String, Object> params1 = new HashMap<String, Object>();
        //params1.put("sendMailMark","0");
        params1.put("isSubmit","0");
        List<StaffInfoEntity> staffInfoList = staffInfoService.queryAll(params1);
        if(staffInfoList!=null&&staffInfoList.size()!=0){
            return R.error("还有员工未提交问卷，还不能启动计算！您可在员工管理界面查看未提交问卷员工。");
        }
        return R.ok();
    }

    /**
     * 计算最终的考核结果
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/calcFinal")
    @RequiresPermissions("tower:examscoreinfo:calcFinal")
    public R calcFinal(HttpServletRequest request,
                      HttpServletResponse response) {
        //判断是否还有用户未提交问卷结果
        Map<String, Object> params1 = new HashMap<String, Object>();
        //params1.put("sendMailMark","0");
        params1.put("isSubmit","0");
        List<StaffInfoEntity> staffInfoList = staffInfoService.queryAll(params1);
        if(staffInfoList!=null&&staffInfoList.size()!=0){
            return R.error("还有员工未提交问卷，还不能启动计算！您可在员工管理界面查看未提交问卷员工。");
        }
        Map<String, Object> params2 = new HashMap<String, Object>();
        List<ExamScoreInfoEntity> scoreList = examScoreInfoService.queryAll(params2);
        for (ExamScoreInfoEntity entity : scoreList
        ) {
            String qusStation = entity.getQusNaireStation();
            String examStation = entity.getExamStation();
            String[] examDeptId = null;
            String[] qusDeptId = null;
            if(StringUtils.isNotEmpty(entity.getExamDeptId())){
                examDeptId = entity.getExamDeptId().split(",");//被评价员工归属部门
            }
            if(StringUtils.isNotEmpty(entity.getQusNaireDeptId())){
                qusDeptId = entity.getQusNaireDeptId().split(",");//评分人归属部门
            }
            entity.setExelStation(entity.getQusNaireStation());
            entity.setExelStationName(entity.getQusNaireStationName());
            //被考核人：省部门正职
            if (Station.provinceDeptManager.equals(examStation)) {
                //省分部门正职+省分部门副职=省分部门负责人
                if (Station.provinceDeptManager.equals(qusStation)||
                        Station.provinceDeptViceManager.equals(qusStation)) {
                    entity.setExelStation(Station.provinceMiddleManager);
                    entity.setExelStationName(dictDefService.qryItemName("EXEL_STATION", Station.provinceMiddleManager));
                }
            }
            //被考核人：省部门副职
            if (Station.provinceDeptViceManager.equals(examStation)) {
                //同个省部门的正职==本部门正职
                if (Station.provinceDeptManager.equals(qusStation)&&isSameDept(qusDeptId, examDeptId)) {
                    entity.setExelStation(Station.localManager);
                    entity.setExelStationName(dictDefService.qryItemName("EXEL_STATION", Station.localManager));
                }else if(Station.provinceDeptManager.equals(qusStation)||
                        Station.provinceDeptViceManager.equals(qusStation)){//除了本部门正职外的其他省部门负责人=其他省分部门负责人
                    entity.setExelStation(Station.provinceOtherMiddleManager);
                    entity.setExelStationName(dictDefService.qryItemName("EXEL_STATION", Station.provinceOtherMiddleManager));
                }

            }
            //被考核人：省部门正职、副职
            if(Station.provinceDeptManager.equals(examStation)||
                    Station.provinceDeptViceManager.equals(examStation)){
                //省分员工=省本部全体员工
                if (Station.provinceStaff.equals(qusStation)) {
                    entity.setExelStation(Station.localStaff);
                    entity.setExelStationName(dictDefService.qryItemName("EXEL_STATION", Station.localStaff));
                }
            }

            //被考核人：地市正职
            if (Station.cityManager.equals(examStation)) {
                if(Station.provinceDeptManager.equals(qusStation)){//省分部门主要负责人
                    entity.setExelStation(Station.provinceMainMiddleManager);
                    entity.setExelStationName(dictDefService.qryItemName("EXEL_STATION", Station.provinceMainMiddleManager));
                }

            }

            //被考核人：地市副职
            if (Station.cityViceManager.equals(examStation)) {
                if(Station.cityViceManager.equals(qusStation)){
                    entity.setExelStation(Station.cityOtherViceManager);
                    entity.setExelStationName(dictDefService.qryItemName("EXEL_STATION", Station.cityOtherViceManager));

                }
            }
            //被考核人：正职、副职&&评分人：市分员工
            if (Station.cityManager.equals(examStation)||Station.cityViceManager.equals(examStation)){
                if (Station.cityStaff.equals(qusStation)) {
                    entity.setExelStation(Station.representStaff);
                    entity.setExelStationName(dictDefService.qryItemName("EXEL_STATION", Station.representStaff));

                }
            }
        }
        //批量更新
        int partCount0 = scoreList.size()/1000 +1;
        for(int i=0;i<partCount0;i++){
            List<ExamScoreInfoEntity> finalPartList = null;
            if(i==partCount0-1){
                finalPartList = scoreList.subList(i*1000,scoreList.size());
            }else{
                finalPartList = scoreList.subList(i*1000,(i+1)*1000);
            }
            if(finalPartList!=null&&finalPartList.size()!=0){
                examScoreInfoService.updateBatchById(finalPartList);
            }
        }
        Map<String,Object> perMap = new HashMap<String,Object>();
        List<ExamPerDefEntity> perList = examPerDefService.queryAll(perMap);
        Integer examStaffIdTmp = 99999999;
        String examStationIdTmp = "";
                //计算考核结果
        List<FinalExamScoreInfoEntity> finalList = examScoreInfoService.queryFinalScore();
        List<FinalExamScoreInfoEntity> finalRetList = new ArrayList<FinalExamScoreInfoEntity>();
        List<String> personList = new ArrayList<String>();
        FinalExamScoreInfoEntity entityTmp = null;
        for (FinalExamScoreInfoEntity entity:finalList
             ) {
            String qusStationId = entity.getQusNaireStation();
            if(examStaffIdTmp == 99999999||examStaffIdTmp.equals(entity.getExamStaffId())){
                personList.add(qusStationId);
            }else{
                List<String> examList = getExamList(perList,examStationIdTmp);
                //判断是否有缺项，缺项需补上
                boolean isNotContain = examList.removeAll(personList);
                int containSize = personList.size();//一共包含多少项
                if(examList!=null&&examList.size()!=0){
                    setArgScore(containSize,examStaffIdTmp,finalList);
                }
                for (String str:examList
                     ) {
                    FinalExamScoreInfoEntity add = new FinalExamScoreInfoEntity();
                    add.setFinalScore(new BigDecimal(0));
                    add.setCreateTime(new Date());
                    add.setExamCity(entityTmp.getExamCity());
                    add.setExamCityId(entityTmp.getExamCityId());
                    add.setExamDept(entityTmp.getExamDept());
                    add.setExamDeptId(entityTmp.getExamDeptId());
                    add.setExamName(entityTmp.getExamName());
                    add.setExamStaffId(entityTmp.getExamStaffId());
                    add.setExamStation(entityTmp.getExamStation());
                    add.setExamStationName(entityTmp.getExamStationName());
                    add.setQusNaireStation(str);
                    add.setQusNaireStationName(dictDefService.qryItemName("EXEL_STATION", str));
                    finalRetList.add(add);
                }
                personList = new ArrayList<String>();
                personList.add(qusStationId);
            }
            examStaffIdTmp = entity.getExamStaffId();
            examStationIdTmp = entity.getExamStation();
            entityTmp = entity;
            finalRetList.add(entity);
        }
        //计算考核结果并入库
        int partCount = finalRetList.size()/1000 +1;
        for(int i=0;i<partCount;i++){
            List<FinalExamScoreInfoEntity> finalPartList = null;
            if(i==partCount-1){
                finalPartList = finalRetList.subList(i*1000,finalRetList.size());
            }else{
                finalPartList = finalRetList.subList(i*1000,(i+1)*1000);
            }
            if(finalPartList!=null&&finalPartList.size()!=0){
                finalExamScoreInfoService.insertBatch(finalPartList);
            }
        }
        try {
            sendMailNotice();
        }catch(Exception e){
            e.printStackTrace();
        }
        return R.ok();
    }

    private List<FinalExamScoreInfoEntity> setArgScore(int containSize,Integer examStaffId,List<FinalExamScoreInfoEntity> scoreList){
        for (FinalExamScoreInfoEntity entity:scoreList
             ) {
            if(examStaffId.equals(entity.getExamStaffId())){
                entity.setFinalScore(entity.getIndexItemScore().divide(new BigDecimal(containSize)));
            }
        }
        return scoreList;
    }

    private List<String> getExamList(List<ExamPerDefEntity> perList,String examStationId){
        List<String> examList = new ArrayList<String>();
        for (ExamPerDefEntity entity:perList
             ) {
            if(entity.getExamStationId().equals(examStationId)){
                examList.add(entity.getQusStationId());
            }
        }
        return examList;
    }

    private void sendMailNotice()throws Exception{
        Map<String, Object> qryParams = new HashMap<String, Object>();
        qryParams.put("delFlag","0");
        List<MailDefEntity> mailList = mailDefService.queryAll(qryParams);
        if(mailList==null||mailList.size()==0){
            return ;
        }else if(mailList.size()>1){
            return ;
        }
        MailDefEntity mailDefEntity = mailList.get(0);
        //发送通知邮件
        String mailTo = getUser().getEmail();    // 指明邮件的收件人
        String mailText = "您好，管理员！线上绩效考核系统已完成计算，请登录系统导出结果！";    // 邮件的文本内容：告知用户名和初始密码，以及登录的系统的URL
        Properties prop = new Properties();
        // 设置邮件传输采用的协议smtp
        prop.setProperty("mail.transport.protocol", "smtp");
        // 设置发送人邮件服务器的smtp地址
        // 这里以网易的邮箱smtp服务器地址为例
        prop.setProperty("mail.smtp.host", mailDefEntity.getMailHost());
        // 设置验证机制
        prop.setProperty("mail.smtp.auth", "true");

        // SMTP 服务器的端口 (非 SSL 连接的端口一般默认为 25, 可以不添加, 如果开启了 SSL 连接,
        // 需要改为对应邮箱的 SMTP 服务器的端口, 具体可查看对应邮箱服务的帮助,
        // QQ邮箱的SMTP(SLL)端口为465或587, 其他邮箱自行去查看)

        final String smtpPort = "465";
        prop.setProperty("mail.smtp.port", smtpPort);
        prop.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        prop.setProperty("mail.smtp.socketFactory.fallback", "false");
        prop.setProperty("mail.smtp.socketFactory.port", smtpPort);
//        prop.setProperty("mail.host", mailDefEntity.getMailHost());
//        prop.setProperty("mail.transport.protocol", "smtp");
//        prop.setProperty("mail.smtp.auth", "true");
//        System.setProperty("java.net.preferIPv4Stack", "true");
        // 1、创建session
        Session session = Session.getInstance(prop);
        // 开启Session的debug模式，这样就可以查看到程序发送Email的运行状态
        session.setDebug(true);
        // 2、通过session得到transport对象
        Transport ts = session.getTransport();
        // 3、使用邮箱的用户名和密码连上邮件服务器，发送邮件时，发件人需要提交邮箱的用户名和密码给smtp服务器，用户名和密码都通过验证之后才能够正常发送邮件给收件人。
        ts.connect(mailDefEntity.getMailHost(), mailDefEntity.getMailFrom(), mailDefEntity.getPasswordMailFrom());
        // 4、创建邮件
        Message message = MailUtils.createSimpleMail(session, mailDefEntity.getMailFrom(), mailTo, "线上绩效考核计算已完成", mailText);
        // 5、发送邮件
        ts.sendMessage(message, message.getAllRecipients());
        ts.close();
    }

    @RequestMapping("/save")
    @ResponseBody
    //@RequiresPermissions("tower:examindexrel:save")
    public R save(HttpServletRequest request,
                  HttpServletResponse response) {
        List<ExamScoreInfoEntity> retList = new ArrayList<ExamScoreInfoEntity>();
        String scoreListStr = ""+request.getParameter("scoreListStr");
        String token = request.getParameter("token");
        String isCommit = request.getParameter("isCommit");
        StaffInfoEntity staffInfo = null;
        if(StringUtils.isEmpty(token)||J2CacheUtils.get(token)!=null){
            Integer staffId = (Integer)J2CacheUtils.get(token);
            staffInfo = staffInfoService.getById(staffId);
        }else{
            return R.error("您未登录或已失效，请重新登录！");
        }
        String stationId = staffInfo.getStationId();
        String stationName = staffInfo.getStation();
        /*String isChief = staffInfo.getIsChief();
        String isRepresent = staffInfo.getIsRepresent();
        if(Station.provinceDeptViceManager.equals(stationId)&&"1".equals(isChief)&&"1".equals(isRepresent)){
            stationId = Station.provinceStaff;
            stationName = "省公司员工";
        }*/
        List<ExamScoreInfoEntity> list = JSON.parseArray(scoreListStr,ExamScoreInfoEntity.class);
        for (ExamScoreInfoEntity examScoreInfoEntity:list
        ) {
            examScoreInfoEntity.setQusNaireCity(staffInfo.getCity());
            examScoreInfoEntity.setQusNaireCityId(staffInfo.getCityId());
            examScoreInfoEntity.setQusNaireDept(staffInfo.getDept());
            if(StringUtils.isNotEmpty(staffInfo.getDeptId())){
                examScoreInfoEntity.setQusNaireDeptId(staffInfo.getDeptId());
            }
            examScoreInfoEntity.setQusNaireName(staffInfo.getStaffName());
            examScoreInfoEntity.setQusNaireStaffId(staffInfo.getStaffId());
            examScoreInfoEntity.setQusNaireStation(stationId);
            examScoreInfoEntity.setQusNaireStationName(stationName);
            examScoreInfoEntity.setCreateTime(new Date());
            examScoreInfoEntity.setQusNaireViceDept(staffInfo.getRiceDept());
            examScoreInfoEntity.setQusNaireViceDeptId(staffInfo.getRiceDeptId());
        }
        //将员工的填写状态更新
        if("1".equals(isCommit)){
            staffInfo.setIsSubmit("1");
        }
        examScoreInfoService.insertBatch(list,staffInfo);
        return R.ok().put("examScoreInfo",list);
    }

    /**
     * 新增
     *
     * @param examScoreInfo examScoreInfo
     * @return R
     */
    @SysLog("新增")
    @RequestMapping("/saveSingle")
    @RequiresPermissions("tower:examscoreinfo:save")
    public R save(@RequestBody ExamScoreInfoEntity examScoreInfo) {

        examScoreInfoService.add(examScoreInfo);

        return R.ok();
    }

    /**
     * 修改
     *
     * @param examScoreInfo examScoreInfo
     * @return R
     */
    @SysLog("修改")
    @RequestMapping("/update")
    @RequiresPermissions("tower:examscoreinfo:update")
    public R update(@RequestBody ExamScoreInfoEntity examScoreInfo) {

        examScoreInfoService.update(examScoreInfo);

        return R.ok();
    }

    /**
     * 根据主键删除
     *
     * @param examResultIds examResultIds
     * @return R
     */
    @SysLog("删除")
    @RequestMapping("/delete")
    @RequiresPermissions("tower:examscoreinfo:delete")
    public R delete(@RequestBody Integer[] examResultIds) {
        examScoreInfoService.deleteBatch(examResultIds);

        return R.ok();
    }
}
