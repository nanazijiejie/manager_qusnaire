/*
 * 类名称:AdInfoEntity.java
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
 * 广告信息表实体
 *
 * @author lipengjun
 * @date 2019-08-24 14:29:31
 */
@Data
@TableName("tokyo_ad_info")
public class AdInfoEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private Integer addId;
    /**
     * 名称
     */
    private String adName;
    /**
     * 图片路径
     */
    private String adPicPath;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 创建人名字
     */
    private String createOperator;
    /**
     * 生效时间
     */
    private Date startDate;
    /**
     * 失效时间
     */
    private Date endDate;
    /**
     * 0:上架；1:下架
     */
    private String adStatus;
    /**
     * 展示顺序，从左到右
     */
    private Integer adSeq;
    /**
     * 类型：0屏显，1轮播
     */
    private Integer adType;
}
