/*
 * 类名称:IndexItemDefEntity.java
 * 包名称:com.ktkj.entity
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-10-09 15:21:53        lipengjun     初版做成
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
 * @date 2019-10-09 15:21:53
 */
public class IndexItemDefAddEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 指标项ID
     */
    private Integer indexItemId;
    /**
     * 指标项名称
     */
    private String indexItemName;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 创建人
     */
    private String createOperator;
    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 修改人
     */
    private String updateOperator;
    /**
     * 顺序
     */
    private Integer itemSeq;
    /**
     *是否被勾选--页面展示使用
     */
    private boolean isChecked;
    /**
     * 占比--页面展示使用
     */
    private String percent;

    public Integer getIndexItemId() {
        return indexItemId;
    }

    public void setIndexItemId(Integer indexItemId) {
        this.indexItemId = indexItemId;
    }

    public String getIndexItemName() {
        return indexItemName;
    }

    public void setIndexItemName(String indexItemName) {
        this.indexItemName = indexItemName;
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

    public Integer getItemSeq() {
        return itemSeq;
    }

    public void setItemSeq(Integer itemSeq) {
        this.itemSeq = itemSeq;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }
}
