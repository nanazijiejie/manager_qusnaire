/*
 * 类名称:StaffDeptRelEntity.java
 * 包名称:com.ktkj.entity
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-10-14 15:00:32        lipengjun     初版做成
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
 * @date 2019-10-14 15:00:32
 */
@Data
@TableName("tower_staff_dept_rel")
public class StaffDeptRelEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 员工ID
     */
    private Integer staffId;
    /**
     * 部门ID
     */
    private Integer deptId;
    /**
     * 
     */
    @TableId
    private Integer id;
    /**
     * 0:归属 1:分管
     */
    private String relType;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 创建人
     */
    private String createOperator;
}
