package com.camelot.pmt.model;

import java.util.Date;

public class SysRole {
    /**
     * id
     */
    private Integer id;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色描述
     */
    private String roleDesc;

    /**
     * 状态值 0-无效 1-有效
     */
    private Integer state;

    /**
     * 创建人
     */
    private Integer createdBy;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 修改人
     */
    private Integer updatedBy;

    /**
     * 修改时间
     */
    private Date updatedTime;

    /**
     * id
     *
     * @return id id
     */
    public Integer getId() {
        return id;
    }

    /**
     * id
     *
     * @param id
     *            id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 角色名称
     *
     * @return role_name 角色名称
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * 角色名称
     *
     * @param roleName
     *            角色名称
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName == null ? null : roleName.trim();
    }

    /**
     * 角色描述
     *
     * @return role_desc 角色描述
     */
    public String getRoleDesc() {
        return roleDesc;
    }

    /**
     * 角色描述
     *
     * @param roleDesc
     *            角色描述
     */
    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc == null ? null : roleDesc.trim();
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
     * @return created_by 创建人
     */
    public Integer getCreatedBy() {
        return createdBy;
    }

    /**
     * 创建人
     *
     * @param createdBy
     *            创建人
     */
    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * 创建时间
     *
     * @return created_time 创建时间
     */
    public Date getCreatedTime() {
        return createdTime;
    }

    /**
     * 创建时间
     *
     * @param createdTime
     *            创建时间
     */
    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    /**
     * 修改人
     *
     * @return updated_by 修改人
     */
    public Integer getUpdatedBy() {
        return updatedBy;
    }

    /**
     * 修改人
     *
     * @param updatedBy
     *            修改人
     */
    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    /**
     * 修改时间
     *
     * @return updated_time 修改时间
     */
    public Date getUpdatedTime() {
        return updatedTime;
    }

    /**
     * 修改时间
     *
     * @param updatedTime
     *            修改时间
     */
    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }
}