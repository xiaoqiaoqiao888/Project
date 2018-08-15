package com.camelot.pmt.model;

/**
 * @description: 职能能力展示 实体类
 * @author: Gnerv LiGen
 * @date: 2018-05-16
 **/
public class AbilityExhibition {

    /**
     * 用户信息
     */
    private SysUser sysUser;

    /**
     * 用户价值分
     */
    private Integer valuePoints;

    public SysUser getSysUser() {
        return sysUser;
    }

    public void setSysUser(SysUser sysUser) {
        this.sysUser = sysUser;
    }

    public Integer getValuePoints() {
        return valuePoints;
    }

    public void setValuePoints(Integer valuePoints) {
        this.valuePoints = valuePoints;
    }

    @Override
    public String toString() {
        return "AbilityExhibition{" + "sysUser=" + sysUser + ", valuePoints=" + valuePoints + '}';
    }
}
