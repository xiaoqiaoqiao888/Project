package com.camelot.pmt.mapper;

import com.camelot.pmt.model.LogSysRole;

import java.util.List;

public interface LogSysRoleMapper {
    /**
     * 添加日志
     *
     * @param logSysRole
     * @return int
     */
    int insertLogSysRole(LogSysRole logSysRole);

    /**
     *
     * @mbggenerated
     */
    LogSysRole selectByPrimaryKey(Integer id);

    /**
     * 查询所有角色日志
     *
     * @return LogSysRole
     */
    List<LogSysRole> selectAllLogSysRole();
}