/*
 * 类名称:GoodsDefController.java
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
import com.ktkj.entity.GoodsGalleryEntity;
import com.ktkj.entity.UserEntity;
import com.ktkj.utils.PageUtils;
import com.ktkj.utils.Query;
import com.ktkj.utils.R;
import com.ktkj.controller.AbstractController;
import com.ktkj.entity.GoodsDefEntity;
import com.ktkj.service.GoodsDefService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Controller
 *
 * @author lipengjun
 * @date 2019-08-27 16:57:02
 */
@RestController
@RequestMapping("/ring/goodsdef")
public class GoodsDefController extends AbstractController {
    @Autowired
    private GoodsDefService goodsDefService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return R
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("ring:goodsdef:list")
    public R queryAll(@RequestParam Map<String, Object> params) {
        List<GoodsDefEntity> list = goodsDefService.queryAll(params);

        return R.ok().put("list", list);
    }

    /**
     * 分页查询
     *
     * @param params 查询参数
     * @return R
     */
    @GetMapping("/list")
    @RequiresPermissions("ring:goodsdef:list")
    public R list(@RequestParam Map<String, Object> params) {
        Query query = new Query(params);
        Page page = goodsDefService.queryPage(params);
        //List<?> list, int totalCount, int pageSize, int currPage
        PageUtils pageUtil = new PageUtils(page.getRecords(),
                Integer.parseInt(page.getTotal()+""),
                Integer.parseInt(page.getSize()+""),
                query.getPage());
        return R.ok().put("page", pageUtil);
    }

    /**
     * 根据主键查询详情
     *
     * @param goodsId 主键
     * @return R
     */
    @RequestMapping("/info/{goodsId}")
    @RequiresPermissions("ring:goodsdef:info")
    public R info(@PathVariable("goodsId") Integer goodsId) {
        GoodsDefEntity goodsDef = goodsDefService.getById(goodsId);
        List<GoodsGalleryEntity> list =  new ArrayList<GoodsGalleryEntity>();
        String[] dtlPath = goodsDef.getGoodsPicDtlPath().split(",");
        for (String pathItem:dtlPath) {
            GoodsGalleryEntity goodsGalleryEntity = new GoodsGalleryEntity();
            goodsGalleryEntity.setImgUrl(pathItem);
            list.add(goodsGalleryEntity);
        }
        return R.ok().put("goodsDef", goodsDef).put("uploadList", list);
    }

    /**
     * 新增
     *
     * @param goodsDef goodsDef
     * @return R
     */
    @SysLog("新增")
    @RequestMapping("/save")
    @RequiresPermissions("ring:goodsdef:save")
    public R save(@RequestBody GoodsDefEntity goodsDef) {
        goodsDef.setGoodsPrice(goodsDef.getGoodsPrice()*100);
        goodsDef.setPreGoodsPrice(goodsDef.getPreGoodsPrice()*100);
        goodsDef.setCreateTime(new Date());
        goodsDefService.add(goodsDef);
        return R.ok();
    }

    /**
     * 修改
     *
     * @param goodsDef goodsDef
     * @return R
     */
    @SysLog("修改")
    @RequestMapping("/update")
    @RequiresPermissions("ring:goodsdef:update")
    public R update(@RequestBody GoodsDefEntity goodsDef) {

        goodsDefService.update(goodsDef);

        return R.ok();
    }

    /**
     * 根据主键删除
     *
     * @param goodsIds goodsIds
     * @return R
     */
    @SysLog("删除")
    @RequestMapping("/delete")
    @RequiresPermissions("ring:goodsdef:delete")
    public R delete(@RequestBody Integer[] goodsIds) {
        goodsDefService.deleteBatch(goodsIds);

        return R.ok();
    }
}
