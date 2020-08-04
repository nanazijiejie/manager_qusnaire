/*
 * 类名称:DictDefController.java
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
import com.ktkj.cache.J2CacheUtils;
import com.ktkj.entity.DictDefEntity;
import com.ktkj.service.DictDefService;
import com.ktkj.utils.Constant;
import com.ktkj.utils.PageUtils;
import com.ktkj.utils.R;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller
 *
 * @author lipengjun
 * @date 2019-10-09 15:21:52
 */
@RestController
@RequestMapping("/tower/dictdef")
public class DictDefController extends AbstractController {
    @Autowired
    private DictDefService dictDefService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return R
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("tower:dictdef:list")
    public R queryAll(@RequestParam Map<String, Object> params) {
        List<DictDefEntity> dictList = null;
        if( J2CacheUtils.get(Constant.DATA_DICT)!=null){
            dictList = (List<DictDefEntity>)J2CacheUtils.get(Constant.DATA_DICT);
        }else{
            dictList = dictDefService.queryAll(params);
            J2CacheUtils.set(Constant.DATA_DICT,dictList,Constant.ALIVE_SECONDS,false);
        }

        return R.ok().put("list", dictList);
    }

    /**
     * 分页查询
     *
     * @param params 查询参数
     * @return R
     */
    @GetMapping("/list")
    @RequiresPermissions("tower:dictdef:list")
    public R list(@RequestParam Map<String, Object> params) {
        if(params.get("typeCode")!=null){
            String typeCode = java.net.URLDecoder.decode(""+params.get("typeCode"));
            params.put("typeCode",typeCode);
        }
        Page page = dictDefService.queryPage(params);
        PageUtils pageUtil = new PageUtils(page.getRecords(), (int)page.getTotal(), (int)page.getSize(), (int)page.getCurrent());
        return R.ok().put("page", pageUtil);

    }

    /**
     * 分页查询
     *
     * @param params 查询参数
     * @return R
     */
    /*@GetMapping("/list")
    @RequiresPermissions("tower:dictdef:list")
    public R list(@RequestParam Map<String, Object> params) {
        Query query = new Query(params);
        Page page = dictDefService.queryPage(params);
        PageUtils pageUtil = new PageUtils(page.getRecords(),
                Integer.parseInt(page.getTotal()+""),
                Integer.parseInt(page.getSize()+""),
                query.getPage());
        return R.ok().put("page", pageUtil);
    }*/

    /**
     * 根据主键查询详情
     *
     * @param code 主键
     * @return R
     */
    @RequestMapping("/info/{code}")
    @RequiresPermissions("tower:dictdef:info")
    public R info(@PathVariable("code") Integer code) {
        DictDefEntity dictDef = dictDefService.getById(code);

        return R.ok().put("dictdef", dictDef);
    }

    /**
     * 新增
     *
     * @param dictDef dictDef
     * @return R
     */
    @SysLog("新增")
    @RequestMapping("/save")
    @RequiresPermissions("tower:dictdef:save")
    public R save(@RequestBody DictDefEntity dictDef) {

        dictDefService.add(dictDef);

        return R.ok();
    }

    /**
     * 修改
     *
     * @param dictDef dictDef
     * @return R
     */
    @SysLog("修改")
    @RequestMapping("/update")
    @RequiresPermissions("tower:dictdef:update")
    public R update(@RequestBody DictDefEntity dictDef) {

        dictDefService.update(dictDef);

        return R.ok();
    }

    /**
     * 根据主键删除
     *
     * @param codes codes
     * @return R
     */
    @SysLog("删除")
    @RequestMapping("/delete")
    @RequiresPermissions("tower:dictdef:delete")
    public R delete(@RequestBody Integer[] codes) {
        dictDefService.deleteBatch(codes);

        return R.ok();
    }
}
