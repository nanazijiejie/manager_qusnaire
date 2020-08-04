/*
 * 类名称:ExamIndexRelEntity.java
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
import java.util.Date;

/**
 * 实体
 *
 * @author lipengjun
 * @date 2019-10-09 15:21:52
 */
@Data
@TableName("tower_exam_index_rel")
public class ExamIndexRelEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 考核项ID
     */
    //@TableId
    private Integer examItemId;
    /**
     * 考核指标项ID
     */
    private Integer indexItemId;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 创建人
     */
    private String createOperator;
    /**
     * 占比
     */
    private String percent;
}
