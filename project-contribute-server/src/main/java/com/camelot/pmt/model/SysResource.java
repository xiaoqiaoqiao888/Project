package com.camelot.pmt.model;

import java.util.Date;
import java.util.List;

public class SysResource {
    /**
     * id
     */
    private Integer id;

    /**
     * 父ID
     */
    private Integer parentId;

    /**
     * 资源名称
     */
    private String text;

    /**
     * 资源路径
     */
    private String href;

    /**
     * 图标
     */
    private String iconcls;

    /**
     * 资源类型
     */
    private Integer type;

    /**
     * 权限标示
     */
    private String permission;

    /**
     * 排序
     */
    private Integer sortNo;

    /**
     * 显示标示 0-不显示 1-显示
     */
    private Integer isShow;

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
     * 实体列表
     */
    private List<SysResource> childList;

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
     * 父ID
     *
     * @return parent_id 父ID
     */
    public Integer getParentId() {
        return parentId;
    }

    /**
     * 父ID
     *
     * @param parentId
     *            父ID
     */
    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    /**
     * 资源名称
     *
     * @return text 资源名称
     */
    public String getText() {
        return text;
    }

    /**
     * 资源名称
     *
     * @param text
     *            资源名称
     */
    public void setText(String text) {
        this.text = text == null ? null : text.trim();
    }

    /**
     * 资源路径
     *
     * @return href 资源路径
     */
    public String getHref() {
        return href;
    }

    /**
     * 资源路径
     *
     * @param href
     *            资源路径
     */
    public void setHref(String href) {
        this.href = href == null ? null : href.trim();
    }

    /**
     * 图标
     *
     * @return iconCls 图标
     */
    public String getIconcls() {
        return iconcls;
    }

    /**
     * 图标
     *
     * @param iconcls
     *            图标
     */
    public void setIconcls(String iconcls) {
        this.iconcls = iconcls == null ? null : iconcls.trim();
    }

    /**
     * 资源类型
     *
     * @return type 资源类型
     */
    public Integer getType() {
        return type;
    }

    /**
     * 资源类型
     *
     * @param type
     *            资源类型
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 权限标示
     *
     * @return permission 权限标示
     */
    public String getPermission() {
        return permission;
    }

    /**
     * 权限标示
     *
     * @param permission
     *            权限标示
     */
    public void setPermission(String permission) {
        this.permission = permission == null ? null : permission.trim();
    }

    /**
     * 排序
     *
     * @return sort_no 排序
     */
    public Integer getSortNo() {
        return sortNo;
    }

    /**
     * 排序
     *
     * @param sortNo
     *            排序
     */
    public void setSortNo(Integer sortNo) {
        this.sortNo = sortNo;
    }

    /**
     * 显示标示 0-不显示 1-显示
     *
     * @return is_show 显示标示 0-不显示 1-显示
     */
    public Integer getIsShow() {
        return isShow;
    }

    /**
     * 显示标示 0-不显示 1-显示
     *
     * @param isShow
     *            显示标示 0-不显示 1-显示
     */
    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
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

    public List<SysResource> getChildList() {
        return childList;
    }

    public void setChildList(List<SysResource> childList) {
        this.childList = childList;
    }
}