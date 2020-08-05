/*
 * 类名称:EvaluateInfoController.java
 * 包名称:com.ktkj.controller
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2020-03-24 09:58:44        lipengjun     初版做成
 *
 * Copyright (c) 2019-2019 厦门继续想科技有限公司
 */
package com.ktkj.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ktkj.annotation.SysLog;
import com.ktkj.cache.J2CacheUtils;
import com.ktkj.entity.StaffInfoEntity;
import com.ktkj.service.StaffInfoService;
import com.ktkj.utils.R;
import com.ktkj.controller.AbstractController;
import com.ktkj.entity.EvaluateInfoEntity;
import com.ktkj.service.EvaluateInfoService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Controller
 *
 * @author lipengjun
 * @date 2020-03-24 09:58:44
 */
@RestController
@RequestMapping("/tower/evaluateinfo")
public class EvaluateInfoController extends AbstractController {
    @Autowired
    private EvaluateInfoService evaluateInfoService;
    @Autowired
    private StaffInfoService staffInfoService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return R
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("tower:evaluateinfo:list")
    public R queryAll(@RequestParam Map<String, Object> params) {
        List<EvaluateInfoEntity> list = evaluateInfoService.queryAll(params);

        return R.ok().put("list", list);
    }

    @RequestMapping("/save")
    @ResponseBody
    //@RequiresPermissions("tower:examindexrel:save")
    public R save(HttpServletRequest request,
                  HttpServletResponse response) {
        List<EvaluateInfoEntity> retList = new ArrayList<EvaluateInfoEntity>();
        String scoreListStr = ""+request.getParameter("scoreListStr");
        String token = request.getParameter("token");
        StaffInfoEntity staffInfo = null;
        if(StringUtils.isEmpty(token)|| J2CacheUtils.get(token)!=null){
            Integer staffId = (Integer)J2CacheUtils.get(token);
            staffInfo = staffInfoService.getById(staffId);
        }else{
            return R.error("您未登录或已失效，请重新登录！");
        }
        List<EvaluateInfoEntity> list = JSON.parseArray(scoreListStr,EvaluateInfoEntity.class);
        for (EvaluateInfoEntity evaluateInfoEntity:list
        ) {
            evaluateInfoEntity.setCity(staffInfo.getCity());
            evaluateInfoEntity.setCityId(staffInfo.getCityId());
            evaluateInfoEntity.setStaffDept(staffInfo.getDept());
            evaluateInfoEntity.setStaffName(staffInfo.getStaffName());
            evaluateInfoEntity.setStaffId(staffInfo.getStaffId());
        }
        evaluateInfoService.insertBatch(list,staffInfo);
        return R.ok();
    }


    /**
     * 分页查询
     *
     * @param params 查询参数
     * @return R
     */
    /*@GetMapping("/list")
    @RequiresPermissions("tower:evaluateinfo:list")
    public R list(@RequestParam Map<String, Object> params) {
        Query query = new Query(params);
        Page page = evaluateInfoService.queryPage(params);
        PageUtils pageUtil = new PageUtils(page.getRecords(),
                Integer.parseInt(page.getTotal()+""),
                Integer.parseInt(page.getSize()+""),
                query.getPage());
        return R.ok().put("page", pageUtil);
    }*/

    /**
     * 根据主键查询详情
     *
     * @param evaluateId 主键
     * @return R
     */
    @RequestMapping("/info/{evaluateId}")
    @RequiresPermissions("tower:evaluateinfo:info")
    public R info(@PathVariable("evaluateId") Integer evaluateId) {
        EvaluateInfoEntity evaluateInfo = evaluateInfoService.getById(evaluateId);

        return R.ok().put("evaluateinfo", evaluateInfo);
    }

    /**
     * 新增
     *
     * @param evaluateInfo evaluateInfo
     * @return R

    @SysLog("新增")
    @RequestMapping("/save")
    @RequiresPermissions("tower:evaluateinfo:save")
    public R save(@RequestBody EvaluateInfoEntity evaluateInfo) {

        evaluateInfoService.add(evaluateInfo);

        return R.ok();
    }
     */

    /**
     * 修改
     *
     * @param evaluateInfo evaluateInfo
     * @return R
     */
    @SysLog("修改")
    @RequestMapping("/update")
    @RequiresPermissions("tower:evaluateinfo:update")
    public R update(@RequestBody EvaluateInfoEntity evaluateInfo) {

        evaluateInfoService.update(evaluateInfo);

        return R.ok();
    }

     /* 根据主键删除
     *
     * @param evaluateIds evaluateIds
     * @return R
     */
    @SysLog("删除")
    @RequestMapping("/delete")
    @RequiresPermissions("tower:evaluateinfo:delete")
    public R delete(@RequestBody Integer[] evaluateIds) {
        evaluateInfoService.deleteBatch(evaluateIds);

        return R.ok();
    }
}
