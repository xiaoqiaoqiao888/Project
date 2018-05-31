package com.camelot.pmt.service;

import com.camelot.pmt.model.LogSysRole;

import java.util.List;

/**
 * @author zlh
 * @date 2018/5/21 10:23
 */
public interface LogSysRoleService {

    boolean insertLogSysRole(LogSysRole logSysRole);

    List<LogSysRole> selectAllLogSysRole();
}
