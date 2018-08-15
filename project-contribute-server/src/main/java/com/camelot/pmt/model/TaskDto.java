package com.camelot.pmt.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: lyh
 * @CreateDate: 2018/5/23 11:02
 * @Description:
 */
public class TaskDto {
    /**
     * ID
     */
    private Integer id;

    /**
     * 项目id
     */
    private Integer projectId;
    /**
     * 任务消耗成本占比
     */
    private BigDecimal useCostRatio;

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
    private Date relStartTime;

    /**
     * 任务实际结束时间
     */

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
     * 优先级
     */
    private Integer priority;

    /**
     * 复杂度
     */
    private Integer complexity;

    /**
     * 任务状态
     */
    private Integer taskState;

    /**
     * 负责人ID
     */
    private Integer taskPersonId;

    /**
     * 任务进度 %
     */
    private Integer taskSchedule;

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
    @DateTimeFormat(pattern = "yyyy-MM-ddHH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 修改人
     */
    private Integer updateBy;

    /**
     * 修改时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-ddHH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 任务描述
     */
    private String taskDesc;

    /**
     * ID
     *
     * @return id ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * ID
     *
     * @param id
     *            ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 项目id
     *
     * @return project_id 项目id
     */
    public Integer getProjectId() {
        return projectId;
    }

    /**
     * 项目id
     *
     * @param projectId
     *            项目id
     */
    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    /**
     * 阶段id
     *
     * @return stage_id 阶段id
     */
    public Integer getStageId() {
        return stageId;
    }

    /**
     * 阶段id
     *
     * @param stageId
     *            阶段id
     */
    public void setStageId(Integer stageId) {
        this.stageId = stageId;
    }

    /**
     * 工程包id
     *
     * @return work_id 工程包id
     */
    public Integer getWorkId() {
        return workId;
    }

    /**
     * 工程包id
     *
     * @param workId
     *            工程包id
     */
    public void setWorkId(Integer workId) {
        this.workId = workId;
    }

    /**
     * 任务名称
     *
     * @return task_name 任务名称
     */
    public String getTaskName() {
        return taskName;
    }

    /**
     * 任务名称
     *
     * @param taskName
     *            任务名称
     */
    public void setTaskName(String taskName) {
        this.taskName = taskName == null ? null : taskName.trim();
    }

    /**
     * 任务编码
     *
     * @return task_code 任务编码
     */
    public String getTaskCode() {
        return taskCode;
    }

    /**
     * 任务编码
     *
     * @param taskCode
     *            任务编码
     */
    public void setTaskCode(String taskCode) {
        this.taskCode = taskCode == null ? null : taskCode.trim();
    }

    /**
     * 任务价值点
     *
     * @return task_value 任务价值点
     */
    public Integer getTaskValue() {
        return taskValue;
    }

    /**
     * 任务价值点
     *
     * @param taskValue
     *            任务价值点
     */
    public void setTaskValue(Integer taskValue) {
        this.taskValue = taskValue;
    }

    public Integer getExpTaskTime() {
        return expTaskTime;
    }

    public void setExpTaskTime(Integer expTaskTime) {
        this.expTaskTime = expTaskTime;
    }

    public Integer getRelTaskTime() {
        return relTaskTime;
    }

    public void setRelTaskTime(Integer relTaskTime) {
        this.relTaskTime = relTaskTime;
    }

    /**
     * 任务实际开始时间
     *
     * @return rel_start_time 任务实际开始时间
     */
    public Date getRelStartTime() {
        return relStartTime;
    }

    /**
     * 任务实际开始时间
     *
     * @param relStartTime
     *            任务实际开始时间
     */
    public void setRelStartTime(Date relStartTime) {
        this.relStartTime = relStartTime;
    }

    /**
     * 任务实际结束时间
     *
     * @return rel_end_time 任务实际结束时间
     */
    public Date getRelEndTime() {
        return relEndTime;
    }

    /**
     * 任务实际结束时间
     *
     * @param relEndTime
     *            任务实际结束时间
     */
    public void setRelEndTime(Date relEndTime) {
        this.relEndTime = relEndTime;
    }

    /**
     * 任务预计开始时间
     *
     * @return exp_start_time 任务预计开始时间
     */
    public Date getExpStartTime() {
        return expStartTime;
    }

    /**
     * 任务预计开始时间
     *
     * @param expStartTime
     *            任务预计开始时间
     */
    public void setExpStartTime(Date expStartTime) {
        this.expStartTime = expStartTime;
    }

    /**
     * 任务预计结束时间
     *
     * @return exp_end_time 任务预计结束时间
     */
    public Date getExpEndTime() {
        return expEndTime;
    }

    /**
     * 任务预计结束时间
     *
     * @param expEndTime
     *            任务预计结束时间
     */
    public void setExpEndTime(Date expEndTime) {
        this.expEndTime = expEndTime;
    }

    /**
     * 优先级
     *
     * @return priority 优先级
     */
    public Integer getPriority() {
        return priority;
    }

    /**
     * 优先级
     *
     * @param priority
     *            优先级
     */
    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    /**
     * 复杂度
     *
     * @return complexity 复杂度
     */
    public Integer getComplexity() {
        return complexity;
    }

    /**
     * 复杂度
     *
     * @param complexity
     *            复杂度
     */
    public void setComplexity(Integer complexity) {
        this.complexity = complexity;
    }

    /**
     * 任务状态
     *
     * @return task_state 任务状态
     */
    public Integer getTaskState() {
        return taskState;
    }

    /**
     * 任务状态
     *
     * @param taskState
     *            任务状态
     */
    public void setTaskState(Integer taskState) {
        this.taskState = taskState;
    }

    /**
     * 负责人ID
     *
     * @return task_person_id 负责人ID
     */
    public Integer getTaskPersonId() {
        return taskPersonId;
    }

    /**
     * 负责人ID
     *
     * @param taskPersonId
     *            负责人ID
     */
    public void setTaskPersonId(Integer taskPersonId) {
        this.taskPersonId = taskPersonId;
    }

    /**
     * 任务进度 %
     *
     * @return task_schedule 任务进度 %
     */
    public Integer getTaskSchedule() {
        return taskSchedule;
    }

    /**
     * 任务进度 %
     *
     * @param taskSchedule
     *            任务进度 %
     */
    public void setTaskSchedule(Integer taskSchedule) {
        this.taskSchedule = taskSchedule;
    }

    /**
     * 状态值 0-无效 1-有效
     *
     * @return state 状态值 0-无效 1-有效
     */
    public Integer getState() {
        return state;
    }

    /**
     * 状态值 0-无效 1-有效
     *
     * @param state
     *            状态值 0-无效 1-有效
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * 创建人
     *
     * @return create_by 创建人
     */
    public Integer getCreateBy() {
        return createBy;
    }

    /**
     * 创建人
     *
     * @param createBy
     *            创建人
     */
    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
    }

    /**
     * 创建时间
     *
     * @return create_time 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 创建时间
     *
     * @param createTime
     *            创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 修改人
     *
     * @return update_by 修改人
     */
    public Integer getUpdateBy() {
        return updateBy;
    }

    /**
     * 修改人
     *
     * @param updateBy
     *            修改人
     */
    public void setUpdateBy(Integer updateBy) {
        this.updateBy = updateBy;
    }

    /**
     * 修改时间
     *
     * @return update_time 修改时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 修改时间
     *
     * @param updateTime
     *            修改时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 任务描述
     *
     * @return task_desc 任务描述
     */
    public String getTaskDesc() {
        return taskDesc;
    }

    /**
     * 任务描述
     *
     * @param taskDesc
     *            任务描述
     */
    public void setTaskDesc(String taskDesc) {
        this.taskDesc = taskDesc == null ? null : taskDesc.trim();
    }

    public BigDecimal getUseCostRatio() {
        return useCostRatio;
    }

    public void setUseCostRatio(BigDecimal useCostRatio) {
        this.useCostRatio = useCostRatio;
    }
}
