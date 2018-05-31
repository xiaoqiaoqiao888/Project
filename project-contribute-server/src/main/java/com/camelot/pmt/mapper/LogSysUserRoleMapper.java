package com.camelot.pmt.mapper;

import com.camelot.pmt.model.LogSysUserRole;

import java.util.List;

public interface LogSysUserRoleMapper {
    /**
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * @mbggenerated
     */
    int insert(LogSysUserRole record);

    /**
     * @mbggenerated
     */
    int insertSelective(LogSysUserRole record);

    /**
     * @mbggenerated
     */
    LogSysUserRole selectByPrimaryKey(Integer id);

    /**
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(LogSysUserRole record);

    /**
     * @mbggenerated
     */
    int updateByPrimaryKey(LogSysUserRole record);

    /**
     * @mbggenerated
     */
    List<LogSysUserRole> selectLogSysUserRoleList(LogSysUserRole logSysUserRole);

    /**
     * @mbggenerated
     */
    void insertLogUserRole(List<LogSysUserRole> list);
}