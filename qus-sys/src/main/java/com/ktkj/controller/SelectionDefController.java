/*
 * 类名称:SelectionInfoController.java
 * 包名称:com.ktkj.controller
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2020-02-21 22:35:28        lipengjun     初版做成
 *
 * Copyright (c) 2019-2019 厦门继续想科技有限公司
 */
package com.ktkj.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ktkj.annotation.SysLog;
import com.ktkj.entity.SelectionDefEntity;
import com.ktkj.entity.SelectionInfoEntity;
import com.ktkj.service.SelectionDefService;
import com.ktkj.utils.PageUtils;
import com.ktkj.utils.R;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Controller
 *
 * @author lipengjun
 * @date 2020-02-21 22:35:28
 */
@RestController
@RequestMapping("/tower/selectiondef")
public class SelectionDefController extends AbstractController {
    @Autowired
    private SelectionDefService selectionDefService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return R
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("tower:selectiondef:list")
    public R queryAll(@RequestParam Map<String, Object> params) {
        List<SelectionDefEntity> list = selectionDefService.queryAll(params);

        return R.ok().put("list", list);
    }

    /**
     * 分页查询
     *
     * @param params 查询参数
     * @return R
     */
    @GetMapping("/list")
    @RequiresPermissions("tower:selectiondef:list")
    public R list(@RequestParam Map<String, Object> params) {
        if (params.get("selCityId") != null) {
            String selCityId = java.net.URLDecoder.decode("" + params.get("selCityId"));
            params.put("selCityId", selCityId);
        }
        Page page = selectionDefService.queryPage(params);
        PageUtils pageUtil = new PageUtils(page.getRecords(), (int) page.getTotal(), (int) page.getSize(), (int) page.getCurrent());
        return R.ok().put("page", pageUtil);
    }

    /**
     * 获取归属单位/地市
     *
     * @return R
     */
    @GetMapping("/querySelCity")
    @RequiresPermissions("tower:selectiondef:selcity")
    public R querySelCity() {
        return R.ok().put("list", selectionDefService.querySelCity());
    }

    /**
     * 根据主键查询详情
     *
     * @param selId 主键
     * @return R
     */
    @RequestMapping("/info/{selId}")
    @RequiresPermissions("tower:selectiondef:info")
    public R info(@PathVariable("selId") Integer selId) {
        SelectionDefEntity selectionDefEntity = selectionDefService.getById(selId);

        return R.ok().put("selectionDef", selectionDefEntity);
    }

    /**
     * 新增
     *
     * @param
     * @return R
     */
    @SysLog("新增")
    @RequestMapping("/save")
    @RequiresPermissions("tower:selectiondef:save")
    public R save(@RequestBody SelectionDefEntity selectionDefEntity) {

        selectionDefService.add(selectionDefEntity);

        return R.ok();
    }

    /**
     * 修改
     *
     * @param selectionDefEntity
     * @return R
     */
    @SysLog("修改")
    @RequestMapping("/update")
    @RequiresPermissions("tower:selectiondef:update")
    public R update(@RequestBody SelectionDefEntity selectionDefEntity) {

        selectionDefService.update(selectionDefEntity);

        return R.ok();
    }

    /**
     * 根据主键删除
     *
     * @param selIds selIds
     * @return R
     */
    @SysLog("删除")
    @RequestMapping("/delete")
    @RequiresPermissions("tower:selectiondef:delete")
    public R delete(@RequestBody Integer[] selIds) {
        selectionDefService.deleteBatch(selIds);

        return R.ok();
    }


}
