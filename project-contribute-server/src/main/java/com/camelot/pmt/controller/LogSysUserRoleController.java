package com.camelot.pmt.controller;

import com.camelot.pmt.model.LogSysUserRole;
import com.camelot.pmt.service.LogSysUserRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * @author zsf
 * @date 2018/5/21 11:31
 */
@RestController
@RequestMapping(value = "log-sys-user-role")
@Api(value = "用户角色关联日志管理接口", description = "用户角色关联日志管理接口")
public class LogSysUserRoleController {

    @Autowired
    private LogSysUserRoleService logSysUserRoleService;

    @GetMapping(value = "")
    @ApiOperation(value = "查询所有用户角色关联日志", notes = "查询所有用户角色关联日志")
    public List<LogSysUserRole> selectLogSysUserRoleList(@ApiIgnore LogSysUserRole logSysUserRole) {

        return logSysUserRoleService.selectLogSysUserRoleList(logSysUserRole);
    }
}
