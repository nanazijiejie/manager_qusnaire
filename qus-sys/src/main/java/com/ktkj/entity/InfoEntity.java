/*
 * 类名称:InfoEntity.java
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
 * 实体
 *
 * @author lipengjun
 * @date 2019-08-24 14:29:31
 */
@Data
@TableName("tokyo_info")
public class InfoEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private Integer infoId;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 图片路径1
     */
    private String pic1Path;
    /**
     * 图片路径2
     */
    private String pic2Path;
    /**
     * 图片路径3
     */
    private String pic3Path;
    /**
     * 二维码图片路径
     */
    private String weixinCodePath;
    /**
     * 审核时间
     */
    private Date examTime;
    /**
     * 审核人名称
     */
    private String examOperator;
    /**
     * 类别ID
     */
    private Integer typeId;
    /**
     * 类别名称
     */
    private String typeName;
    /**
     * 标题
     */
    private String title;
    /**
     * 信息状态：0待审核，1审核通过，2审核不通过
     */
    private String infoStatus;
    /**
     * 简短描述
     */
    private String shortDesc;
}
