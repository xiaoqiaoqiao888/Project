package com.camelot.pmt.model;

import java.util.Date;

public class Stage {
    /**
     * ID
     */
    private Integer id;

    /**
     * 项目ID
     */
    private Integer projectId;

    /**
     * 阶段名称
     */
    private String stageName;

    /**
     * 阶段预算
     */
    private Long stageBudget;

    /**
     * 阶段预计开始时间
     */
    private Date startTime;

    /**
     * 阶段预计结束时间
     */
    private Date endTime;

    /**
     * 阶段业务状态自定
     */
    private Integer stageState;

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
    private Date createTime;

    /**
     * 修改人
     */
    private Integer updateBy;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 最后提交时间
     */
    private Date endSubmitTime;

    /**
     * 阶段描述
     */
    private String stageDesc;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 真时名称
     */
    private String cherealname;

    /**
     * 进度
     */
    private String plan;

    /**
     * 成本
     */
    private String cost;

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
     * 项目ID
     *
     * @return project_id 项目ID
     */
    public Integer getProjectId() {
        return projectId;
    }

    /**
     * 项目ID
     *
     * @param projectId
     *            项目ID
     */
    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    /**
     * 阶段名称
     *
     * @return stage_name 阶段名称
     */
    public String getStageName() {
        return stageName;
    }

    /**
     * 阶段名称
     *
     * @param stageName
     *            阶段名称
     */
    public void setStageName(String stageName) {
        this.stageName = stageName == null ? null : stageName.trim();
    }

    /**
     * 阶段预算
     *
     * @return stage_budget 阶段预算
     */
    public Long getStageBudget() {
        return stageBudget;
    }

    /**
     * 阶段预算
     *
     * @param stageBudget
     *            阶段预算
     */
    public void setStageBudget(Long stageBudget) {
        this.stageBudget = stageBudget;
    }

    /**
     * 阶段预计开始时间
     *
     * @return start_time 阶段预计开始时间
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * 阶段预计开始时间
     *
     * @param startTime
     *            阶段预计开始时间
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * 阶段预计结束时间
     *
     * @return end_time 阶段预计结束时间
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * 阶段预计结束时间
     *
     * @param endTime
     *            阶段预计结束时间
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * 阶段业务状态自定
     *
     * @return stage_state 阶段业务状态自定
     */
    public Integer getStageState() {
        return stageState;
    }

    /**
     * 阶段业务状态自定
     *
     * @param stageState
     *            阶段业务状态自定
     */
    public void setStageState(Integer stageState) {
        this.stageState = stageState;
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
     * 最后提交时间
     *
     * @return endSubmitTime 最后提交时间
     */
    public Date getEndSubmitTime() {
        return endSubmitTime;
    }

    /**
     * 最后提交时间
     *
     * @param endSubmitTime
     *            最后提交时间
     */
    public void setEndSubmitTime(Date endSubmitTime) {
        this.endSubmitTime = endSubmitTime;
    }

    /**
     * 阶段描述
     *
     * @return stage_desc 阶段描述
     */
    public String getStageDesc() {
        return stageDesc;
    }

    /**
     * 阶段描述
     *
     * @param stageDesc
     *            阶段描述
     */
    public void setStageDesc(String stageDesc) {
        this.stageDesc = stageDesc == null ? null : stageDesc.trim();
    }

    /**
     * 用户名
     *
     * @return user_name 用户名
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 用户名
     *
     * @param userName
     *            用户名
     */
    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    /**
     * 真实名称
     *
     * @return cherealname 真实名称
     */
    public String getCherealname() {
        return cherealname;
    }

    /**
     * 真实名称
     *
     * @param cherealname
     *            真实名称
     */
    public void setCherealname(String cherealname) {
        this.cherealname = cherealname == null ? null : cherealname.trim();
    }

    /**
     * 进度
     *
     * @return 进度 进度
     */
    public String getPlan() {
        return plan;
    }

    /**
     * 预算
     *
     * @param plan
     *            进度
     */
    public void setPlan(String plan) {
        this.plan = plan == null ? null : plan.trim();
    }

    /**
     * 成本
     *
     * @return cost 成本
     */
    public String getCost() {
        return cost;
    }

    /**
     * 预算
     *
     * @param cost
     *            成本
     */
    public void setCost(String cost) {
        this.cost = cost == null ? null : cost.trim();
    }
}