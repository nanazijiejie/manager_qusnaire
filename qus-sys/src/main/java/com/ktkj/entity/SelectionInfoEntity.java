/*
 * 类名称:SelectionInfoEntity.java
 * 包名称:com.ktkj.entity
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2020-02-21 22:35:28        lipengjun     初版做成
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
 * @date 2020-02-21 22:35:28
 */
@Data
@TableName("tower_selection_info")
public class SelectionInfoEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * ID,自增主键
     */
    @TableId
    private Integer selId;
    /**
     * 选举岗位
     */
    private String selStationId;
    /**
     * 选举岗位名称
     */
    private String selStation;
    /**
     * 被选举人姓名
     */
    private String selName;
    /**
     * 被选举人归属单位
     */
    private String selCityId;
    /**
     * 被选举人归属单位名称
     */
    private String selCity;
    /**
     * 被选举人当前岗位
     */
    private String selPreStationId;
    /**
     * 被选举人当前岗位名称
     */
    private String selPreStation;
    /**
     * 参与选举人姓名
     */
    private String qusName;
    /**
     * 参与选举人归属单位
     */
    private String qusCityId;
    /**
     * 参与选举人归属单位名称
     */
    private String qusCity;
    /**
     * 参与选举人归属部门
     */
    private String qusDept;
    /**
     * 被选举人部门
     */
    private String selDept;
    /**
     * 参与选举人职务
     */
    private String qusStationId;
    /**
     * 参与选举人职务名称
     */
    private String qusStation;
    /**
     * 投票的原因
     */
    private String reason;
    /**
     * 被选举人的系统ID
     */
    private Integer selStaffId;
    /**
     * 提交时间
     */
    private Date createTime;
}
