/*
 * 类名称:FinalExamScoreInfoEntity.java
 * 包名称:com.ktkj.entity
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-10-24 11:58:43        lipengjun     初版做成
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
 * @date 2019-10-24 11:58:43
 */
@Data
@TableName("tower_final_exam_score_info")
public class FinalExamScoreInfoEntity implements Serializable {
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
     * 填写问卷人职务名称
     */
    private String qusNaireStationName;
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
     * 最终考核平均分
     */
    private BigDecimal finalScore;
    /**
     * 提交时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;
}
