/*
 * 类名称:QuestionnaireDefEntity.java
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
@TableName("tower_questionnaire_def")
public class QuestionnaireDefEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 问卷ID
     */
    @TableId
    private Integer qusNaireId;
    /**
     * 问卷填写人职务
     */
    private String qusNaireStation;
    /**
     * 问卷填写人职务ID
     */
    private String qusNaireStationId;

    /**
     * 问卷名称
     */
    private String qusNaireName;
    /**
     * 问卷考核抬头说明
     */
    private String headDesc;
    /**
     * 问卷考核末尾说明
     */
    private String bottomDesc;
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
}
