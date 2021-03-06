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
package com.ktkj.api;

import com.ktkj.annotation.IgnoreAuth;
import com.ktkj.controller.AbstractController;
import com.ktkj.entity.GoodsDefVo;
import com.ktkj.service.ApiGoodsDefService;
import com.ktkj.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Controller
 *
 * @author lipengjun
 * @date 2019-08-27 16:57:02
 */
@Api(tags = "产品信息")
@RestController
@RequestMapping("/api/goodsdef")
public class ApiGoodsDefController extends AbstractController {
    @Autowired
    private ApiGoodsDefService apiGoodsDefService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return R
     */
    @ApiOperation(value = "queryAll")
    @IgnoreAuth
    @PostMapping(value = "queryAll")
    public R queryAll(@RequestParam Map<String, Object> params) {
        List<GoodsDefVo> list = apiGoodsDefService.queryAll(params);

        return R.ok().put("list", list);
    }
}
