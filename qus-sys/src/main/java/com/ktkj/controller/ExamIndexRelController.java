/*
 * 类名称:ExamIndexRelController.java
 * 包名称:com.ktkj.controller
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-10-09 15:21:52        lipengjun     初版做成
 *
 * Copyright (c) 2019-2019 厦门继续想科技有限公司
 */
package com.ktkj.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ktkj.annotation.SysLog;
import com.ktkj.utils.R;
import com.ktkj.controller.AbstractController;
import com.ktkj.entity.ExamIndexRelEntity;
import com.ktkj.service.ExamIndexRelService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Controller
 *
 * @author lipengjun
 * @date 2019-10-09 15:21:52
 */
@RestController
@RequestMapping("/tower/examindexrel")
public class ExamIndexRelController extends AbstractController {
    @Autowired
    private ExamIndexRelService examIndexRelService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return R
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("tower:examindexrel:list")
    public R queryAll(@RequestParam Map<String, Object> params) {
        List<ExamIndexRelEntity> list = examIndexRelService.queryAll(params);

        return R.ok().put("list", list);
    }

    /**
     * 分页查询
     *
     * @param params 查询参数
     * @return R
     */
    /*@GetMapping("/list")
    @RequiresPermissions("tower:examindexrel:list")
    public R list(@RequestParam Map<String, Object> params) {
        Query query = new Query(params);
        Page page = examIndexRelService.queryPage(params);
        PageUtils pageUtil = new PageUtils(page.getRecords(),
                Integer.parseInt(page.getTotal()+""),
                Integer.parseInt(page.getSize()+""),
                query.getPage());
        return R.ok().put("page", pageUtil);
    }*/

    /**
     * 根据主键查询详情
     *
     * @param examItemId 主键
     * @return R
     */
    @RequestMapping("/info/{examItemId}")
    @RequiresPermissions("tower:examindexrel:info")
    public R info(@PathVariable("examItemId") Integer examItemId) {
        ExamIndexRelEntity examIndexRel = examIndexRelService.getById(examItemId);

        return R.ok().put("examindexrel", examIndexRel);
    }

    /**
     * 新增
     * @param request
     * @param response
     * @return
     */
    //@SysLog("新增")
    @RequestMapping("/save")
    @ResponseBody
    //@RequiresPermissions("tower:examindexrel:save")
    public R save(HttpServletRequest request,
                  HttpServletResponse response) {
        List<ExamIndexRelEntity> retList = new ArrayList<ExamIndexRelEntity>();
        String indexItemListStr = ""+request.getParameter("indexItemListStr");
        List<ExamIndexRelEntity> list = JSON.parseArray(indexItemListStr,ExamIndexRelEntity.class);
        for (ExamIndexRelEntity examIndexRelEntity:list
             ) {
            examIndexRelEntity.setCreateTime(new Date());
            examIndexRelEntity.setCreateOperator(getUser().getUsername());
            if(examIndexRelEntity.getPercent()!=null&&!"0".equals(examIndexRelEntity.getPercent())){
                retList.add(examIndexRelEntity);
            }
        }
        examIndexRelService.addList(list);
        return R.ok();
    }

    /**
     * 新增
     * @param request
     * @param response
     * @return
     */
   // @SysLog("修改")
    @RequestMapping("/update")
    @ResponseBody
    //@RequiresPermissions("tower:examindexrel:update")
    public R update(HttpServletRequest request,
                    HttpServletResponse response) {
        String indexItemListStr = ""+request.getParameter("indexItemListStr");
        List<ExamIndexRelEntity> retList = new ArrayList<ExamIndexRelEntity>();
        List<ExamIndexRelEntity> list = JSON.parseArray(indexItemListStr,ExamIndexRelEntity.class);
        for (ExamIndexRelEntity examIndexRelEntity:list
        ) {
            examIndexRelEntity.setCreateTime(new Date());
            examIndexRelEntity.setCreateOperator(getUser().getUsername());
            if(examIndexRelEntity.getPercent()!=null&&!"0".equals(examIndexRelEntity.getPercent())){
                retList.add(examIndexRelEntity);
            }
        }
        examIndexRelService.addList(retList);
        return R.ok();
    }

    /**
     * 根据主键删除
     *
     * @param examItemIds examItemIds
     * @return R
     */
    @SysLog("删除")
    @RequestMapping("/delete")
    @RequiresPermissions("tower:examindexrel:delete")
    public R delete(@RequestBody Integer[] examItemIds) {
        examIndexRelService.deleteBatch(examItemIds);

        return R.ok();
    }
}
