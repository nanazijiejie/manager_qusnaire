/*
 * 类名称:GoodsDefServiceImpl.java
 * 包名称:com.ktkj.service.impl
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-08-27 16:57:02        lipengjun     初版做成
 *
 * Copyright (c) 2019-2019 厦门继续想科技有限公司
 */
package com.ktkj.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ktkj.dao.ApiGoodsDefMapper;
import com.ktkj.entity.GoodsDefVo;
import com.ktkj.utils.QueryPlus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Service实现类
 *
 * @author lipengjun
 * @date 2019-08-27 16:57:02
 */
@Service("apiGoodsDefService")
public class ApiGoodsDefService {
    @Autowired
    private ApiGoodsDefMapper apiGoodsDefDao;

    public List<GoodsDefVo> queryAll(Map<String, Object> params) {
        return apiGoodsDefDao.queryAll(params);
    }

    public Page queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "T.goods_id");
        params.put("asc", false);
        Page<GoodsDefVo> page = new QueryPlus<GoodsDefVo>(params).getPage();
        return page.setRecords(apiGoodsDefDao.selectGoodsDefPage(page, params));
    }
}
