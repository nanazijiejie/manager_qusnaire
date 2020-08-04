/*
 * 类名称:OrderInfoEntity.java
 * 包名称:com.ktkj.entity
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-08-27 16:57:03        lipengjun     初版做成
 *
 * Copyright (c) 2019-2019 厦门继续想科技有限公司
 */
package com.ktkj.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 实体
 *
 * @author lipengjun
 * @date 2019-08-27 16:57:03
 */
public class OrderInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 订单ID
     */
    private String orderId;
    /**
     * 订单状态：0预下单 1支付成功 2支付失败
     */
    private String orderStatus;
    /**
     * 支付金额，单位为分
     */
    private Integer totalPayAmount;
    /**
     * 支付时间
     */
    private Date payTime;
    /**
     * 下单时间
     */
    private Date createTime;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 用户微信昵称
     */
    private String nickName;
    /**
     * 微信用户ID
     */
    private String openId;
    /**
     * 收货人姓名
     */
    private String receiver;
    /**
     * 联系电话
     */
    private String contactPhone;
    /**
     * 收货地址
     */
    private String receiveAddr;
    /**
     * 商品规格
     */
    private String specParam;
    /**
     * 数量
     */
    private Integer totalCount;
    /**
     * 物流单号
     */
    private String expressId;
    /**
     * 统一下单接口返回数据
     */
    private String uniformorder;
    /**
     * 支付回调接口返回数据
     */
    private String notify;

    public String getUniformorder() {
        return uniformorder;
    }

    public void setUniformorder(String uniformorder) {
        this.uniformorder = uniformorder;
    }

    public String getNotify() {
        return notify;
    }

    public void setNotify(String notify) {
        this.notify = notify;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getTotalPayAmount() {
        return totalPayAmount;
    }

    public void setTotalPayAmount(Integer totalPayAmount) {
        this.totalPayAmount = totalPayAmount;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getReceiveAddr() {
        return receiveAddr;
    }

    public void setReceiveAddr(String receiveAddr) {
        this.receiveAddr = receiveAddr;
    }

    public String getSpecParam() {
        return specParam;
    }

    public void setSpecParam(String specParam) {
        this.specParam = specParam;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public String getExpressId() {
        return expressId;
    }

    public void setExpressId(String expressId) {
        this.expressId = expressId;
    }
}
