package com.camelot.pmt.service.impl;

import com.camelot.pmt.mapper.LogSysRoleMapper;
import com.camelot.pmt.model.LogSysRole;
import com.camelot.pmt.service.LogSysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 角色日志
 *
 * @author zlh
 * @date 2018/5/21 10:24
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class LogSysRoleServiceImpl implements LogSysRoleService {

    @Autowired
    private LogSysRoleMapper logSysRoleMapper;

    /**
     * 添加角色日志
     *
     * @param logSysRole
     * @return
     */
    @Override
    public boolean insertLogSysRole(LogSysRole logSysRole) {
        int insertResult = logSysRoleMapper.insertLogSysRole(logSysRole);
        return insertResult == 1;
    }

    /**
     * 查询所有角色日志
     *
     * @return
     */
    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<LogSysRole> selectAllLogSysRole() {
        return logSysRoleMapper.selectAllLogSysRole();
    }
}
