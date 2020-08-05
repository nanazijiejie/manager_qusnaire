/*
 * 类名称:ExamPerDefController.java
 * 包名称:com.ktkj.controller
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-10-09 15:21:53        lipengjun     初版做成
 *
 * Copyright (c) 2019-2019 厦门继续想科技有限公司
 */
package com.ktkj.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ktkj.annotation.SysLog;
import com.ktkj.service.DictDefService;
import com.ktkj.utils.Constant;
import com.ktkj.utils.PageUtils;
import com.ktkj.utils.Query;
import com.ktkj.utils.R;
import com.ktkj.controller.AbstractController;
import com.ktkj.entity.ExamPerDefEntity;
import com.ktkj.service.ExamPerDefService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller
 *
 * @author lipengjun
 * @date 2019-10-09 15:21:53
 */
@RestController
@RequestMapping("/tower/examperdef")
public class ExamPerDefController extends AbstractController {
    @Autowired
    private ExamPerDefService examPerDefService;
    @Autowired
    private DictDefService dictDefService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return R
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("tower:examperdef:list")
    public R queryAll(@RequestParam Map<String, Object> params) {
        List<ExamPerDefEntity> list = examPerDefService.queryAll(params);

        return R.ok().put("list", list);
    }

    /**
     * 分页查询
     *
     * @param params 查询参数
     * @return R
     */
    @GetMapping("/list")
    @RequiresPermissions("tower:examperdef:list")
    public R list(@RequestParam Map<String, Object> params) {
        Page page = examPerDefService.queryPage(params);
        PageUtils pageUtil = new PageUtils(page.getRecords(), (int)page.getTotal(), (int)page.getSize(), (int)page.getCurrent());
        return R.ok().put("page", pageUtil);
    }

    /**
     * 根据主键查询详情
     *
     * @param percentId 主键
     * @return R
     */
    @RequestMapping("/info/{percentId}")
    @RequiresPermissions("tower:examperdef:info")
    public R info(@PathVariable("percentId") Integer percentId) {
        ExamPerDefEntity examPerDef = examPerDefService.getById(percentId);

        return R.ok().put("examPerDef", examPerDef);
    }

    /**
     * 新增
     *
     * @param examPerDef examPerDef
     * @return R
     */
    @SysLog("新增")
    @RequestMapping("/save")
    @RequiresPermissions("tower:examperdef:save")
    public R save(@RequestBody ExamPerDefEntity examPerDef) {
        //判断占比是否超过100%
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("examStationId",examPerDef.getExamStationId());
        List<ExamPerDefEntity> list = examPerDefService.queryAll(params);
        Integer totalPercent = 0;
        for (ExamPerDefEntity entity:list
             ) {
            totalPercent += entity.getPercent();
            if (examPerDef.getQusStationId().equals(entity.getQusStationId())) {
                return R.error("该配置项已存在，请勿重复添加！");
            }
        }
        String examStationName = dictDefService.qryItemName("EXAM_STATION",examPerDef.getExamStationId());
        if(totalPercent+examPerDef.getPercent()>100){
            return R.error("职务：'"+examStationName+"'的考核占比剩余为："+(100-totalPercent)+",您填写的数额超过剩余，请调整！");
        }
        examPerDef.setExamStation(examStationName);
        examPerDef.setQusStation(dictDefService.qryItemName("EXEL_STATION",examPerDef.getQusStationId()));

        examPerDefService.add(examPerDef);

        return R.ok();
    }

    /**
     * 修改
     *
     * @param examPerDef examPerDef
     * @return R
     */
    @SysLog("修改")
    @RequestMapping("/update")
    @RequiresPermissions("tower:examperdef:update")
    public R update(@RequestBody ExamPerDefEntity examPerDef) {
        //判断占比是否超过100%
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("examStationId",examPerDef.getExamStationId());
        List<ExamPerDefEntity> list = examPerDefService.queryAll(params);
        Integer totalPercent = 0;
        for (ExamPerDefEntity entity:list
        ) {
            if(!examPerDef.getPercentId().equals(entity.getPercentId())){
                totalPercent += entity.getPercent();
            }
            if(!examPerDef.getPercentId().equals(entity.getPercentId())&&examPerDef.getQusStationId().equals(entity.getQusStationId())){
                return R.error("该配置项已存在，请勿重复添加！");
            }
        }
        String examStationName = dictDefService.qryItemName("EXAM_STATION",examPerDef.getExamStationId());
        if(totalPercent+examPerDef.getPercent()>100){
            return R.error("职务：'"+examStationName+"'的考核占比剩余为"+(100-totalPercent)+"，您填写的数额超过剩余，请调整！");
        }
        examPerDef.setExamStation(examStationName);
        examPerDef.setQusStation(dictDefService.qryItemName("EXEL_STATION",examPerDef.getQusStationId()));
        examPerDefService.update(examPerDef);

        return R.ok();
    }

    /**
     * 根据主键删除
     *
     * @param percentIds percentIds
     * @return R
     */
    @SysLog("删除")
    @RequestMapping("/delete")
    @RequiresPermissions("tower:examperdef:delete")
    public R delete(@RequestBody Integer[] percentIds) {
        examPerDefService.deleteBatch(percentIds);

        return R.ok();
    }
}
