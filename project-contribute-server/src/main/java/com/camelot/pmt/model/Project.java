package com.camelot.pmt.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Project {

    /**
     * ID
     */
    private Integer id;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 项目编码
     */
    private String projectCode;

    /**
     * 项目预算
     */
    private BigDecimal projectBudget;

    /**
     * 项目预计开始时间
     */
    private String startTime;

    /**
     * 项目实际开始时间
     */
    private String actualTime;

    /**
     * 项目预计结束时间
     */
    private String endTime;

    /**
     * 项目实际结束时间
     */
    private String finshTime;

    /**
     * 项目阶段 0-未开始 1-进行中 3-已完成 4-延期进行中
     */
    private Integer projectState;

    /**
     * 状态值 0-无效 1-有效
     */
    private Integer state;

    /**
     * 创建人
     */
    private Integer createBy;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 修改人
     */
    private Integer updateBy;

    /**
     * 修改时间
     */
    private String updateTime;

    /**
     * 项目描述
     */
    private String projectDesc;

    private int projectId;

    private String operateDesc; // 描述

    private String realName;// 用户的真实姓名

    private List<ProjectUser> projectUserList = new ArrayList<>();

}