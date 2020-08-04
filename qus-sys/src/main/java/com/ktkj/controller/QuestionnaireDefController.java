/*
 * 类名称:QuestionnaireDefController.java
 * 包名称:com.ktkj.controller
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-10-09 15:21:52        lipengjun     初版做成
 *
 * Copyright (c) 2019-2019 厦门继续想科技有限公司
 */
package com.ktkj.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ktkj.annotation.SysLog;
import com.ktkj.service.DictDefService;
import com.ktkj.utils.PageUtils;
import com.ktkj.utils.Query;
import com.ktkj.utils.R;
import com.ktkj.controller.AbstractController;
import com.ktkj.entity.QuestionnaireDefEntity;
import com.ktkj.service.QuestionnaireDefService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller
 *
 * @author lipengjun
 * @date 2019-10-09 15:21:52
 */
@RestController
@RequestMapping("/tower/questionnairedef")
public class QuestionnaireDefController extends AbstractController {
    @Autowired
    private QuestionnaireDefService questionnaireDefService;
    @Autowired
    private DictDefService dictDefService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return R
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("tower:questionnairedef:list")
    public R queryAll(@RequestParam Map<String, Object> params) {
        List<QuestionnaireDefEntity> list = questionnaireDefService.queryAll(params);

        return R.ok().put("list", list);
    }

    /**
     * 分页查询
     *
     * @param params 查询参数
     * @return R
     */
    @GetMapping("/list")
    @RequiresPermissions("tower:questionnairedef:list")
    public R list(@RequestParam Map<String, Object> params) {
        Page page = questionnaireDefService.queryPage(params);
        PageUtils pageUtil = new PageUtils(page.getRecords(), (int)page.getTotal(), (int)page.getSize(), (int)page.getCurrent());
        return R.ok().put("page", pageUtil);
    }

    /**
     * 根据主键查询详情
     *
     * @param qusNaireId 主键
     * @return R
     */
    @RequestMapping("/info/{qusNaireId}")
    @RequiresPermissions("tower:questionnairedef:info")
    public R info(@PathVariable("qusNaireId") Integer qusNaireId) {
        QuestionnaireDefEntity questionnaireDef = questionnaireDefService.getById(qusNaireId);

        return R.ok().put("questionnaireDef", questionnaireDef);
    }

    /**
     * 新增
     *
     * @param questionnaireDef questionnaireDef
     * @return R
     */
    @SysLog("新增")
    @RequestMapping("/save")
    @RequiresPermissions("tower:questionnairedef:save")
    public R save(@RequestBody QuestionnaireDefEntity questionnaireDef) {
        //qusNaireStationId
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("qusNaireStationId",questionnaireDef.getQusNaireStationId());
        List<QuestionnaireDefEntity> list = questionnaireDefService.queryAll(params);
        String stationName = dictDefService.qryItemName("QUS_STATION",questionnaireDef.getQusNaireStationId());
        if(list!=null&&list.size()!=0){
            return R.error("职务：'"+stationName+"'"+"已存在对应的填写问卷，请修改，勿重复添加!");
        }
        questionnaireDef.setCreateTime(new Date());
        questionnaireDef.setCreateOperator(getUser().getUsername());
        questionnaireDef.setQusNaireStation(stationName);
        questionnaireDefService.add(questionnaireDef);

        return R.ok().put("qusNaireId",questionnaireDef.getQusNaireId());
    }

    /**
     * 修改
     *
     * @param questionnaireDef questionnaireDef
     * @return R
     */
    @SysLog("修改")
    @RequestMapping("/update")
    @RequiresPermissions("tower:questionnairedef:update")
    public R update(@RequestBody QuestionnaireDefEntity questionnaireDef) {
        questionnaireDef.setUpdateTime(new Date());
        questionnaireDef.setUpdateOperator(getUser().getUsername());
        questionnaireDef.setQusNaireStation(dictDefService.qryItemName("QUS_STATION",questionnaireDef.getQusNaireStationId()));

        questionnaireDefService.update(questionnaireDef);

        return R.ok().put("qusNaireId",questionnaireDef.getQusNaireId());
    }

    /**
     * 根据主键删除
     *
     * @param qusNaireIds qusNaireIds
     * @return R
     */
    @SysLog("删除")
    @RequestMapping("/delete")
    @RequiresPermissions("tower:questionnairedef:delete")
    public R delete(@RequestBody Integer[] qusNaireIds) {
        questionnaireDefService.deleteBatch(qusNaireIds);

        return R.ok();
    }
}
