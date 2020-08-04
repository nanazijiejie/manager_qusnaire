/*
 * 类名称:StaffDeptRelController.java
 * 包名称:com.ktkj.controller
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-10-14 15:00:32        lipengjun     初版做成
 *
 * Copyright (c) 2019-2019 厦门继续想科技有限公司
 */
package com.ktkj.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ktkj.annotation.SysLog;
import com.ktkj.utils.R;
import com.ktkj.controller.AbstractController;
import com.ktkj.entity.StaffDeptRelEntity;
import com.ktkj.service.StaffDeptRelService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller
 *
 * @author lipengjun
 * @date 2019-10-14 15:00:32
 */
@RestController
@RequestMapping("/tower/staffdeptrel")
public class StaffDeptRelController extends AbstractController {
    @Autowired
    private StaffDeptRelService staffDeptRelService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return R
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("tower:staffdeptrel:list")
    public R queryAll(@RequestParam Map<String, Object> params) {
        List<StaffDeptRelEntity> list = staffDeptRelService.queryAll(params);

        return R.ok().put("list", list);
    }

    /**
     * 分页查询
     *
     * @param params 查询参数
     * @return R
     */
    /*@GetMapping("/list")
    @RequiresPermissions("tower:staffdeptrel:list")
    public R list(@RequestParam Map<String, Object> params) {
        Query query = new Query(params);
        Page page = staffDeptRelService.queryPage(params);
        PageUtils pageUtil = new PageUtils(page.getRecords(),
                Integer.parseInt(page.getTotal()+""),
                Integer.parseInt(page.getSize()+""),
                query.getPage());
        return R.ok().put("page", pageUtil);
    }*/

    /**
     * 根据主键查询详情
     *
     * @param id 主键
     * @return R
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("tower:staffdeptrel:info")
    public R info(@PathVariable("id") Integer id) {
        StaffDeptRelEntity staffDeptRel = staffDeptRelService.getById(id);

        return R.ok().put("staffdeptrel", staffDeptRel);
    }

    /**
     * 新增
     *
     * @param staffDeptRel staffDeptRel
     * @return R
     */
    @SysLog("新增")
    @RequestMapping("/save")
    @RequiresPermissions("tower:staffdeptrel:save")
    public R save(@RequestBody StaffDeptRelEntity staffDeptRel) {

        staffDeptRelService.add(staffDeptRel);

        return R.ok();
    }

    /**
     * 修改
     *
     * @param staffDeptRel staffDeptRel
     * @return R
     */
    @SysLog("修改")
    @RequestMapping("/update")
    @RequiresPermissions("tower:staffdeptrel:update")
    public R update(@RequestBody StaffDeptRelEntity staffDeptRel) {

        staffDeptRelService.update(staffDeptRel);

        return R.ok();
    }

    /**
     * 根据主键删除
     *
     * @param ids ids
     * @return R
     */
    @SysLog("删除")
    @RequestMapping("/delete")
    @RequiresPermissions("tower:staffdeptrel:delete")
    public R delete(@RequestBody Integer[] ids) {
        staffDeptRelService.deleteBatch(ids);

        return R.ok();
    }
}
