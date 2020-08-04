/*
 * 类名称:IndexItemDefEntity.java
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
import java.util.Date;

/**
 * 实体
 *
 * @author lipengjun
 * @date 2019-10-09 15:21:53
 */
@Data
@TableName("tower_index_item_def")
public class IndexItemDefEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 指标项ID
     */
    @TableId
    private Integer indexItemId;
    /**
     * 指标项名称
     */
    private String indexItemName;
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
    /**
     * 顺序
     */
    private Integer itemSeq;
}
