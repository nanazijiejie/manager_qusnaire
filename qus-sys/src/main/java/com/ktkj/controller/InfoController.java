/*
 * 类名称:InfoController.java
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
import com.ktkj.entity.InfoEntity;
import com.ktkj.service.InfoService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller
 *
 * @author lipengjun
 * @date 2019-08-24 14:29:31
 */
@RestController
@RequestMapping("tokyo/info")
public class InfoController extends AbstractController {
    @Autowired
    private InfoService infoService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return R
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("tokyo:info:list")
    public R queryAll(@RequestParam Map<String, Object> params) {
        List<InfoEntity> list = infoService.queryAll(params);

        return R.ok().put("list", list);
    }

    /**
     * 分页查询
     *
     * @param params 查询参数
     * @return R
     */
    @GetMapping("/list")
    @RequiresPermissions("tokyo:info:list")
    public R list(@RequestParam Map<String, Object> params) {
        Page page = infoService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 根据主键查询详情
     *
     * @param infoId 主键
     * @return R
     */
    @RequestMapping("/info/{infoId}")
    @RequiresPermissions("tokyo:info:info")
    public R info(@PathVariable("infoId") Integer infoId) {
        InfoEntity info = infoService.getById(infoId);

        return R.ok().put("info", info);
    }

    /**
     * 新增
     *
     * @param info info
     * @return R
     */
    @SysLog("新增")
    @RequestMapping("/save")
    @RequiresPermissions("tokyo:info:save")
    public R save(@RequestBody InfoEntity info) {

        infoService.add(info);

        return R.ok();
    }

    /**
     * 修改
     *
     * @param info info
     * @return R
     */
    @SysLog("修改")
    @RequestMapping("/update")
    @RequiresPermissions("tokyo:info:update")
    public R update(@RequestBody InfoEntity info) {

        infoService.update(info);

        return R.ok();
    }

    /**
     * 根据主键删除
     *
     * @param infoIds infoIds
     * @return R
     */
    @SysLog("删除")
    @RequestMapping("/delete")
    @RequiresPermissions("tokyo:info:delete")
    public R delete(@RequestBody Integer[] infoIds) {
        infoService.deleteBatch(infoIds);

        return R.ok();
    }
}
