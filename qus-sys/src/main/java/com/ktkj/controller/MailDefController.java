/*
 * 类名称:MailDefController.java
 * 包名称:com.ktkj.controller
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-10-17 20:33:03        lipengjun     初版做成
 *
 * Copyright (c) 2019-2019 厦门继续想科技有限公司
 */
package com.ktkj.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ktkj.annotation.SysLog;
import com.ktkj.utils.Constant;
import com.ktkj.utils.PageUtils;
import com.ktkj.utils.Query;
import com.ktkj.utils.R;
import com.ktkj.controller.AbstractController;
import com.ktkj.entity.MailDefEntity;
import com.ktkj.service.MailDefService;
import org.apache.commons.lang.StringUtils;
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
 * @date 2019-10-17 20:33:03
 */
@RestController
@RequestMapping("/tower/maildef")
public class MailDefController extends AbstractController {
    @Autowired
    private MailDefService mailDefService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return R
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("tower:maildef:list")
    public R queryAll(@RequestParam Map<String, Object> params) {
        List<MailDefEntity> list = mailDefService.queryAll(params);

        return R.ok().put("list", list);
    }

    /**
     * 分页查询
     *
     * @param params 查询参数
     * @return R
     */
    @GetMapping("/list")
    @RequiresPermissions("tower:maildef:list")
    public R list(@RequestParam Map<String, Object> params) {
        if(params.get("mailTitle")!=null){
            String staffName = java.net.URLDecoder.decode(""+params.get("mailTitle"));
            params.put("mailTitle",staffName);
        }
        Query query = new Query(params);
        List<MailDefEntity> list = mailDefService.queryAll(params);
        PageUtils pageUtil = new PageUtils(
                list, list.size(), query.getLimit(), query.getPage());
        return R.ok().put("page", pageUtil);
    }

    /**
     * 根据主键查询详情
     *
     * @param mailId 主键
     * @return R
     */
    @RequestMapping("/info/{mailId}")
    @RequiresPermissions("tower:maildef:info")
    public R info(@PathVariable("mailId") Integer mailId) {
        MailDefEntity mailDef = mailDefService.getById(mailId);

        return R.ok().put("mailDef", mailDef);
    }

    /**
     * 新增
     *
     * @param mailDef mailDef
     * @return R
     */
    @SysLog("新增")
    @RequestMapping("/save")
    @RequiresPermissions("tower:maildef:save")
    public R save(@RequestBody MailDefEntity mailDef) {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("delFlag","0");
        List<MailDefEntity> list = mailDefService.queryAll(params);
        if(list!=null&&list.size()!=0&&"0".equals(mailDef.getDelFlag())){
            return R.error("已存在'在用'记录，请修改或删除后再添加！");
        }
        mailDefService.add(mailDef);

        return R.ok();
    }

    /**
     * 修改
     *
     * @param mailDef mailDef
     * @return R
     */
    @SysLog("修改")
    @RequestMapping("/update")
    @RequiresPermissions("tower:maildef:update")
    public R update(@RequestBody MailDefEntity mailDef) {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("delFlag","0");
        List<MailDefEntity> list = mailDefService.queryAll(params);
        if(list!=null&&list.size()!=0&&"0".equals(mailDef.getDelFlag())&&!mailDef.getMailId().equals(list.get(0).getMailId())){
            return R.error("已存在'在用'记录，请修改或删除后再添加！");
        }
        mailDefService.update(mailDef);

        return R.ok();
    }

    /**
     * 根据主键删除
     *
     * @param mailIds mailIds
     * @return R
     */
    @SysLog("删除")
    @RequestMapping("/delete")
    @RequiresPermissions("tower:maildef:delete")
    public R delete(@RequestBody Integer[] mailIds) {
        mailDefService.deleteBatch(mailIds);

        return R.ok();
    }
}
