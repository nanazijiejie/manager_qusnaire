/*
 * 类名称:DictDefEntity.java
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

/**
 * 实体
 *
 * @author lipengjun
 * @date 2019-10-09 15:21:52
 */
@Data
@TableName("tower_dict_def")
public class DictDefEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId
    private Integer code;
    /**
     * 类型
     */
    private String typeCode;
    /**
     * 字典名称（展示用）
     */
    private String itemName;
    /**
     * 字典值（使用）
     */
    private String itemValue;
    /**
     * 类型描述
     */
    private String typeDesc;
}
