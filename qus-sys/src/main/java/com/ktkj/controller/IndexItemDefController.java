/*
 * 类名称:IndexItemDefController.java
 * 包名称:com.ktkj.controller
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-10-09 15:21:53        lipengjun     初版做成
 *
 * Copyright (c) 2019-2019 厦门继续想科技有限公司
 */
package com.ktkj.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ktkj.annotation.SysLog;
import com.ktkj.entity.ExamIndexRelEntity;
import com.ktkj.entity.IndexItemDefAddEntity;
import com.ktkj.service.ExamIndexRelService;
import com.ktkj.utils.PageUtils;
import com.ktkj.utils.Query;
import com.ktkj.utils.R;
import com.ktkj.controller.AbstractController;
import com.ktkj.entity.IndexItemDefEntity;
import com.ktkj.service.IndexItemDefService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Controller
 *
 * @author lipengjun
 * @date 2019-10-09 15:21:53
 */
@RestController
@RequestMapping("/tower/indexitemdef")
public class IndexItemDefController extends AbstractController {
    @Autowired
    private IndexItemDefService indexItemDefService;
    @Autowired
    private ExamIndexRelService examIndexRelService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return R
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("tower:indexitemdef:list")
    public R queryAll(@RequestParam Map<String, Object> params) {
        List<IndexItemDefEntity> list = indexItemDefService.queryAll(params);

        return R.ok().put("list", list);
    }

    /**
     * 分页查询
     *
     * @param params 查询参数
     * @return R
     */
    @GetMapping("/list")
    @RequiresPermissions("tower:indexitemdef:list")
    public R list(@RequestParam Map<String, Object> params) {
        if(params.get("indexItemName")!=null){
            String indexItemName = java.net.URLDecoder.decode(""+params.get("indexItemName"));
            params.put("indexItemName",indexItemName);
        }
        Page page = indexItemDefService.queryPage(params);

        PageUtils pageUtil = new PageUtils(page.getRecords(), (int)page.getTotal(), (int)page.getSize(), (int)page.getCurrent());

        return R.ok().put("page", pageUtil);
    }

    /**
     * 查询某个考核项对应的指标项
     * @param params
     * @return
     */
    @GetMapping("/indexList")
    public R indexList(@RequestParam Map<String, Object> params) {
        List<IndexItemDefEntity> list = indexItemDefService.queryAll(params);
        List<ExamIndexRelEntity> relList =null;
        if(params.get("examItemId")!=null){//查询该考核项勾选了哪些指标
            Map<String, Object> params2 = new HashMap<String, Object>();
            params2.put("examItemId",params.get("examItemId"));
            relList = examIndexRelService.queryAll(params2);
        }
        List<IndexItemDefAddEntity> retList = new ArrayList<IndexItemDefAddEntity>();
        for (IndexItemDefEntity indexItemDefEntity:list
             ) {
            IndexItemDefAddEntity indexItemDefAddEntity = new IndexItemDefAddEntity();
            indexItemDefAddEntity.setIndexItemId(indexItemDefEntity.getIndexItemId());
            indexItemDefAddEntity.setIndexItemName(indexItemDefEntity.getIndexItemName());
            indexItemDefAddEntity.setItemSeq(indexItemDefEntity.getItemSeq());
            retList.add(indexItemDefAddEntity);
        }
        if(relList!=null){
            for(int i=0;i<retList.size();i++){
                for(int j=0;j<relList.size();j++){
                    if(relList.get(j).getIndexItemId()==retList.get(i).getIndexItemId()){
                        retList.get(i).setChecked(true);
                        retList.get(i).setPercent(relList.get(j).getPercent());
                        break;
                    }
                }
            }
        }
        /*for (IndexItemDefEntity indexItemDefEntity:list
             ) {
            if(indexItemDefEntity.getPercent()==null){
                indexItemDefEntity.setPercent(0);
            }
        }*/
        return R.ok().put("list", retList);
    }

    /**
     * 根据主键查询详情
     *
     * @param indexItemId 主键
     * @return R
     */
    @RequestMapping("/info/{indexItemId}")
    @RequiresPermissions("tower:indexitemdef:info")
    public R info(@PathVariable("indexItemId") Integer indexItemId) {
        IndexItemDefEntity indexItemDef = indexItemDefService.getById(indexItemId);

        return R.ok().put("indexItemDef", indexItemDef);
    }

    /**
     * 新增
     *
     * @param indexItemDef indexItemDef
     * @return R
     */
    @SysLog("新增")
    @RequestMapping("/save")
    @RequiresPermissions("tower:indexitemdef:save")
    public R save(@RequestBody IndexItemDefEntity indexItemDef) {
        //判断指标项名称是否存在，不能重复添加
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("indexItemName",indexItemDef.getIndexItemName());
        List<IndexItemDefEntity> list = indexItemDefService.queryAll(map);
        if(list!=null&&list.size()!=0){
            return R.error("指标项：'"+indexItemDef.getIndexItemName()+"'已存在，请勿重复添加！");
        }
        indexItemDef.setCreateOperator(getUser().getUsername());
        indexItemDef.setCreateTime(new Date());
        indexItemDefService.add(indexItemDef);

        return R.ok();
    }

    /**
     * 修改
     *
     * @param indexItemDef indexItemDef
     * @return R
     */
    @SysLog("修改")
    @RequestMapping("/update")
    @RequiresPermissions("tower:indexitemdef:update")
    public R update(@RequestBody IndexItemDefEntity indexItemDef) {
        indexItemDef.setUpdateOperator(getUser().getUsername());
        indexItemDef.setUpdateTime(new Date());
        indexItemDefService.update(indexItemDef);

        return R.ok();
    }

    /**
     * 根据主键删除
     *
     * @param indexItemIds indexItemIds
     * @return R
     */
    @SysLog("删除")
    @RequestMapping("/delete")
    @RequiresPermissions("tower:indexitemdef:delete")
    public R delete(@RequestBody Integer[] indexItemIds) {
        indexItemDefService.deleteBatch(indexItemIds);

        return R.ok();
    }
}
