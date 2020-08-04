/*
 * 类名称:GoodsDefEntity.java
 * 包名称:com.ktkj.entity
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-08-27 16:57:02        lipengjun     初版做成
 *
 * Copyright (c) 2019-2019 厦门继续想科技有限公司
 */
package com.ktkj.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 实体
 *
 * @author lipengjun
 * @date 2019-08-27 16:57:02
 */
@Data
@TableName("ring_goods_def")
public class GoodsDefEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 商品ID
     */
    @TableId
    private Integer goodsId;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 商品大图路径
     */
    private String goodsPicPath;
    /**
     * 商品描述
     */
    private String goodsDesc;
    /**
     * 创建人名字
     */
    private String createOperator;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 生效时间
     */
    private Date startDate;
    /**
     * 失效时间
     */
    private Date endDate;
    /**
     * 商品状态，0上架1下架
     */
    private String goodsStatus;
    /**
     * 商品详情图
     */
    private String goodsPicDtlPath;
    /**
     * 商品价格，单位分
     */
    private Integer goodsPrice;
    /**
     * 商品原价，单位分
     */
    private Integer preGoodsPrice;
    /**
     * 界面特别说明
     */
    private String specialDesc;
    /**
     * 客服联系二维码图片
     */
    private String contactCodePath;
    /**
     * 商家联系电话
     */
    private String contactPhone;

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsPicPath() {
        return goodsPicPath;
    }

    public void setGoodsPicPath(String goodsPicPath) {
        this.goodsPicPath = goodsPicPath;
    }

    public String getGoodsDesc() {
        return goodsDesc;
    }

    public void setGoodsDesc(String goodsDesc) {
        this.goodsDesc = goodsDesc;
    }

    public String getCreateOperator() {
        return createOperator;
    }

    public void setCreateOperator(String createOperator) {
        this.createOperator = createOperator;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getGoodsStatus() {
        return goodsStatus;
    }

    public void setGoodsStatus(String goodsStatus) {
        this.goodsStatus = goodsStatus;
    }

    public String getGoodsPicDtlPath() {
        return goodsPicDtlPath;
    }

    public void setGoodsPicDtlPath(String goodsPicDtlPath) {
        this.goodsPicDtlPath = goodsPicDtlPath;
    }

    public Integer getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(Integer goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public Integer getPreGoodsPrice() {
        return preGoodsPrice;
    }

    public void setPreGoodsPrice(Integer preGoodsPrice) {
        this.preGoodsPrice = preGoodsPrice;
    }

    public String getSpecialDesc() {
        return specialDesc;
    }

    public void setSpecialDesc(String specialDesc) {
        this.specialDesc = specialDesc;
    }

    public String getContactCodePath() {
        return contactCodePath;
    }

    public void setContactCodePath(String contactCodePath) {
        this.contactCodePath = contactCodePath;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }
}
