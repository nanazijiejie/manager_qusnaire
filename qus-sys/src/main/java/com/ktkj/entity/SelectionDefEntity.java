/*
 * 类名称:SelectionDefEntity.java
 * 包名称:com.ktkj.entity
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2020-02-21 18:34:23        lipengjun     初版做成
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
 * @date 2020-02-21 18:34:23
 */
@Data
@TableName("tower_selection_def")
public class SelectionDefEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 选举的职务名称
     */
    private String selStation;
    /**
     * 选举的职务ID
     */
    private String selStationId;
    /**
     * 候选人数量
     */
    private Integer selCount;
    /**
     * 归属单位（地市）
     */
    private String selCityId;
    /**
     * 归属单位（地市）名称
     */
    private String selCity;
    /**
     * ID，自增序列
     */
    @TableId
    private Integer selDefId;
    /**
     * 选举表格名称
     */
    private String selName;
}
