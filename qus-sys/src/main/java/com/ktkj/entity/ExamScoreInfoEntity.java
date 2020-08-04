/*
 * 类名称:ExamScoreInfoEntity.java
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
import java.math.BigDecimal;
import java.util.Date;

/**
 * 实体
 *
 * @author lipengjun
 * @date 2019-10-09 15:21:52
 */
@Data
@TableName("tower_exam_score_info")
public class ExamScoreInfoEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 问卷结果ID
     */
    @TableId
    private Integer examResultId;
    /**
     * 填写问卷人职务
     */
    private String qusNaireStation;
    /**
     * 填写问卷人姓名
     */
    private String qusNaireName;
    /**
     * 填写问卷人归属部门
     */
    private String qusNaireDept;
    /**
     * 填写问卷人分管部门
     */
    private String qusNaireViceDept;
    /**
     * 填写问卷人归属地市
     */
    private String qusNaireCity;
    /**
     * 填写问卷人职务名称
     */
    private String qusNaireStationName;
    /**
     * 填写问卷人员工ID
     */
    private Integer qusNaireStaffId;
    /**
     * 填写问卷人归属部门ID
     */
    private String qusNaireDeptId;
    /**
     * 填写问卷人分管部门ID
     */
    private String qusNaireViceDeptId;
    /**
     * 填写问卷人归属地市ID
     */
    private String qusNaireCityId;
    /**
     * 考核人职务
     */
    private String examStation;
    /**
     * 考核人姓名
     */
    private String examName;
    /**
     * 考核人归属部门
     */
    private String examDept;
    /**
     * 考核人归属地市
     */
    private String examCity;
    /**
     * 考核人职务名称
     */
    private String examStationName;
    /**
     * 考核员工ID
     */
    private Integer examStaffId;
    /**
     * 考核人归属部门ID
     */
    private String examDeptId;
    /**
     * 考核人归属地市ID
     */
    private String examCityId;
    /**
     * 考核指标ID
     */
    private Integer indexItemId;
    /**
     * 考核指标名称
     */
    private String indexItemName;
    /**
     * 考核指标项得分
     */
    private BigDecimal indexItemScore;
    /**
     * 0:各考核项得分，1:总分
     */
    private String scoreType;
    /**
     * 提交时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 指标占比
     */
    private String indexItemPercent;
    /**
     * 界面元素ID
     */
    private String pageId;
    /**
     * 最终在exel中显示的职务ID
     */
    private String exelStation;
    /**
     * 最终在exel中显示的职务名称
     */
    private String exelStationName;

    private Integer examItemId;

    private String examItemName;
}
