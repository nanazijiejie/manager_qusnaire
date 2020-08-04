/*
 * 类名称:AdInfoController.java
 * 包名称:com.ktkj.controller
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-08-24 14:29:31        lipengjun     初版做成
 *
 * Copyright (c) 2019-2019 厦门继续想科技有限公司
 */
package com.ktkj.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ktkj.annotation.SysLog;
import com.ktkj.utils.R;
import com.ktkj.controller.AbstractController;
import com.ktkj.entity.AdInfoEntity;
import com.ktkj.service.AdInfoService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 广告信息表Controller
 *
 * @author lipengjun
 * @date 2019-08-24 14:29:31
 */
@RestController
@RequestMapping("tokyo/adinfo")
public class AdInfoController extends AbstractController {
    @Autowired
    private AdInfoService adInfoService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return R
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("tokyo:adinfo:list")
    public R queryAll(@RequestParam Map<String, Object> params) {
        List<AdInfoEntity> list = adInfoService.queryAll(params);

        return R.ok().put("list", list);
    }

    /**
     * 分页查询广告信息表
     *
     * @param params 查询参数
     * @return R
     */
    @GetMapping("/list")
    @RequiresPermissions("tokyo:adinfo:list")
    public R list(@RequestParam Map<String, Object> params) {
        Page page = adInfoService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 根据主键查询详情
     *
     * @param addId 主键
     * @return R
     */
    @RequestMapping("/info/{addId}")
    @RequiresPermissions("tokyo:adinfo:info")
    public R info(@PathVariable("addId") Integer addId) {
        AdInfoEntity adInfo = adInfoService.getById(addId);

        return R.ok().put("adinfo", adInfo);
    }

    /**
     * 新增广告信息表
     *
     * @param adInfo adInfo
     * @return R
     */
    @SysLog("新增广告信息表")
    @RequestMapping("/save")
    @RequiresPermissions("tokyo:adinfo:save")
    public R save(@RequestBody AdInfoEntity adInfo) {

        adInfoService.add(adInfo);

        return R.ok();
    }

    /**
     * 修改广告信息表
     *
     * @param adInfo adInfo
     * @return R
     */
    @SysLog("修改广告信息表")
    @RequestMapping("/update")
    @RequiresPermissions("tokyo:adinfo:update")
    public R update(@RequestBody AdInfoEntity adInfo) {

        adInfoService.update(adInfo);

        return R.ok();
    }

    /**
     * 根据主键删除广告信息表
     *
     * @param addIds addIds
     * @return R
     */
    @SysLog("删除广告信息表")
    @RequestMapping("/delete")
    @RequiresPermissions("tokyo:adinfo:delete")
    public R delete(@RequestBody Integer[] addIds) {
        adInfoService.deleteBatch(addIds);

        return R.ok();
    }
}
