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
package com.ktkj.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ktkj.annotation.SysLog;
import com.ktkj.utils.PageUtils;
import com.ktkj.utils.Query;
import com.ktkj.utils.R;
import com.ktkj.controller.AbstractController;
import com.ktkj.entity.OrderInfoEntity;
import com.ktkj.service.OrderInfoService;
import com.ktkj.utils.StringUtils;
import com.ktkj.utils.excel.ExcelExport;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Controller
 *
 * @author lipengjun
 * @date 2019-08-27 16:57:03
 */
@RestController
@RequestMapping("/ring/orderinfo")
public class OrderInfoController extends AbstractController {
    @Autowired
    private OrderInfoService orderInfoService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return R
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("ring:orderinfo:list")
    public R queryAll(@RequestParam Map<String, Object> params) {
        List<OrderInfoEntity> list = orderInfoService.queryAll(params);

        return R.ok().put("list", list);
    }

    @RequestMapping("/queryList")
    @ResponseBody
    public Object queryList( HttpServletRequest request,
                              HttpServletResponse response) throws Exception {
        String contactPhone = request.getParameter("contactPhone");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("contactPhone",contactPhone);
        List<OrderInfoEntity> list = orderInfoService.queryAll(params);
        return list;
    }
    /**
     * 分页查询
     *
     * @param params 查询参数
     * @return R
     */
    @GetMapping("/list")
    @RequiresPermissions("ring:orderinfo:list")
    public R list(@RequestParam Map<String, Object> params) {
        Query query = new Query(params);
        List<OrderInfoEntity> list = orderInfoService.queryAll(params);
        PageUtils pageUtil = new PageUtils(
                list, list.size(), query.getLimit(), query.getPage());
        return R.ok().put("page", pageUtil);
    }

    /**
     * 导出会员
     */
    @RequestMapping("/export")
    @RequiresPermissions("ring:orderinfo:export")
    public R export(@RequestParam Map<String, Object> params, HttpServletResponse response) {

        List<OrderInfoEntity> orderList = orderInfoService.queryAll(params);

        ExcelExport ee = new ExcelExport("订单列表");

        String[] header = new String[]{"订单号", "状态", "支付金额（单位：元）", "下单时间",
        "支付时间","商品名称","用户微信昵称","收货人","联系电话","收货地址","商品规格","数量","物流单号"};
        List<Map<String, Object>> list = new ArrayList<>();
        if (orderList != null && orderList.size() != 0) {
            for (OrderInfoEntity orderInfoEntity : orderList) {
                LinkedHashMap<String, Object> map = new LinkedHashMap<>();
                map.put("ORDER_ID", orderInfoEntity.getOrderId());
                String orderStatusName = "";
                if("0".equals(orderInfoEntity.getOrderStatus())){
                    map.put("ORDER_STATUS", "预下单待支付");
                }else if("1".equals(orderInfoEntity.getOrderStatus())){
                    map.put("ORDER_STATUS", "支付成功");
                } else if("2".equals(orderInfoEntity.getOrderStatus())){
                    map.put("ORDER_STATUS", "支付失败");
                }
                map.put("TOTAL_PAY_AMOUNT", orderInfoEntity.getTotalPayAmount()/100);
                map.put("CREATE_TIME", orderInfoEntity.getCreateTime());
                map.put("PAY_TIME", orderInfoEntity.getPayTime());
                map.put("GOODS_NAME", orderInfoEntity.getGoodsName());
                map.put("NICK_NAME", orderInfoEntity.getNickName());
                map.put("RECEIVER",orderInfoEntity.getReceiver());
                map.put("CONTACT_PHONE",orderInfoEntity.getContactPhone());
                map.put("RECEIVE_ADDR",orderInfoEntity.getReceiveAddr());
                map.put("SPEC_PARAM",orderInfoEntity.getSpecParam());
                map.put("TOTAL_COUNT",orderInfoEntity.getTotalCount());
                map.put("EXPRESS_ID",orderInfoEntity.getExpressId());
                list.add(map);
            }
        }

        ee.addSheetByMap("订单", list, header);
        ee.export(response);
        return R.ok();
    }

    /**
     * 根据主键查询详情
     *
     * @param orderId 主键
     * @return R
     */
    @RequestMapping("/info/{orderId}")
    @RequiresPermissions("ring:orderinfo:info")
    public R info(@PathVariable("orderId") String orderId) {
        OrderInfoEntity orderInfo = orderInfoService.getById(orderId);

        return R.ok().put("orderInfo", orderInfo);
    }

    /**
     * 新增
     *
     * @param orderInfo orderInfo
     * @return R
     */
    @SysLog("新增")
    @RequestMapping("/save")
    @RequiresPermissions("ring:orderinfo:save")
    public R save(@RequestBody OrderInfoEntity orderInfo) {

        orderInfoService.add(orderInfo);

        return R.ok();
    }

    /**
     * 修改
     *
     * @param orderInfo orderInfo
     * @return R
     */
    @SysLog("修改")
    @RequestMapping("/update")
    @RequiresPermissions("ring:orderinfo:update")
    public R update(@RequestBody OrderInfoEntity orderInfo) {
        if(orderInfo.getTotalPayAmount()!=null){
            orderInfo.setTotalPayAmount(orderInfo.getTotalPayAmount()*100);
        }
        orderInfoService.update(orderInfo);
        return R.ok();
    }

    @RequestMapping("/scanExpress")
    @ResponseBody
    public Object scanExpress( HttpServletRequest request,
                             HttpServletResponse response) throws Exception {
        String expressId = request.getParameter("expressId");
        String orderId = request.getParameter("orderId");
        OrderInfoEntity orderInfo = new OrderInfoEntity();
        orderInfo.setOrderId(orderId);
        orderInfo.setExpressId(expressId);
        orderInfoService.scanExpress(orderInfo);
        return true;

    }

    /**
     * 根据主键删除
     *
     * @param orderIds orderIds
     * @return R
     */
    @SysLog("删除")
    @RequestMapping("/delete")
    @RequiresPermissions("ring:orderinfo:delete")
    public R delete(@RequestBody String[] orderIds) {
        orderInfoService.deleteBatch(orderIds);

        return R.ok();
    }
}
