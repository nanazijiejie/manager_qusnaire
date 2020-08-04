/*
 * 类名称:SpecParamDefController.java
 * 包名称:com.ktkj.controller
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-08-27 16:57:03        lipengjun     初版做成
 *
 * Copyright (c) 2019-2019 厦门继续想科技有限公司
 */
package com.ktkj.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ktkj.annotation.SysLog;
import com.ktkj.utils.PageUtils;
import com.ktkj.utils.Query;
import com.ktkj.utils.R;
import com.ktkj.controller.AbstractController;
import com.ktkj.entity.SpecParamDefEntity;
import com.ktkj.service.SpecParamDefService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Controller
 *
 * @author lipengjun
 * @date 2019-08-27 16:57:03
 */
@RestController
@RequestMapping("/ring/specparamdef")
public class SpecParamDefController extends AbstractController {
    @Autowired
    private SpecParamDefService specParamDefService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return R
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("ring:specparamdef:list")
    public R queryAll(@RequestParam Map<String, Object> params) {
        List<SpecParamDefEntity> list = specParamDefService.queryAll(params);

        return R.ok().put("list", list);
    }

    /**
     * 分页查询
     *
     * @param params 查询参数
     * @return R
     */
    @GetMapping("/list")
    @RequiresPermissions("ring:specparamdef:list")
    public R list(@RequestParam Map<String, Object> params) throws Exception{
        Query query = new Query(params);
        if(params.get("specName")!=null){
            String specName = java.net.URLDecoder.decode(""+params.get("specName"));
            params.put("specName",specName);
        }
        Page page = specParamDefService.queryPage(params);

        PageUtils pageUtil = new PageUtils(page.getRecords(),
                Integer.parseInt(page.getTotal()+""),
                Integer.parseInt(page.getSize()+""),
                query.getPage());
        return R.ok().put("page", pageUtil);
    }

    /**
     * 根据主键查询详情
     *
     * @param specId 主键
     * @return R
     */
    @RequestMapping("/info/{specId}")
    @RequiresPermissions("ring:specparamdef:info")
    public R info(@PathVariable("specId") Integer specId) {
        SpecParamDefEntity specParamDef = specParamDefService.getById(specId);

        return R.ok().put("specParamDef", specParamDef);
    }

    /**
     * 新增
     *
     * @param specParamDef specParamDef
     * @return R
     */
    @SysLog("新增")
    @RequestMapping("/save")
    @RequiresPermissions("ring:specparamdef:save")
    public R save(@RequestBody SpecParamDefEntity specParamDef) {
        specParamDef.setCreateTime(new Date());
        specParamDefService.add(specParamDef);

        return R.ok();
    }

    /**
     * 修改
     *
     * @param specParamDef specParamDef
     * @return R
     */
    @SysLog("修改")
    @RequestMapping("/update")
    @RequiresPermissions("ring:specparamdef:update")
    public R update(@RequestBody SpecParamDefEntity specParamDef) {

        specParamDefService.update(specParamDef);

        return R.ok();
    }

    /**
     * 根据主键删除
     *
     * @param specIds specIds
     * @return R
     */
    @SysLog("删除")
    @RequestMapping("/delete")
    @RequiresPermissions("ring:specparamdef:delete")
    public R delete(@RequestBody Integer[] specIds) {
        specParamDefService.deleteBatch(specIds);

        return R.ok();
    }
}
