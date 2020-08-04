/*
 * 类名称:TypeDefController.java
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
import com.ktkj.entity.TypeDefEntity;
import com.ktkj.service.TypeDefService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 信息分类Controller
 *
 * @author lipengjun
 * @date 2019-08-24 14:29:31
 */
@RestController
@RequestMapping("tokyo/typedef")
public class TypeDefController extends AbstractController {
    @Autowired
    private TypeDefService typeDefService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return R
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("tokyo:typedef:list")
    public R queryAll(@RequestParam Map<String, Object> params) {
        List<TypeDefEntity> list = typeDefService.queryAll(params);

        return R.ok().put("list", list);
    }

    /**
     * 分页查询信息分类
     *
     * @param params 查询参数
     * @return R
     */
    @GetMapping("/list")
    @RequiresPermissions("tokyo:typedef:list")
    public R list(@RequestParam Map<String, Object> params) {
        Page page = typeDefService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 根据主键查询详情
     *
     * @param typeId 主键
     * @return R
     */
    @RequestMapping("/info/{typeId}")
    @RequiresPermissions("tokyo:typedef:info")
    public R info(@PathVariable("typeId") Integer typeId) {
        TypeDefEntity typeDef = typeDefService.getById(typeId);

        return R.ok().put("typedef", typeDef);
    }

    /**
     * 新增信息分类
     *
     * @param typeDef typeDef
     * @return R
     */
    @SysLog("新增信息分类")
    @RequestMapping("/save")
    @RequiresPermissions("tokyo:typedef:save")
    public R save(@RequestBody TypeDefEntity typeDef) {

        typeDefService.add(typeDef);

        return R.ok();
    }

    /**
     * 修改信息分类
     *
     * @param typeDef typeDef
     * @return R
     */
    @SysLog("修改信息分类")
    @RequestMapping("/update")
    @RequiresPermissions("tokyo:typedef:update")
    public R update(@RequestBody TypeDefEntity typeDef) {

        typeDefService.update(typeDef);

        return R.ok();
    }

    /**
     * 根据主键删除信息分类
     *
     * @param typeIds typeIds
     * @return R
     */
    @SysLog("删除信息分类")
    @RequestMapping("/delete")
    @RequiresPermissions("tokyo:typedef:delete")
    public R delete(@RequestBody Integer[] typeIds) {
        typeDefService.deleteBatch(typeIds);

        return R.ok();
    }
}
