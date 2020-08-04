/*
 * 类名称:HighOpinionDefEntity.java
 * 包名称:com.ktkj.entity
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-08-27 16:57:02        lipengjun     初版做成
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
 * @date 2019-08-27 16:57:02
 */
@Data
@TableName("ring_high_opinion_def")
public class HighOpinionDefEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 评论ID
     */
    @TableId
    private Integer opinionId;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 虚拟用户名
     */
    private String userName;
    /**
     * 虚拟好评内容
     */
    private String highOpinion;

    public Integer getOpinionId() {
        return opinionId;
    }

    public void setOpinionId(Integer opinionId) {
        this.opinionId = opinionId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getHighOpinion() {
        return highOpinion;
    }

    public void setHighOpinion(String highOpinion) {
        this.highOpinion = highOpinion;
    }
}
