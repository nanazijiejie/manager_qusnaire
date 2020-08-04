/*
 * 类名称:StaffInfoEntity.java
 * 包名称:com.ktkj.entity
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-10-01 16:23:16        lipengjun     初版做成
 *
 * Copyright (c) 2019-2019 厦门继续想科技有限公司
 */
package com.ktkj.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ktkj.validator.group.AddGroup;
import com.ktkj.validator.group.UpdateGroup;
import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.Date;

/**
 * 实体
 *
 * @author lipengjun
 * @date 2019-10-01 16:23:16
 */
@Data
@TableName("tower_staff_info")
public class StaffInfoEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 员工ID
     */
    @TableId
    private Integer staffId;
    /**
     * 员工姓名
     */
    @NotBlank(message = "员工姓名不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String staffName;
    /**
     * 归属地市（省公司+九地市）
     */
    @NotBlank(message = "归属地市不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String city;
    /**
     * 归属部门
     */
    @NotBlank(message = "归属部门不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String dept;
    /**
     * 职务
     */
    @NotBlank(message = "职务不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String station;
    /**
     * 邮箱
     */
    @NotBlank(message = "邮箱不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Email(message = "邮箱格式不正确", groups = {AddGroup.class, UpdateGroup.class})
    private String email;
    /**
     * 手机号码
     */
    private String phone;
    /**
     * 密码，作为登录问卷系统用
     */
    private String pwd;
    /**
     * 0:正常 1:删除
     */
    private String sendStatus;
    /**
     * 创建人名字
     */
    private String createOperator;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改人名字
     */
    private String updateOperator;
    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 修改时间
     */
    private Date sendTime;
    /**
     * 登录的token标记
     */
    private String token;
    /**
     * 最后一次登录系统的时间
     */
    private Date lastLoginTime;

    private String cityId;

    private String deptId;

    private String stationId;

    private String isSubmit;

    private String riceDeptId;

    private String riceDept;

    private String isRepresent;

    private String isChief;

    private String isUptPwd;

    private String isSelection;

    private String selStationId;

    private String selStation;

    private String isSelSubmit;

    private String selEdu;

    private String selPerform1;

    private String selPerform2;

    private String selPerform3;

    private String selTakeOfficeYears;

    private String selNowStation;




}
