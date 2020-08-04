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

/**
 * 实体
 *
 * @author lipengjun
 * @date 2019-10-09 15:21:52
 */
@Data
@TableName("tower_exam_item_def")
public class ExamItemDefEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 考核项ID
     */
    @TableId
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
     * 优秀人数限制
     */
    private Integer excellentCount;
    /**
     * 优良人数限制
     */
    private Integer goodCount;
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

    //private Integer[] examIndexRels;
}
