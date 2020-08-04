/*
 * 类名称:ExamPerDefEntity.java
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

/**
 * 实体
 *
 * @author lipengjun
 * @date 2019-10-09 15:21:53
 */
@Data
@TableName("tower_exam_per_def")
public class ExamPerDefEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 占比ID
     */
    @TableId
    private Integer percentId;
    /**
     * 考核职务
     */
    private String examStation;
    /**
     * 考核职务ID
     */
    private String examStationId;
    /**
     * 省公司总经理占比
     */
//    private Integer provManagerPer;
//    /**
//     * 省公司副总经理占比
//     */
//    private Integer provViceManagerPer;
//    /**
//     * 省中层正职/省部门正职占比
//     */
//    private Integer provDeptManagerPer;
//    /**
//     * 省分管副总占比
//     */
//    private Integer provDuptyManagerPer;
//    /**
//     * 省其他副总占比
//     */
//    private Integer provOtherViceManagerPer;
//    /**
//     * 省中层正职副职/省部门正职副职占比
//     */
//    private Integer provMiddleManagerPer;
//    /**
//     * 本部门正职占比
//     */
//    private Integer localDeptManagerPer;
//    /**
//     * 本部门副职占比
//     */
//    private Integer localViceDeptManagerPer;
//    /**
//     * 本部门员工占比
//     */
//    private Integer localDeptStaffPer;
//    /**
//     * 省中层副职/省部门副职占比
//     */
//    private Integer provDeptViceManagerPer;
//    /**
//     * 地市正职/地市总经理占比
//     */
//    private Integer cityManagerPer;
//    /**
//     * 地市中层/地市部门正职副职占比
//     */
//    private Integer cityDeptManagerPer;
//    /**
//     * 地市员工占比
//     */
//    private Integer cityDeptStaffPer;
//    /**
//     * 地市副职/地市副总经理占比
//     */
//    private Integer cityDeptViceManagerPer;
    private String qusStation;
    private String qusStationId;
    private Integer percent;
}
