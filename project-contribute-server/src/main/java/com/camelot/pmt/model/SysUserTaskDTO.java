package com.camelot.pmt.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysUserTaskDTO {

    /**
     * ID
     */
    private Integer id;

    /**
     * 任务总数
     */
    private Integer totalTask;

    /**
     * 项目id
     */
    private Integer projectId;

    /**
     * 阶段id
     */
    private Integer stageId;

    /**
     * 工程包id
     */
    private Integer workId;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 任务编码
     */
    private String taskCode;

    /**
     * 任务价值点
     */
    private Integer taskValue;

    /**
     * 任务估时
     */
    private Integer expTaskTime;

    /**
     * 任务实际消耗工时
     */
    private Integer relTaskTime;

    /**
     * 任务实际开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-ddHH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date relStartTime;

    /**
     * 任务实际结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-ddHH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date relEndTime;

    /**
     * 任务预计开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-ddHH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expStartTime;

    /**
     * 任务预计结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-ddHH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expEndTime;

    /**
     * 任务状态
     */
    private Integer taskState;

    /**
     * 负责人ID
     */
    private Integer taskPersonId;

    /**
     * 状态值 0-无效 1-有效
     */
    private Integer state;

}