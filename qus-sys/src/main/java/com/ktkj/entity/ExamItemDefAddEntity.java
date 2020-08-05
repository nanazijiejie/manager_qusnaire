/*
 * 类名称:ExamItemDefEntity.java
 * 包名称:com.ktkj.entity
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-10-09 15:21:52        lipengjun     初版做成
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
 * @date 2019-10-09 15:21:52
 */
public class ExamItemDefAddEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 考核项ID
     */
    private Integer examItemId;
    /**
     * 考核对象职务
     */
    private String examStation;
    /**
     * 考核对象职务ID
     */
    private String examStationId;
    /**
     * 得A人数限制
     */
    private Integer excellentCount;
    /**
     * 得B人数限制
     */
    private Integer goodCount;
    /**
     * 得C人数限制
     */
    private Integer normalCount;
    /**
     * 考核项名称
     */
    private String examItemName;
    /**
     * 考核项描述
     */
    private String examItemDesc;
    /**
     * 显示顺序
     */
    private String examSeq;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 创建人
     */
    private String createOperator;
    /**
     * 修改人
     */
    private Date updateTime;
    /**
     * 修改时间
     */
    private String updateOperator;
    /**
     * 考核项对应的指标项列表
     */
    private List<IndexItemDefAddEntity> examIndexRels;

    public Integer getExamItemId() {
        return examItemId;
    }

    public void setExamItemId(Integer examItemId) {
        this.examItemId = examItemId;
    }

    public String getExamStation() {
        return examStation;
    }

    public void setExamStation(String examStation) {
        this.examStation = examStation;
    }

    public String getExamStationId() {
        return examStationId;
    }

    public void setExamStationId(String examStationId) {
        this.examStationId = examStationId;
    }

    public Integer getExcellentCount() {
        return excellentCount;
    }

    public void setExcellentCount(Integer excellentCount) {
        this.excellentCount = excellentCount;
    }

    public Integer getGoodCount() {
        return goodCount;
    }

    public void setGoodCount(Integer goodCount) {
        this.goodCount = goodCount;
    }

    public String getExamItemName() {
        return examItemName;
    }

    public void setExamItemName(String examItemName) {
        this.examItemName = examItemName;
    }

    public String getExamItemDesc() {
        return examItemDesc;
    }

    public void setExamItemDesc(String examItemDesc) {
        this.examItemDesc = examItemDesc;
    }

    public String getExamSeq() {
        return examSeq;
    }

    public void setExamSeq(String examSeq) {
        this.examSeq = examSeq;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateOperator() {
        return createOperator;
    }

    public void setCreateOperator(String createOperator) {
        this.createOperator = createOperator;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateOperator() {
        return updateOperator;
    }

    public void setUpdateOperator(String updateOperator) {
        this.updateOperator = updateOperator;
    }

    public List<IndexItemDefAddEntity> getExamIndexRels() {
        return examIndexRels;
    }


    public Integer getNormalCount() {
        return normalCount;
    }

    public void setNormalCount(Integer normalCount) {
        this.normalCount = normalCount;
    }

    public void setExamIndexRels(List<IndexItemDefAddEntity> examIndexRels) {
        this.examIndexRels = examIndexRels;
    }
}
