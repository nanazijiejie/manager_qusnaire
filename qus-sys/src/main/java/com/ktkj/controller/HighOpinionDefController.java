/*
 * 类名称:HighOpinionDefController.java
 * 包名称:com.ktkj.controller
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-08-27 16:57:02        lipengjun     初版做成
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
import com.ktkj.entity.HighOpinionDefEntity;
import com.ktkj.service.HighOpinionDefService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Date;

/**
 * Controller
 *
 * @author lipengjun
 * @date 2019-08-27 16:57:02
 */
@RestController
@RequestMapping("/ring/highopiniondef")
public class HighOpinionDefController extends AbstractController {
    @Autowired
    private HighOpinionDefService highOpinionDefService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return R
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("ring:highopiniondef:list")
    public R queryAll(@RequestParam Map<String, Object> params) {
        List<HighOpinionDefEntity> list = highOpinionDefService.queryAll(params);

        return R.ok().put("list", list);
    }

    /**
     * 分页查询
     *
     * @param params 查询参数
     * @return R
     */
    @GetMapping("/list")
    @RequiresPermissions("ring:highopiniondef:list")
    public R list(@RequestParam Map<String, Object> params) {
        Query query = new Query(params);
        Page page = highOpinionDefService.queryPage(params);

        PageUtils pageUtil = new PageUtils(page.getRecords(),
                Integer.parseInt(page.getTotal()+""),
                Integer.parseInt(page.getSize()+""),
                query.getPage());
        return R.ok().put("page", pageUtil);
    }

    /**
     * 根据主键查询详情
     *
     * @param opinionId 主键
     * @return R
     */
    @RequestMapping("/info/{opinionId}")
    @RequiresPermissions("ring:highopiniondef:info")
    public R info(@PathVariable("opinionId") Integer opinionId) {
        HighOpinionDefEntity highOpinionDef = highOpinionDefService.getById(opinionId);

        return R.ok().put("highOpinionDef", highOpinionDef);
    }

    /**
     * 新增
     *
     * @param highOpinionDef highOpinionDef
     * @return R
     */
    @SysLog("新增")
    @RequestMapping("/save")
    @RequiresPermissions("ring:highopiniondef:save")
    public R save(@RequestBody HighOpinionDefEntity highOpinionDef) {
        highOpinionDef.setCreateTime(new Date());
        highOpinionDefService.add(highOpinionDef);

        return R.ok();
    }

    /**
     * 修改
     *
     * @param highOpinionDef highOpinionDef
     * @return R
     */
    @SysLog("修改")
    @RequestMapping("/update")
    @RequiresPermissions("ring:highopiniondef:update")
    public R update(@RequestBody HighOpinionDefEntity highOpinionDef) {

        highOpinionDefService.update(highOpinionDef);
        return R.ok();
    }

    /**
     * 根据主键删除
     *
     * @param opinionIds opinionIds
     * @return R
     */
    @SysLog("删除")
    @RequestMapping("/delete")
    @RequiresPermissions("ring:highopiniondef:delete")
    public R delete(@RequestBody Integer[] opinionIds) {
        highOpinionDefService.deleteBatch(opinionIds);

        return R.ok();
    }
}
