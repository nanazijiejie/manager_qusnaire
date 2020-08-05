package com.ktkj.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tower_qus_exam_staff_rel")
public class QusExamStaffRel {
    private String qusStaffName;
    private String examStaffName;

    public String getQusStaffName() {
        return qusStaffName;
    }

    public void setQusStaffName(String qusStaffName) {
        this.qusStaffName = qusStaffName;
    }

    public String getExamStaffName() {
        return examStaffName;
    }

    public void setExamStaffName(String examStaffName) {
        this.examStaffName = examStaffName;
    }
}
