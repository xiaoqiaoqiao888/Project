package com.camelot.pmt.model;

import java.util.Date;

public class LogTask extends LogTaskKey {
    private Integer projectId;

    private Integer stageId;

    private Integer workId;

    private Integer taskId;

    private String taskName;

    private String taskCode;

    private Integer taskValue;

    private Integer expTaskTime;

    private Double relTaskTime;

    private Date relStartTime;

    private Date relEndTime;

    private Date expStartTime;

    private Date expEndTime;

    private Integer priority;

    private Integer taskState;

    private Integer taskPersonId;

    private Integer taskSchedule;

    private String operateDesc;

    private Integer state;

    private Integer createBy;

    private Date createTime;

    private Integer updateBy;

    private Date updateTime;

    private String taskDesc;

    private Task task;

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getStageId() {
        return stageId;
    }

    public void setStageId(Integer stageId) {
        this.stageId = stageId;
    }

    public Integer getWorkId() {
        return workId;
    }

    public void setWorkId(Integer workId) {
        this.workId = workId;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName == null ? null : taskName.trim();
    }

    public String getTaskCode() {
        return taskCode;
    }

    public void setTaskCode(String taskCode) {
        this.taskCode = taskCode == null ? null : taskCode.trim();
    }

    public Integer getTaskValue() {
        return taskValue;
    }

    public void setTaskValue(Integer taskValue) {
        this.taskValue = taskValue;
    }

    public Integer getExpTaskTime() {
        return expTaskTime;
    }

    public void setExpTaskTime(Integer expTaskTime) {
        this.expTaskTime = expTaskTime;
    }

    public Double getRelTaskTime() {
        return relTaskTime;
    }

    public void setRelTaskTime(Double relTaskTime) {
        this.relTaskTime = relTaskTime;
    }

    public Date getRelStartTime() {
        return relStartTime;
    }

    public void setRelStartTime(Date relStartTime) {
        this.relStartTime = relStartTime;
    }

    public Date getRelEndTime() {
        return relEndTime;
    }

    public void setRelEndTime(Date relEndTime) {
        this.relEndTime = relEndTime;
    }

    public Date getExpStartTime() {
        return expStartTime;
    }

    public void setExpStartTime(Date expStartTime) {
        this.expStartTime = expStartTime;
    }

    public Date getExpEndTime() {
        return expEndTime;
    }

    public void setExpEndTime(Date expEndTime) {
        this.expEndTime = expEndTime;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getTaskState() {
        return taskState;
    }

    public void setTaskState(Integer taskState) {
        this.taskState = taskState;
    }

    public Integer getTaskPersonId() {
        return taskPersonId;
    }

    public void setTaskPersonId(Integer taskPersonId) {
        this.taskPersonId = taskPersonId;
    }

    public Integer getTaskSchedule() {
        return taskSchedule;
    }

    public void setTaskSchedule(Integer taskSchedule) {
        this.taskSchedule = taskSchedule;
    }

    public String getOperateDesc() {
        return operateDesc;
    }

    public void setOperateDesc(String operateDesc) {
        this.operateDesc = operateDesc == null ? null : operateDesc.trim();
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Integer updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getTaskDesc() {
        return taskDesc;
    }

    public void setTaskDesc(String taskDesc) {
        this.taskDesc = taskDesc == null ? null : taskDesc.trim();
    }
}