/*
 * 类名称:QusItemRelController.java
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
import com.ktkj.utils.R;
import com.ktkj.controller.AbstractController;
import com.ktkj.entity.QusItemRelEntity;
import com.ktkj.service.QusItemRelService;
import org.apache.commons.lang.StringUtils;
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
@RequestMapping("/tower/qusitemrel")
public class QusItemRelController extends AbstractController {
    @Autowired
    private QusItemRelService qusItemRelService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return R
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("tower:qusitemrel:list")
    public R queryAll(@RequestParam Map<String, Object> params) {
        List<QusItemRelEntity> list = qusItemRelService.queryAll(params);

        return R.ok().put("list", list);
    }

    /**
     * 分页查询
     *
     * @param params 查询参数
     * @return R
     */
    /*@GetMapping("/list")
    @RequiresPermissions("tower:qusitemrel:list")
    public R list(@RequestParam Map<String, Object> params) {
        Query query = new Query(params);
        Page page = qusItemRelService.queryPage(params);
        PageUtils pageUtil = new PageUtils(page.getRecords(),
                Integer.parseInt(page.getTotal()+""),
                Integer.parseInt(page.getSize()+""),
                query.getPage());
        return R.ok().put("page", pageUtil);
    }*/

    /**
     * 根据主键查询详情
     *
     * @param qusNaireId 主键
     * @return R
     */
    @RequestMapping("/info/{qusNaireId}")
    @RequiresPermissions("tower:qusitemrel:info")
    public R info(@PathVariable("qusNaireId") Integer qusNaireId) {
        List<QusItemRelEntity> qusItemRelList = qusItemRelService.selectByQusNaireId(qusNaireId);
        return R.ok().put("list", qusItemRelList);
    }

    /**
     * 新增
     *
     * @param qusItemRel qusItemRel
     * @return R
     */
    @SysLog("新增")
    @RequestMapping("/save")
    @RequiresPermissions("tower:qusitemrel:save")
    public R save(@RequestBody QusItemRelEntity qusItemRel) {

        qusItemRelService.add(qusItemRel);

        return R.ok();
    }

    /**
     * 批量保存
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/saveBatch")
    @ResponseBody
    //@RequiresPermissions("tower:examindexrel:save")
    public R save(HttpServletRequest request,
                  HttpServletResponse response) {
        List<QusItemRelEntity> list = new ArrayList<QusItemRelEntity>();
        String qusNaireId = request.getParameter("qusNaireId");//问卷ID
        String examItemList = request.getParameter("examItemList");
        if(StringUtils.isEmpty(examItemList)) return R.ok();
        String[]examItemArr = examItemList.split(",");
        for (String str:examItemArr
             ) {
            if(StringUtils.isEmpty(str)||"undefined".equals(str)){
                continue;
            }
            QusItemRelEntity entity = new QusItemRelEntity();
            entity.setExamItemId(Integer.valueOf(str));
            entity.setQusNaireId(Integer.valueOf(qusNaireId));
            entity.setCreateTime(new Date());
            entity.setCreateOperator(getUser().getUsername());
            list.add(entity);
        }
        if(list!=null&&list.size()!=0){
            qusItemRelService.addList(list);
        }
        return R.ok();
    }

    /**
     * 修改
     *
     * @param qusItemRel qusItemRel
     * @return R
     */
    @SysLog("修改")
    @RequestMapping("/update")
    @RequiresPermissions("tower:qusitemrel:update")
    public R update(@RequestBody QusItemRelEntity qusItemRel) {

        qusItemRelService.update(qusItemRel);

        return R.ok();
    }

    /**
     * 根据主键删除
     *
     * @param qusNaireIds qusNaireIds
     * @return R
     */
    @SysLog("删除")
    @RequestMapping("/delete")
    @RequiresPermissions("tower:qusitemrel:delete")
    public R delete(@RequestBody Integer[] qusNaireIds) {
        qusItemRelService.deleteBatch(qusNaireIds);

        return R.ok();
    }
}
