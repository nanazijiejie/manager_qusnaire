/*
 * 类名称:SpecParamDefEntity.java
 * 包名称:com.ktkj.entity
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-08-27 16:57:03        lipengjun     初版做成
 *
 * Copyright (c) 2019-2019 厦门继续想科技有限公司
 */
package com.ktkj.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 实体
 *
 * @author lipengjun
 * @date 2019-08-27 16:57:03
 */
public class SpecParamDefVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 规格ID
     */
    private Integer specId;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 规格组顺序
     */
    private String specSeq;
    /**
     * 规格组名称
     */
    private String specName;
    /**
     * 参数名称
     */
    private String paramName;
    /**
     * 图片
     */
    private String paramPicPath;
    /**
     * 状态（0上架1下架）
     */
    private String specStatus;

    public String getSpecSeq() {
        return specSeq;
    }

    public void setSpecSeq(String specSeq) {
        this.specSeq = specSeq;
    }

    public Integer getSpecId() {
        return specId;
    }

    public void setSpecId(Integer specId) {
        this.specId = specId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getParamPicPath() {
        return paramPicPath;
    }

    public void setParamPicPath(String paramPicPath) {
        this.paramPicPath = paramPicPath;
    }

    public String getSpecStatus() {
        return specStatus;
    }

    public void setSpecStatus(String specStatus) {
        this.specStatus = specStatus;
    }
}
