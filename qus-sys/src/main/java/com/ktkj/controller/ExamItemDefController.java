/*
 * 类名称:ExamItemDefController.java
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
import com.ktkj.entity.ExamItemDefEntity;
import com.ktkj.service.ExamItemDefService;
import com.ktkj.utils.StringUtils;
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
@RequestMapping("/tower/examitemdef")
public class ExamItemDefController extends AbstractController {
    @Autowired
    private ExamItemDefService examItemDefService;
    @Autowired
    private DictDefService dictDefService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return R
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("tower:examitemdef:list")
    public R queryAll(@RequestParam Map<String, Object> params) {
        List<ExamItemDefEntity> list = examItemDefService.queryAll(params);

        return R.ok().put("list", list);
    }

    /**
     * 分页查询
     *
     * @param params 查询参数
     * @return R
     */
    @GetMapping("/list")
    @RequiresPermissions("tower:examitemdef:list")
    public R list(@RequestParam Map<String, Object> params) {
        /*if(params.get("indexItemName")!=null){
            String indexItemName = java.net.URLDecoder.decode(""+params.get("indexItemName"));
            params.put("indexItemName",indexItemName);
        }*/
        Query query = new Query(params);
        List<ExamItemDefEntity> list = examItemDefService.queryAll(params);
        PageUtils pageUtil = new PageUtils(
                list, list.size(), query.getLimit(), query.getPage());
        return R.ok().put("page", pageUtil);
    }

    /**
     * 根据主键查询详情
     *
     * @param examItemId 主键
     * @return R
     */
    @RequestMapping("/info/{examItemId}")
    @RequiresPermissions("tower:examitemdef:info")
    public R info(@PathVariable("examItemId") Integer examItemId) {
        ExamItemDefEntity examItemDef = examItemDefService.getById(examItemId);

        return R.ok().put("examItemDef", examItemDef);
    }

    /**
     * 新增
     *
     * @param examItemDef examItemDef
     * @return R
     */
    @SysLog("新增")
    @RequestMapping("/save")
    @RequiresPermissions("tower:examitemdef:save")
    public R save(@RequestBody ExamItemDefEntity examItemDef) {
        //先判断职务对应的考核项是否存在，不能重复添加
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("examStationId",examItemDef.getExamStationId());
        //List<ExamItemDefEntity> list = examItemDefService.queryAll(params);
        /*if(list!=null&&list.size()!=0){
            return R.error("职务：'"+stationName+"'对应的考核项已存在配置，请修改，勿重复添加！");
        }*/
        examItemDef.setCreateTime(new Date());
        examItemDef.setCreateOperator(getUser().getUsername());
        if(StringUtils.isNotEmpty(examItemDef.getExamStationId())){
            String stationName = dictDefService.qryItemName("EXAM_STATION",examItemDef.getExamStationId());
            examItemDef.setExamStation(stationName);
        }
        examItemDefService.add(examItemDef,null);
        return R.ok().put("examItemId",examItemDef.getExamItemId());
    }

    /**
     * 修改
     *
     * @param examItemDef examItemDef
     * @return R
     */
    @SysLog("修改")
    @RequestMapping("/update")
    @RequiresPermissions("tower:examitemdef:update")
    public R update(@RequestBody ExamItemDefEntity examItemDef) {
        examItemDef.setUpdateTime(new Date());
        examItemDef.setUpdateOperator(getUser().getUsername());
        if(StringUtils.isNotEmpty(examItemDef.getExamStationId())){
            examItemDef.setExamStation(dictDefService.qryItemName("EXAM_STATION",examItemDef.getExamStationId()));
        }
        examItemDefService.update(examItemDef);

        return R.ok().put("examItemId",examItemDef.getExamItemId());
    }

    /**
     * 根据主键删除
     *
     * @param examItemIds examItemIds
     * @return R
     */
    @SysLog("删除")
    @RequestMapping("/delete")
    @RequiresPermissions("tower:examitemdef:delete")
    public R delete(@RequestBody Integer[] examItemIds) {
        examItemDefService.deleteBatch(examItemIds);

        return R.ok();
    }
}
