/*
 * 类名称:TypeDefEntity.java
 * 包名称:com.ktkj.entity
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-08-24 14:29:31        lipengjun     初版做成
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
 * 信息分类实体
 *
 * @author lipengjun
 * @date 2019-08-24 14:29:31
 */
@Data
@TableName("tokyo_type_def")
public class TypeDefEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private Integer typeId;
    /**
     * 类型名称
     */
    private String typeName;
    /**
     * 在小程序中的展示顺序，从左到右。
     */
    private Integer showSeq;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 创建人名字
     */
    private String createOperator;
    /**
     * 0:上架；1:下架
     */
    private String typeStatus;
    /**
     * 图片路径
     */
    private String picPath;
}
