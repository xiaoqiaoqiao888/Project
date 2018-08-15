package com.camelot.pmt.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectUserInfo extends ProjectUser {

    // 价值分
    private Double taskValueSum;
    // 预估工时
    private Double expTaskTimeSum;
    // 实际消耗工时
    private Double relTaskTimeSum;
    // 提交任务
    private Double submitTaskSum;
    // 完成任务
    private Double finishedTaskSum;
    // 成员真实姓名
    private String realName;
    // 成员编号（工号）
    private String userNo;
    // 职能组id
    private Integer groupId;

    private Integer page;

    private Integer size;
}
