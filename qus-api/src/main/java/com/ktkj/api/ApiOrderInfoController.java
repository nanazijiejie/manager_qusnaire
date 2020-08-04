/*
 * 类名称:OrderInfoController.java
 * 包名称:com.ktkj.controller
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-08-27 16:57:03        lipengjun     初版做成
 *
 * Copyright (c) 2019-2019 厦门继续想科技有限公司
 */
package com.ktkj.api;

import com.ktkj.annotation.IgnoreAuth;
import com.ktkj.controller.AbstractController;
import com.ktkj.entity.OrderInfoVo;
import com.ktkj.service.ApiOrderInfoService;
import com.ktkj.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller
 *
 * @author lipengjun
 * @date 2019-08-27 16:57:03
 */
@Api(tags = "订单信息")
@RestController
@RequestMapping("/api/orderinfo")
public class ApiOrderInfoController extends AbstractController {
    @Autowired
    private ApiOrderInfoService apiOrderInfoService;

    /**
     * 新增
     *
     * @param orderInfo orderInfo
     * @return R
     */
    @ApiOperation(value = "save")
    @IgnoreAuth
    @PostMapping(value = "save")
    public R save(@RequestBody OrderInfoVo orderInfo) {
        orderInfo = apiOrderInfoService.add(orderInfo);
        Map<String, Object> map = new HashMap<>();
        map.put("orderInfo", orderInfo);
        return R.ok(map);
    }

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return R
     */
    @ApiOperation(value = "queryExpress")
    @IgnoreAuth
    @PostMapping(value = "queryExpress")
    public R queryExpress(@RequestParam Map<String, Object> params) {
        List<OrderInfoVo> list = apiOrderInfoService.queryAll(params);

        return R.ok().put("list", list);
    }
}
