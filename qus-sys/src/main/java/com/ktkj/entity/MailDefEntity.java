/*
 * 类名称:MailDefEntity.java
 * 包名称:com.ktkj.entity
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-10-17 20:33:03        lipengjun     初版做成
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
 * @date 2019-10-17 20:33:03
 */
@Data
@TableName("tower_mail_def")
public class MailDefEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 发件人
     */
    private String mailFrom;
    /**
     * 发件人密码
     */
    private String passwordMailFrom;
    /**
     * 邮件标题
     */
    private String mailTitle;
    /**
     * 邮件服务器域名
     */
    private String mailHost;
    /**
     * 问卷系统链接地址
     */
    private String qusUrl;
    /**
     * 邮件内容
     */
    private String qusContent;
    /**
     * ID
     */
    @TableId
    private Integer mailId;
    /**
     * 0:在用，1:删除
     */
    private String delFlag;
}
