package com.camelot.pmt.service;

import com.camelot.pmt.model.LogSysUserRole;

import java.util.List;

public interface LogSysUserRoleService {

    void insertLogUserRole(List<LogSysUserRole> logSysUserRoleList);

    List<LogSysUserRole> selectLogSysUserRoleList(LogSysUserRole logSysUserRole);

}
