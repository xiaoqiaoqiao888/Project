package com.camelot.pmt.service.impl;

import com.camelot.pmt.mapper.LogSysUserRoleMapper;
import com.camelot.pmt.model.LogSysUserRole;
import com.camelot.pmt.service.LogSysUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class LogSysUserRoleServiceImpl implements LogSysUserRoleService {

    @Autowired
    private LogSysUserRoleMapper logSysUserRoleMapper;

    @Override
    public void insertLogUserRole(List<LogSysUserRole> logSysUserRoleList) {
        logSysUserRoleMapper.insertLogUserRole(logSysUserRoleList);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<LogSysUserRole> selectLogSysUserRoleList(LogSysUserRole logSysUserRole) {

        return logSysUserRoleMapper.selectLogSysUserRoleList(logSysUserRole);
    }

}
