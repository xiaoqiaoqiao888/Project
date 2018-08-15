package com.camelot.pmt.model;

import java.util.Date;

public class SysUserRole {
    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 角色ID
     */
    private Integer roleId;

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
     * 主键ID
     *
     * @return id 主键ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * 主键ID
     *
     * @param id
     *            主键ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 用户ID
     *
     * @return user_id 用户ID
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 用户ID
     *
     * @param userId
     *            用户ID
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 角色ID
     *
     * @return role_id 角色ID
     */
    public Integer getRoleId() {
        return roleId;
    }

    /**
     * 角色ID
     *
     * @param roleId
     *            角色ID
     */
    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
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
}