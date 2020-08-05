/*
 * 类名称:StaffInfoController.java
 * 包名称:com.ktkj.controller
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-10-01 16:23:16        lipengjun     初版做成
 *
 * Copyright (c) 2019-2019 厦门继续想科技有限公司
 */
package com.ktkj.controller;

import com.alipay.api.domain.StaffInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.ktkj.annotation.SysLog;
import com.ktkj.cache.J2CacheUtils;
import com.ktkj.entity.*;
import com.ktkj.service.*;
import com.ktkj.utils.*;
import com.ktkj.utils.excel.CatalogExcelUtil;
import com.ktkj.utils.excel.ExcelExport;
import com.ktkj.utils.excel.ExelUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.web.multipart.MultipartFile;

/**
 * Controller
 *
 * @author lipengjun
 * @date 2019-10-01 16:23:16
 */
@RestController
@RequestMapping("/tower/staffinfo")
public class StaffInfoController extends AbstractController {
    @Autowired
    private StaffInfoService staffInfoService;
    @Autowired
    private DictDefService dictDefService;
    @Autowired
    private MailDefService mailDefService;
    @Autowired
    private QuestionnaireDefService questionnaireDefService;
    @Autowired
    private ExamItemDefService examItemDefService;
    @Autowired
    private SysConfigService sysConfigService;

    //private static String mailFrom = "1490771580@qq.com";// 指明邮件的发件人
   // private static String password_mailFrom = "voxpflrnhlytieji";// 指明邮件的发件人登陆密码
   // String mailTittle = "亲爱的铁塔员工，请登录问卷系统进行问答！";// 邮件的标题
    //String mail_host = "smtp.qq.com";	// 邮件的服务器域名
    //String qusUrl = "https://qusnaire.jixuxiang.com/qusnaire";//问卷考核系统地址
    @Autowired
    private StaffDeptRelService staffDeptRelService;
    @Autowired
    private SysDeptService sysDeptService;
    @Autowired
    private IndexItemDefService indexItemDefService;
    @Autowired
    private ExamScoreInfoService examScoreInfoService;

    private static String YZM60 = "YZM60";//60秒内只能发送一次验证码

    private static String YZM300 = "YZM300";//300秒失效

    private static String YZM5 = "YZM5";

    private static String YZM = "YZM";

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return R
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("tower:staffinfo:list")
    public R queryAll(@RequestParam Map<String, Object> params) {
        List<StaffInfoEntity> list = staffInfoService.queryAll(params);

        return R.ok().put("list", list);
    }

