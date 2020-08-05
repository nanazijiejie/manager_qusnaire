/*
 * 类名称:EvaluateInfoEntity.java
 * 包名称:com.ktkj.entity
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2020-03-24 09:58:44        lipengjun     初版做成
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
 * @date 2020-03-24 09:58:44
 */
@Data
@TableName("tower_evaluate_info")
public class EvaluateInfoEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 自增序列
     */
    @TableId
    private Integer evaluateId;
    /**
     * 评价内容
     */
    private String evaContent;
    /**
     * 地市/单位
     */
    private String city;
    /**
     * 地市/单位ID
     */
    private String cityId;
    /**
     * 评价人ID
     */
    private Integer staffId;
    /**
     * 评价人姓名
     */
    private String staffName;
    /**
     * 评价人部门
     */
    private String staffDept;
    /**
     * 得分
     */
    private String score;
    /**
     * 1:总计 0:细项
     */
    private String scoreType;

    private String evaluateType;
}
