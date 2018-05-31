package com.camelot.pmt.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @FileName: WorkDTO
 * @Description: 职能包清单列表数据接收传输
 * @author: xueyj
 * @create: 2018-05-17 10:36
 */
@Data
public class WorkDTO {

    /**
     * ID
     */
    private Integer id;

    /**
     * 项目ID
     */
    private Integer projectId;

    /**
     * 阶段ID
     */
    private Integer stageId;

    /**
     * 工程包名称
     */
    private String workName;

    /**
     * 工程包预算
     */
    private BigDecimal workBudget;

    /**
     * 价值分
     */
    private Integer workValue;

    /**
     * 工程包预计开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-ddHH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    /**
     * 工程包预计结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-ddHH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    /**
     * 工程包业务状态自定
     */
    private Integer workState;

    /**
     * 工程包类型
     */
    private Integer workType;

    /**
     * 状态值 0-无效 1-有效
     */
    private Integer state;

    /**
     * 创建人
     */
    private Integer createBy;

    /**
     * 修改人真实姓名
     */
    private String createUserName;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-ddHH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 修改人id
     */
    private Integer updateBy;


    /**
     * 修改时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-ddHH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 工程包描述
     */
    private String workDesc;

    /**
     * 提交时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-ddHH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date endSubmitTime;

    /**
     * 工程名称
     */
    private String projectName;

    /**
     * 功能包预算是否超标：0：未超标；1：已超标
     */
    private Integer workCostStatus;

    /**
     * 创建人姓名
     */
    private String creatUserName;

    /**
     * 修改人姓名
     */
    private String updateUserName;

    /**
     * 延期天数
     */
    private Integer elapsedTime;

    /**
     * 功能包任务预估总时间
     */
    private Integer expTaskTime;

    /**
     * 任务总消耗（根据任务消耗记录表统计）
     */
    private String workCost;

    /**
     * 当前任务所在阶段预算
     */
    private String stageBudget;

    /**
     * 当前功能包任务消耗，占所在阶段预算百分比
     */
    private String stageBudgetSchedule;

    /**
     * 任务完成百分比
     */
    private String taskSchedule;

    /**
     * 当前功能包下总任务数
     */
    private Integer taskTotal;

    /**
     * 当前功能包下已完成/待验收任务数
     */
    private Integer taskCompletedCount;

    /**
     * 工程负责人名称
     */
    private String projectUserName;
}