    /**
     * 密码修改
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/uptPwd")
    @ResponseBody
    public R uptPwd(HttpServletRequest request,
                    HttpServletResponse response){
        String phone = ""+request.getParameter("userName");
        String oldPwd = ""+request.getParameter("oldPwd");
        String newPwd = ""+request.getParameter("newPwd");
        String newConfirmPwd = ""+request.getParameter("newConfirmPwd");
        String msgCode = ""+request.getParameter("msgCode");
        if(StringUtils.isEmpty(phone)){
            return R.error("请输入手机号码！");
        }
        if(J2CacheUtils.get(YZM,YZM300+"_"+phone)==null){
            return R.error("请先发送验证码！");
        }
        if(StringUtils.isEmpty(msgCode)){
            return R.error("请输入验证码！");
        }
        if(StringUtils.isEmpty(oldPwd)){
            return R.error("请输入旧密码！");
        }
        if(StringUtils.isEmpty(newPwd)){
            return R.error("请输入新密码！");
        }
        if(!newPwd.equals(newConfirmPwd)){
            return R.error("两次输入的新密码不一致！");
        }
        String msgCodeRedis = (String)J2CacheUtils.get(YZM,YZM300+"_"+phone);
        long lastSeconds = (long)J2CacheUtils.get(YZM,YZM300+"_"+phone+"_seconds");
        long nowSeconds = new Date().getTime();
        if(nowSeconds-lastSeconds>5*60*1000){
            return R.error("验证码已失效，请重新发送！");
        }
        if(!msgCode.equals(msgCodeRedis)){
            return R.error("验证码不正确！");
        }
        //密码匹配正则表达式
        if (!newPwd.matches("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$")) {
            return R.error("请输入6~20位字母+数字组合的密码！");
        }
        Map<String, Object> qryParams = new HashMap<String, Object>();
        qryParams.put("phone",phone);
        //先查询用户是否存在
        List<StaffInfoEntity> staffList = staffInfoService.queryAll(qryParams);
        if(staffList==null||staffList.size()==0){
            return R.error("您填写的手机号码不存在对应的员工信息，请联系管理员");
        }
        StaffInfoEntity staffInfoEntity = staffList.get(0);
        String oldPwdHex = new Sha256Hash(oldPwd).toHex();
        if(!oldPwdHex.equals(staffInfoEntity.getPwd())){
            return R.error("旧密码输入错误！");
        }
        staffInfoEntity.setPwd(new Sha256Hash(newPwd).toHex());
        staffInfoEntity.setIsUptPwd("1");
        staffInfoService.update(staffInfoEntity);
        return R.ok();

    }

    /**
     * 退出
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/logout")
    @ResponseBody
    public R logout(HttpServletRequest request,
                    HttpServletResponse response){
        String token = request.getParameter("token");
        J2CacheUtils.remove(token);
        return R.ok();
    }
    @RequestMapping("/sendCode")
    @ResponseBody
    public R sendCode(HttpServletRequest request,
                      HttpServletResponse response){
        try {
            String phone = request.getParameter("phone");
            //60秒内只能发送一次
            if(J2CacheUtils.get(YZM,YZM60+"_"+phone)!=null){
                long lastSeconds = (long)J2CacheUtils.get(YZM,YZM60+"_"+phone);
                long nowSeconds = new Date().getTime();
                if(nowSeconds-lastSeconds<=60*1000){
                    return R.error("验证码60秒内只能发送一次，请稍后再试！");
                }
            }
            SimpleDateFormat fmt = new SimpleDateFormat("(yyyyMMdd)");
            String suffix = fmt.format(new Date());
            //if(J2CacheUtils.get(YZM5+"_"+fmt+"_"+phone))
            //同个号码一天发送不超过5次
            if(J2CacheUtils.get(YZM,YZM5+"_"+fmt+"_"+phone)!=null) {
                Integer times = (Integer) J2CacheUtils.get(YZM,YZM5 + "_" + fmt + "_" + phone);
                if(times>5){
                    return R.error("您的号码当日发送验证码超过五次，已被锁定，请明日再试！");
                }
            }
            //同一个IP一天发送不超过10次
            int code = (int)((Math.random()*9+1)*100000);
            System.out.println("验证码："+code);
            long nowSeconds = new Date().getTime();
            String[] params ={""+code};
            SmsSingleSenderResult ret = TencentSendMsgUtils.sendMsg(phone, params, 474035, "继续想");
            if(ret.result==0){//
                J2CacheUtils.put(YZM,YZM60+"_"+phone,nowSeconds,60*60l,false);
                J2CacheUtils.put(YZM,YZM300+"_"+phone,""+code,60*60l,false);
                J2CacheUtils.put(YZM,YZM300+"_"+phone+"_seconds",nowSeconds,60*60l,false);
                if(J2CacheUtils.get(YZM,YZM5+"_"+fmt+"_"+phone)!=null){
                    Integer times = (Integer)J2CacheUtils.get(YZM,YZM5+"_"+fmt+"_"+phone);
                    J2CacheUtils.put(YZM,YZM5+"_"+fmt+"_"+phone,times+1,60*60l,true);
                }else{
                    J2CacheUtils.put(YZM,YZM5+"_"+fmt+"_"+phone,1,60*60l,true);
                }
                return R.ok();
            }else{
                return R.error("短信发送失败，请联系管理员");
            }
        }catch(Exception e){
            e.printStackTrace();
            return R.error("程序异常，短信发送失败！");
        }
    }

    /**
     * 员工登录问卷系统
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/login")
    @ResponseBody
    public R login(HttpServletRequest request,
                   HttpServletResponse response){
        String phone = ""+request.getParameter("userName");
        String pwd = ""+request.getParameter("userPwd");
        if(StringUtils.isEmpty(phone)){
            return R.error("请输入手机号码！");
        }
        if(StringUtils.isEmpty(pwd)){
            return R.error("请输入密码！");
        }

        Map<String, Object> qryParams = new HashMap<String, Object>();
        qryParams.put("phone",phone);
        String pwdHex = new Sha256Hash(pwd).toHex();
        //先查询用户是否存在
        List<StaffInfoEntity> staffList = staffInfoService.queryAll(qryParams);
        if(staffList==null||staffList.size()==0){
            return R.error("账号不存在或密码错误，请重新输入！");
        }
        StaffInfoEntity staffInfoEntity = staffList.get(0);
        if(!"1".equals(staffInfoEntity.getIsUptPwd())){
            return R.error("请先修改密码再登录！");
        }
        if(!pwdHex.equals(staffList.get(0).getPwd())){
            return R.error("账号不存在或密码错误，请重新输入！");
        }
        staffInfoEntity.setLastLoginTime(new Date());
        String token = StringUtils2.genenrateUniqueInd();
        //两个小时内有效
        J2CacheUtils.set(token,staffInfoEntity.getStaffId(), Constant.ALIVE_SECONDS,false);
        staffInfoEntity.setToken(token);
        staffInfoService.updateById(staffInfoEntity);
        String stationId = staffInfoEntity.getStationId();//职务
        //查询当前用户需要填写和问卷信息
        Map<String,Object> qusMap = new HashMap<String,Object>();
        qusMap.put("qusNaireStationId",stationId);
        List<QuestionnaireDefEntity> qusList = questionnaireDefService.queryAll(qusMap);
        QuestionnaireDefEntity qusEntity = null;
        if(qusList!=null&&qusList.size()!=0){
            qusEntity = qusList.get(0);
        }
        //需填写的考核项
        Map<String,Object> examMap = new HashMap<String,Object>();
        examMap.put("qusNaireStationId",stationId);
        List<ExamItemDefEntity> examItemList = examItemDefService.queryAll(examMap);
        List<ExamItemDefAddEntity> examItemAddList = new ArrayList<ExamItemDefAddEntity>();
        for (ExamItemDefEntity entity:examItemList
        ) {
            Integer examItemId = entity.getExamItemId();//
            Map<String,Object> indexMap = new HashMap<String,Object>();
            indexMap.put("examItemId",entity.getExamItemId());
            ExamItemDefAddEntity examItemDefAddEntity = new ExamItemDefAddEntity();
            examItemDefAddEntity.setExamItemDesc(entity.getExamItemDesc());
            examItemDefAddEntity.setExamItemId(entity.getExamItemId());
            examItemDefAddEntity.setExamItemName(entity.getExamItemName());
            examItemDefAddEntity.setExamSeq(entity.getExamSeq());
            examItemDefAddEntity.setExamStation(entity.getExamStation());
            examItemDefAddEntity.setExamStationId(entity.getExamStationId());
            examItemDefAddEntity.setExcellentCount(entity.getExcellentCount());
            examItemDefAddEntity.setGoodCount(entity.getGoodCount());
            examItemDefAddEntity.setNormalCount(entity.getNormalCount());
            examItemDefAddEntity.setExamIndexRels(indexItemDefService.queryExamIndex(indexMap));
            examItemAddList.add(examItemDefAddEntity);
        }
        //要评价的员工信息
        /*被评价人	评价人
        省公司部门正职	省公司该部门副职、员工，地市公司该专业线所有人员（不含区域）
        省公司部门副职	省公司该部门员工，地市公司该专业线所有人员（不含区域）
        地市公司总经理	该地市副总经理、四级经理、普通员工
        地市公司副总经理	该地市四级经理、普通员工
        省公司部门正副职，地市总经理，副总经理	评价自己*/
        /*Map<String,Object> staffMap = new HashMap<String,Object>();
        staffMap.put("cityId",staffInfoEntity.getCityId());//只取属于本单位的人员
        if(Station.provinceDeptViceManager.equals(stationId)){//省公司部门副职，评价省公司部门正职&&自评;
            staffMap.put("stationId",Station.provinceDeptManager);
        }
        if(Station.provinceStaff.equals(stationId)){//省公司普通员工,评价本部门正职、副职
            staffMap.put("stationId",Station.provinceDeptManager+","+Station.provinceDeptViceManager);
        }
        if(Station.cityViceManager.equals(stationId)){//地市公司副职，自评&&评价地市公司正职
            staffMap.put("stationId",Station.cityManager);
        }
        if(Station.cityStaff.equals(stationId)||Station.cityFourthManager.equals(stationId)){//地市公司员工||四级经理，评价地市正职、副职
            staffMap.put("stationId",Station.cityManager+","+Station.cityViceManager);
        }
        List<StaffInfoEntity> staffInfoList = new ArrayList<StaffInfoEntity>();
        if(!Station.provinceDeptManager.equals(stationId)&&!Station.cityManager.equals(stationId)){
            staffInfoList = staffInfoService.qryExamStaff(staffMap);
        }
        if(Station.provinceDeptManager.equals(stationId)||Station.provinceDeptViceManager.equals(stationId)
        ||Station.cityManager.equals(stationId)||Station.cityViceManager.equals(stationId)){//需要自评的
            staffInfoList.add(staffInfoEntity);
        }
        if(Station.provinceDeptViceManager.equals(stationId)
                ||Station.provinceStaff.equals(stationId)){
            Iterator<StaffInfoEntity> it = staffInfoList.iterator();
            while(it.hasNext()){
                StaffInfoEntity examStaffInfo = it.next();
                String [] deptArr = examStaffInfo.getDeptId().split(",");
                if(!isSameDept(deptArr,staffInfoEntity.getDeptId().split(","))){
                    it.remove();
                }
            }
        }*/
        List<StaffInfoEntity> staffInfoList = staffInfoService.qryExamStaffName(staffInfoEntity.getStaffName().trim());
        Map<String,Object> scoreMap = new HashMap<String,Object>();
        scoreMap.put("qusNaireStaffId",staffInfoEntity.getStaffId());
        List<ExamScoreInfoEntity> scoreList = examScoreInfoService.queryAll(scoreMap);
        List<StaffInfoEntity> selStaffInfo = null;
        List<SelectionDefEntity> selDefInfo = null;
        String isSelection = staffInfoEntity.getIsSelection();//是否参与选举
        if("1".equals(isSelection)){
            String selStationId = staffInfoEntity.getSelStationId();
            Map<String,Object> selMap = new HashMap<String,Object>();
            selMap.put("cityId",staffInfoEntity.getCityId());
            selMap.put("stationId",selStationId);
            selStaffInfo = staffInfoService.qrySelStaff(selMap);
            selDefInfo = staffInfoService.qrySelDef(selMap);
        }
        return R.ok().put("token",token)
                .put("qusInfo",qusEntity)
                .put("examStaffInfo",staffInfoList)
                .put("examItemInfo",examItemAddList)
                .put("examScoreInfo",scoreList)
                .put("staffName",staffInfoEntity.getStaffName())
                .put("userName",staffInfoEntity.getPhone())
                .put("isSubmit",staffInfoEntity.getIsSubmit())
                .put("city",staffInfoEntity.getCity())
                .put("selStaffInfo",selStaffInfo)
                .put("selDefInfo",selDefInfo)
                .put("isSelSubmit",staffInfoEntity.getIsSelSubmit())
                .put("isSelection",staffInfoEntity.getIsSelection());

    }
    public StaffInfoEntity getCloneObject(StaffInfoEntity entity){
        StaffInfoEntity copy = new StaffInfoEntity();
        copy.setDeptId(entity.getDeptId());
        copy.setDept(entity.getDept());
        copy.setStation(entity.getStation());
        copy.setStationId(entity.getStationId());
        copy.setCity(entity.getCity());
        copy.setCityId(entity.getCityId());
        copy.setStaffId(entity.getStaffId());
        copy.setStaffName(entity.getStaffName());
        copy.setEmail(entity.getEmail());
        copy.setPhone(entity.getPhone());
        copy.setIsChief(entity.getIsChief());
        copy.setIsRepresent(entity.getIsRepresent());
        copy.setRiceDept(entity.getRiceDept());
        copy.setRiceDeptId(entity.getRiceDeptId());
        return copy;

    }
    private static boolean isSameDept(String[]arr1,String[]arr2){
        for (String arrItem1:arr1
             ) {
            for (String arrItem2:arr2
            ) {
                if(!StringUtils.isEmpty(arrItem1)&&arrItem1.equals(arrItem2)){
                    return true;
                }

            }
        }
        return false;
    }

    @RequestMapping("/qryExamScoreInfo")
    @ResponseBody
    public R qryExamScoreInfo(HttpServletRequest request,
                   HttpServletResponse response){
        String token = request.getParameter("token");
        StaffInfoEntity staffInfo = null;
        if(StringUtils.isEmpty(token)||J2CacheUtils.get(token)!=null){
            Integer staffId = (Integer)J2CacheUtils.get(token);
            staffInfo = staffInfoService.getById(staffId);
        }else{
            return R.error("您未登录或已失效，请重新登录！");
        }
        Map<String,Object> scoreMap = new HashMap<String,Object>();
        scoreMap.put("qusNaireStaffId",staffInfo.getStaffId());
        List<ExamScoreInfoEntity> scoreList = examScoreInfoService.queryAll(scoreMap);
        return R.ok().put("examScoreInfo",scoreList);
    }

    /**
     * 给所有的员工发送邮件：登录问卷系统
     * @param params
     * @return
     */
    @RequestMapping("/sendMailAll")
    @RequiresPermissions("tower:sendmail:all")
    public R sendMailAll(@RequestParam Map<String, Object> params){
        //查询出所有员工信息
        Map<String, Object> qryParams = new HashMap<String, Object>();
        String[] failPhone = null;
        try {
            //若用户为总监、地市员工、省公司员工，得判断是否是员工代表，否则无需发送邮件
            //params.put("sendMailMark","0"); 安徽不需要判断，只要导入系统都要发
            List<StaffInfoEntity> staffList = staffInfoService.queryAll(params);
            qryParams.put("delFlag","0");
            List<MailDefEntity> mailList = mailDefService.queryAll(qryParams);
            if(mailList==null||mailList.size()==0){
                return R.error("发送邮件前请到'发送邮件配置'菜单先配置信息！");
            }else if(mailList.size()>1){
                return R.error("当前在用的发送邮件配置有多组，请更改为一组！");
            }
            failPhone = sendLoginMail(staffList,mailList.get(0));
            sendMailNotice(failPhone,mailList.get(0));
        }catch (Exception e){
            return R.error("程序异常，请联系管理员");
        }
        return R.ok().put("failPhone",failPhone);

    }
    private void sendMailNotice(String[] failArr,MailDefEntity mailDefEntity)throws Exception{
        String failPhone = "";
        for (String fail:failArr
             ) {
            failPhone+=fail+"、";
        }
        //发送通知邮件
        String mailTo = getUser().getEmail();    // 指明邮件的收件人
        String mailText = "您好，管理员！线上绩效考核邮件发送已完成，请登录系统导出结果！";    // 邮件的文本内容：告知用户名和初始密码，以及登录的系统的URL
        if(StringUtils.isNotEmpty(failPhone)){
            mailText += "以下人员邮件发送失败："+failPhone.substring(0,failPhone.length()-1)+"。请检查邮箱地址是否正确且有效！失败记录可在'员工管理'界面修改邮箱并点击'发送登录邮件（选择）'按钮补发。";
        }
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
        Message message = MailUtils.createSimpleMail(session, mailDefEntity.getMailFrom(), mailTo, "线上绩效考核邮件发送已完成", mailText);
        // 5、发送邮件
        ts.sendMessage(message, message.getAllRecipients());
        ts.close();
    }
    private String[] sendLoginMail(List<StaffInfoEntity> staffList,MailDefEntity mailDefEntity){
        String failStaffName = "";
        for (StaffInfoEntity staffInfoEntity : staffList
        ) {
            try {
                String mailTo = staffInfoEntity.getEmail();    // 指明邮件的收件人
                int prePwd = (int) ((Math.random() * 9 + 1) * 100000);
                staffInfoEntity.setPwd(new Sha256Hash(""+prePwd).toHex());//设置初始密码
                String mailText = mailDefEntity.getQusContent()
                        .replace("userName",staffInfoEntity.getPhone())
                        .replace("passWord",""+prePwd)
                        .replace("loginUrl",mailDefEntity.getQusUrl())
                        .replace("\n","<br>");
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

//                Properties prop = new Properties();
//                prop.setProperty("mail.host", mailDefEntity.getMailHost());
//                prop.setProperty("mail.transport.protocol", "smtp");
//                prop.setProperty("mail.smtp.auth", "true");
//                System.setProperty("java.net.preferIPv4Stack", "true");
                // 1、创建session
                Session session = Session.getInstance(prop);
                // 开启Session的debug模式，这样就可以查看到程序发送Email的运行状态
                session.setDebug(true);
                // 2、通过session得到transport对象
                Transport ts = session.getTransport();
                // 3、使用邮箱的用户名和密码连上邮件服务器，发送邮件时，发件人需要提交邮箱的用户名和密码给smtp服务器，用户名和密码都通过验证之后才能够正常发送邮件给收件人。
                ts.connect(mailDefEntity.getMailHost(), mailDefEntity.getMailFrom(), mailDefEntity.getPasswordMailFrom());
                // 4、创建邮件
                Message message = MailUtils.createSimpleMail(session, mailDefEntity.getMailFrom(), mailTo, mailDefEntity.getMailTitle(), mailText);
                // 5、发送邮件
                ts.sendMessage(message, message.getAllRecipients());
                ts.close();
                staffInfoEntity.setSendStatus("1");//成功
                staffInfoEntity.setSendTime(new Date());
                staffInfoEntity.setIsUptPwd("0");

            }catch(Exception e){
                failStaffName += staffInfoEntity.getStaffName()+",";
                String msg = e.toString();
                e.printStackTrace();
                //发送失败的记录下数据库
                staffInfoEntity.setSendStatus("2");//失败
                staffInfoEntity.setSendTime(new Date());
            }

        }
        //批量修改数据库数据
        staffInfoService.updateBatch(staffList);
        if(StringUtils.isNotEmpty(failStaffName)){
            failStaffName = failStaffName.substring(0,failStaffName.length()-1);
            return failStaffName.split(",");
        }
        return null;
    }
    /**
     * 分页查询
     *
     * @param params 查询参数
     * @return R
     */
    @GetMapping("/list")
    @RequiresPermissions("tower:staffinfo:list")
    public R list(@RequestParam Map<String, Object> params) {
        if(params.get("staffName")!=null){
            String staffName = java.net.URLDecoder.decode(""+params.get("staffName"));
            params.put("staffName",staffName);
        }
        if (getUserId() != Constant.SUPER_ADMIN&&!"Z".equals(getUser().getCityId())) {//不是超级管理员，只能查看本地市的数据
            params.put("cityId", getUser().getCityId());
        }
        Page page = staffInfoService.queryPage(params);
        PageUtils pageUtil = new PageUtils(page.getRecords(), (int)page.getTotal(), (int)page.getSize(), (int)page.getCurrent());
        return R.ok().put("page", pageUtil);

    }

    /**
     * 根据主键查询详情
     *
     * @param staffId 主键
     * @return R
     */
    @RequestMapping("/info/{staffId}")
    @RequiresPermissions("tower:staffinfo:info")
    public R info(@PathVariable("staffId") Integer staffId) {
        StaffInfoEntity staffInfo = staffInfoService.getById(staffId);

        return R.ok().put("staffInfo", staffInfo);
    }

    /**
     * 给指定的员工发送邮件
     * @param staffIds
     * @return
     */
    @RequestMapping("/sendMailById")
    @RequiresPermissions("tower:sendmail:byid")
    public R sendMailById(@RequestBody Integer[] staffIds){
        //查询出所有员工信息
       String[] failPhone = null;
       try {
           List<StaffInfoEntity> staffList = new ArrayList<StaffInfoEntity>();
           for (Integer staffId:staffIds) {
               StaffInfoEntity staffInfo = staffInfoService.getById(staffId);
               /*String stationId = staffInfo.getStationId();安徽不需要判断
               String isChief = staffInfo.getIsChief();//0否1是
               String isRepresent = staffInfo.getIsRepresent();//0否1是
               if((Station.cityStaff.equals(stationId)||
                       Station.provinceStaff.equals(stationId)||
                       "1".equals(isChief))&&"0".equals(isRepresent)){
                   return R.error("员工：'"+staffInfo.getStaffName()+"'"+"非员工代表不能发送问卷邮件！");
               }*/
               staffList.add(staffInfo);
           }
           Map<String,Object> params = new HashMap<String,Object>();
           params.put("delFlag","0");
           List<MailDefEntity> mailList = mailDefService.queryAll(params);
           if(mailList==null||mailList.size()==0){
               return R.error("发送邮件前请先配置邮件信息");
           } else if(mailList.size()>1){
               return R.error("当前在用的发送邮件配置有多组，请更改为一组！");
           }
           failPhone = sendLoginMail(staffList,mailList.get(0));
        }catch (Exception e){
            return R.error("程序异常，请联系管理员");
        }
        return R.ok().put("failPhone",failPhone);

    }

    /**
     * 新增
     *
     * @param staffInfo staffInfo
     * @return R
     */
    @SysLog("新增")
    @RequestMapping("/save")
    @RequiresPermissions("tower:staffinfo:save")
    public R save(@RequestBody StaffInfoEntity staffInfo) {
        staffInfo.setCreateTime(new Date());
        staffInfo.setCreateOperator(getUser().getUsername());
        Map<String, Object> params = new HashMap<String,Object>();
        String phone = staffInfo.getPhone().trim();
        params.put("phone",phone);
        if(!isNumber(phone)||phone.length()!=11){
            return R.error("您输入的手机号码格式不正确，请重新输入！");
        }
        List<StaffInfoEntity> list = staffInfoService.queryAll(params);
        if(list!=null&&list.size()!=0){
            return R.error("您输入的手机号码已被添加过，请重新输入！");
        }
        Map<String, Object> params2 = new HashMap<String,Object>();
        params2.put("email",staffInfo.getEmail());
        List<StaffInfoEntity> list2 = staffInfoService.queryAll(params2);
        if(list2!=null&&list2.size()!=0){
            return R.error("您输入的邮箱已被添加过，请重新输入！");
        }
        String cityId = staffInfo.getCityId();
        if(getUserId()!=Constant.SUPER_ADMIN&&!"Z".equals(getUser().getCityId())){
            cityId = getUser().getCityId();
        }
        staffInfo.setCity(dictDefService.qryItemName("CITY",cityId));
        staffInfo.setCityId(cityId);
        staffInfo.setStation(dictDefService.qryItemName("STATION",staffInfo.getStationId()));
        staffInfoService.add(staffInfo);
        return R.ok();
    }

    /**
     * 修改
     *
     * @param staffInfo staffInfo
     * @return R
     */
    @SysLog("修改")
    @RequestMapping("/update")
    @RequiresPermissions("tower:staffinfo:update")
    public R update(@RequestBody StaffInfoEntity staffInfo) {
        staffInfo.setUpdateTime(new Date());
        staffInfo.setUpdateOperator(getUser().getUsername());
        String cityId = staffInfo.getCityId();
        if(getUserId()!=Constant.SUPER_ADMIN&&!"Z".equals(getUser().getCityId())){
            cityId = getUser().getCityId();
        }
        staffInfo.setCity(dictDefService.qryItemName("CITY",cityId));
        staffInfo.setCityId(cityId);
        staffInfo.setStation(dictDefService.qryItemName("STATION",staffInfo.getStationId()));
        staffInfoService.update(staffInfo);

        return R.ok();
    }

    /**
     * 根据主键删除
     *
     * @param staffIds staffIds
     * @return R
     */
    @SysLog("删除")
    @RequestMapping("/delete")
    @RequiresPermissions("tower:staffinfo:delete")
    public R delete(@RequestBody Integer[] staffIds) {
        staffInfoService.deleteBatch(staffIds);

        return R.ok();
    }

    @SysLog("选为员工代表")
    @RequestMapping("/ispresent")
    @RequiresPermissions("tower:ispresent:byid")
    public R isPresent(@RequestBody Integer[] staffIds) {
        staffInfoService.updateBatch(staffIds,"1");
        return R.ok();
    }

    /**
     * 导出会员
     */
    @RequestMapping("/export")
    @RequiresPermissions("tower:staffinfo:export")
    public R export(@RequestParam Map<String, Object> params, HttpServletResponse response) {
        if(params.get("staffName")!=null){
            String staffName = java.net.URLDecoder.decode(""+params.get("staffName"));
            params.put("staffName",staffName);
        }
        if (getUserId() != Constant.SUPER_ADMIN&&!"Z".equals(getUser().getCityId())) {//不是超级管理员，只能查看本地市的数据
            params.put("cityId", getUser().getCityId());
        }
        List<StaffInfoEntity> orderList = staffInfoService.queryAll(params);

        ExcelExport ee = new ExcelExport("员工列表");

        String[] header = new String[]{"姓名", "归属地市", "归属部门", "分管部门","职务",
                "邮箱","手机号码","发送邮件状态","是否员工代表","是否总监","问卷是否提交"};
        List<Map<String, Object>> list = new ArrayList<>();
        if (orderList != null && orderList.size() != 0) {
            for (StaffInfoEntity staffInfoEntity : orderList) {
                LinkedHashMap<String, Object> map = new LinkedHashMap<>();
                map.put("STAFF_NAME", staffInfoEntity.getStaffName());
                map.put("CITY", staffInfoEntity.getCity());
                map.put("DEPT", staffInfoEntity.getDept());
                map.put("RICE_DEPT", staffInfoEntity.getRiceDept());
                map.put("STATION", staffInfoEntity.getStation());
                map.put("EMAIL", staffInfoEntity.getEmail());
                map.put("PHONE", staffInfoEntity.getPhone());
                if("1".equals(staffInfoEntity.getSendStatus())){
                    map.put("SEND_STATUS", "发送成功");
                }else if("2".equals(staffInfoEntity.getSendStatus())){
                    map.put("SEND_STATUS", "发送失败");
                }else{
                    map.put("SEND_STATUS", "未发送");
                }
                /*if("1".equals(staffInfoEntity.getIsRepresent())){
                    map.put("IS_REPRESENT", "是");
                }else{
                    map.put("IS_REPRESENT", "否");
                }
                if("1".equals(staffInfoEntity.getIsChief())){
                    map.put("IS_CHIEF", "是");
                }else{
                    map.put("IS_CHIEF", "否");
                }*/
                if("1".equals(staffInfoEntity.getIsRepresent())){
                    map.put("IS_SUBMIT", "已提交");
                }else{
                    map.put("IS_SUBMIT", "未提交");
                }
                list.add(map);
            }
        }

        ee.addSheetByMap("员工", list, header);
        ee.export(response);
        return R.ok();
    }

    /**
     * 下载上传的模版
     * @param response
     * @param request
     * @throws IOException
     */
    @RequestMapping(value="downloadExcel", method = RequestMethod.GET)//method = RequestMethod.GET将数据传递给前端
    public void downloadExcel(HttpServletResponse response, HttpServletRequest request)throws Exception {
        /**查询地市下的部门有哪些**/
        try {
            boolean isCityUser = false;
            String cityId = request.getParameter("cityId");
            Map<String, Object> map = new HashMap<>();
            if (getUserId() != Constant.SUPER_ADMIN&&!"Z".equals(getUser().getCityId())) {//不是超级管理员，只能查看本地市的数据
                map.put("cityFilter", getUser().getCityId());
                cityId = getUser().getCityId();
                isCityUser = true;
            }else{
                map.put("cityFilter", cityId);
            }
            if(StringUtils.isEmpty(cityId)){
                return ;
            }
            List<SysDeptEntity> deptList = null;
            if (J2CacheUtils.get(Constant.CITY_DEPT+"_"+cityId) != null) {
                deptList = (List<SysDeptEntity>)J2CacheUtils.get(Constant.CITY_DEPT+"_"+cityId);
            }else{
                deptList = sysDeptService.queryList(map);
                J2CacheUtils.set(Constant.CITY_DEPT+"_"+cityId, deptList, Constant.ALIVE_SECONDS, false);
            }
            String[] departSelectList = new String[deptList.size()];
            int count = 0;
            for (SysDeptEntity sysDeptEntity : deptList
            ) {
                departSelectList[count++] = sysDeptEntity.getName();
            }
            /**查询有哪些职务**/
            List<DictDefEntity> dictList = null;
            Map<String, Object> params = new HashMap<>();
            if (J2CacheUtils.get(Constant.DATA_DICT) != null) {
                dictList = (List<DictDefEntity>) J2CacheUtils.get(Constant.DATA_DICT);
            } else {
                dictList = dictDefService.queryAll(params);
                J2CacheUtils.set(Constant.DATA_DICT, dictList, Constant.ALIVE_SECONDS, false);
            }
            /*List<DictDefEntity> retDictList = new ArrayList<DictDefEntity>();
            for (DictDefEntity dictDefEntity:dictList
                 ) {
                if(dictDefEntity.getItemName().startsWith("省")&&isCityUser){
                    continue;
                }
                retDictList.add(dictDefEntity);
            }*/
            String[] stationSelectList = new String[dictList.size()];
            int count1 = 0;
            for (DictDefEntity dictDefEntity : dictList
            ) {
                if ("STATION".equals(dictDefEntity.getTypeCode())) {
                    if(dictDefEntity.getItemName().indexOf("省")!=-1&&"Z".equals(cityId)){//省公司
                        stationSelectList[count1++] = dictDefEntity.getItemName();
                    }
                    if(dictDefEntity.getItemName().indexOf("省")==-1&&!"Z".equals(cityId)){//地市公司
                        stationSelectList[count1++] = dictDefEntity.getItemName();
                    }
                }
            }
            Workbook wb = new HSSFWorkbook();
            Sheet sheet = wb.createSheet("员工信息");
            Map<String, Object> params2 = new HashMap<String, Object>();
            params2.put("key", Constant.TMPLATE_DOWNLOAD_PATH);
            List<SysConfigEntity> configList = sysConfigService.queryList(params2);
            String filePath = configList.get(0).getValue()+"staff_template.xls";

            // 第一行
            Row row = sheet.createRow(0);
            CellStyle style = CatalogExcelUtil.getHeadStyle(wb);

            CatalogExcelUtil.initCell(row.createCell(0), style, "姓名*");
            CatalogExcelUtil.initCell(row.createCell(1), style, "归属部门");
            CatalogExcelUtil.initCell(row.createCell(2), style, "职务*");
            CatalogExcelUtil.initCell(row.createCell(3), style, "邮箱*");
            CatalogExcelUtil.initCell(row.createCell(4), style, "手机号码*");

            // 第3列的第1行到第21行单元格部门下拉 ，可替换为从数据库的部门表数据，
            // hidden_depart 为隐藏的sheet的别名，1为这个sheet的索引 ，考虑到有多个列绑定下拉列表
            if(departSelectList!=null&&departSelectList.length>0){
                wb = ExelUtils.dropDownList2003(wb, sheet, departSelectList, 1, 1000, 1, 1, "hidden_depart", 1);
            }
            if(stationSelectList!=null&&stationSelectList.length>0){
                wb = ExelUtils.dropDownList2003(wb, sheet, stationSelectList, 1, 1000, 2, 2, "hidden_level", 2);
            }
            FileOutputStream stream = new FileOutputStream(filePath);
            wb.write(stream);
            stream.close();


            //获取输入流，原始模板位置
            InputStream bis = new BufferedInputStream(new FileInputStream(new File(filePath)));
            //假如以中文名下载的话，设置下载文件名称
            String filename = "staff_template.xls";
            //转码，免得文件名中文乱码
            filename = URLEncoder.encode(filename, "UTF-8");
            //设置文件下载头
            response.addHeader("Content-Disposition", "attachment;filename=" + filename);
            //1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
            response.setContentType("multipart/form-data");
            BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
            int len = 0;
            while ((len = bis.read()) != -1) {
                out.write(len);
                out.flush();
            }
            out.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 下载选择员工代表的模板
     * @param response
     * @param request
     * @throws Exception
     */
    @RequestMapping(value="downloadExcel2", method = RequestMethod.GET)//method = RequestMethod.GET将数据传递给前端
    public void downloadExcel2(HttpServletResponse response, HttpServletRequest request)throws Exception {
        //获取输入流，原始模板位置
        Map<String, Object> params2 = new HashMap<String, Object>();
        params2.put("key", Constant.TMPLATE_DOWNLOAD_PATH);
        List<SysConfigEntity> configList = sysConfigService.queryList(params2);
        String filePath = configList.get(0).getValue()+"represent_template.xls";
        InputStream bis = new BufferedInputStream(new FileInputStream(new File(filePath)));
        //假如以中文名下载的话，设置下载文件名称
        String filename = "represent_template.xls";
        //转码，免得文件名中文乱码
        filename = URLEncoder.encode(filename, "UTF-8");
        //设置文件下载头
        response.addHeader("Content-Disposition", "attachment;filename=" + filename);
        //1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
        response.setContentType("multipart/form-data");
        BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
        int len = 0;
        while ((len = bis.read()) != -1) {
            out.write(len);
            out.flush();
        }
        out.close();
    }

    /**
     * 员工信息批量上传
     * @param request
     * @param response
     * @param tempfile
     * @param cityId
     * @return
     */
    @RequestMapping(value="/batchSave", method = RequestMethod.POST)
    @ResponseBody
    public R batchSave(HttpServletRequest request,HttpServletResponse response,@RequestParam("tempfile") MultipartFile tempfile,@RequestParam("city") String cityId){
        InputStream in = null ;
        Workbook workbook = null;
        Sheet sheet = null;
        try{
            if(tempfile.isEmpty()){
                return R.error("批量上传附件不能为空");
            }
            in =tempfile.getInputStream();
            workbook= new HSSFWorkbook(in);
            sheet = workbook.getSheetAt(0);
            int len = sheet.getLastRowNum();// 总行数

            if (len < 1) {
                return R.error("导入的文件没有数据，请检查！");
            }
            int max = 1000;
            if (len > max) {
                return R.error("导入的文件记录不得超过1000条！");
            }
            StaffInfoEntity entity = new StaffInfoEntity();
            entity.setCreateTime(new Date());
            entity.setCreateOperator(getUser().getUsername());
            if(getUserId()!=Constant.SUPER_ADMIN&&!"Z".equals(getUser().getCityId())){
                cityId = getUser().getCityId();
            }
            entity.setCity(dictDefService.qryItemName("CITY",cityId));
            entity.setCityId(cityId);
            return staffInfoService.batchSave(sheet,entity);
        } catch (Exception e) {// 异常捕获
            R.error("保存失败");
            logger.error("batchSave",e);
        } finally {
            try {
                if(in != null){
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return R.ok();
    }

    /**
     * 选择员工代表批量上传
     * @param request
     * @param response
     * @param tempfile
     * @return
     */
    @RequestMapping(value="/batchSave2", method = RequestMethod.POST)
    @ResponseBody
    public R batchSave2(HttpServletRequest request,HttpServletResponse response,@RequestParam("tempfile") MultipartFile tempfile){
        InputStream in = null ;
        Workbook workbook = null;
        Sheet sheet = null;
        try{
            if(tempfile.isEmpty()){
                return R.error("批量上传附件不能为空");
            }
            in =tempfile.getInputStream();
            workbook= new HSSFWorkbook(in);
            sheet = workbook.getSheetAt(0);
            int len = sheet.getLastRowNum();// 总行数

            if (len < 1) {
                return R.error("导入的文件没有数据，请检查！");
            }
            int max = 1000;
            if (len > max) {
                return R.error("导入的文件记录不得超过1000条！");
            }
            StaffInfoEntity entity = new StaffInfoEntity();
            entity.setCreateTime(new Date());
            entity.setCreateOperator(getUser().getUsername());
            return staffInfoService.batchSave2(sheet,entity);
        } catch (Exception e) {// 异常捕获
            R.error("保存失败");
            logger.error("batchSave",e);
        } finally {
            try {
                if(in != null){
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return R.ok();
    }
    @RequestMapping("/checkQusLogin")
    @ResponseBody
    public R checkQusLogin(HttpServletRequest request,
                HttpServletResponse response){
        String token = request.getParameter("token");
        StaffInfoEntity staffInfo = null;
        if(StringUtils.isEmpty(token)||J2CacheUtils.get(token)!=null){
            Integer staffId = (Integer)J2CacheUtils.get(token);
            staffInfo = staffInfoService.getById(staffId);
            return R.ok().put("staffInfo",staffInfo);
        }else{
            return R.error("您未登录或已失效，请重新登录！");
        }

    }
    public static boolean isNumber(String str) {
        if (str == null)
            return false;
        Pattern pattern = Pattern.compile("^-?\\d+(\\.\\d+)?$");
        return pattern.matcher(str).matches();
    }

    public static void main(String[] args) {
        System.out.println(new Sha256Hash("123qwe").toHex());
    }


}
