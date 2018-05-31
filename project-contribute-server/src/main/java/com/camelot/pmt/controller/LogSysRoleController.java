package com.camelot.pmt.controller;

import com.camelot.pmt.model.LogSysRole;
import com.camelot.pmt.service.LogSysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author zlh
 * @date 2018/5/21 11:31
 */
@RestController
@RequestMapping(value = "log-sys-role")
@Api(value = "角色日志管理接口", description = "角色日志管理接口")
public class LogSysRoleController {

    @Autowired
    private LogSysRoleService logSysRoleService;

    @GetMapping(value = "")
    @ApiOperation(value = "查询所有角色日志", notes = "查询所有角色日志")
    public List<LogSysRole> selectLogSysRole() {
        return logSysRoleService.selectAllLogSysRole();
    }
}
