package com.camelot.pmt.controller;

import com.camelot.pmt.service.SysUserRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zsf
 * @date 2018/5/14 17:28
 */
@RestController
@RequestMapping(value = "/sys-user-role")
@Api(value = "角色用户管理接口", description = "角色用户管理接口")
public class SysUserRoleController {

    @Autowired
    private SysUserRoleService sysUserRoleService;

    @DeleteMapping(value = "")
    @ApiOperation(value = "根据角色id删除角色下对应的员工", notes = "根据角色id删除角色下对应的员工")
    @ApiImplicitParams({ @ApiImplicitParam(name = "roleId", value = "角色id", required = true, paramType = "query"),
            @ApiImplicitParam(name = "userIds", value = "用户id集合如 1,2,3", required = true, paramType = "query") })
    public ResponseEntity<String> deleteByRoleOrUserIds(Integer roleId, Integer[] userIds) {
        if (userIds == null || userIds.length == 0) {
            throw new IllegalArgumentException("用户id不能为空");
        }
        boolean result = sysUserRoleService.deleteByRoleOrUserIds(roleId, userIds);
        if (result) {
            return ResponseEntity.status(HttpStatus.OK).body("删除成功");
        }
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("删除失败");
    }

    @PostMapping(value = "")
    @ApiOperation(value = "角色添加用户", notes = "角色添加用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色ID", required = true, paramType = "form", dataType = "int"),
            @ApiImplicitParam(name = "userIds", value = "用户ID集合如 1,2,3", required = true, paramType = "form", dataType = "int") })
    public ResponseEntity<String> roleInsertUser(Integer roleId, Integer[] userIds) {
        boolean result = sysUserRoleService.roleInsertUser(roleId, userIds);
        if (result) {
            return ResponseEntity.status(HttpStatus.OK).body("添加成功");
        }
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("添加失败");
    }

    @PutMapping(value = "")
    @ApiOperation(value = "用户赋予角色", notes = "用户赋予角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "roleIds", value = "角色ID集合如 1,2,3", paramType = "query", dataType = "int") })
    public ResponseEntity<String> updateUserRole(Integer userId, Integer[] roleIds) {
        boolean result = sysUserRoleService.updateUserRole(roleIds, userId);
        if (result) {
            return ResponseEntity.status(HttpStatus.OK).body("用户赋予角色成功");
        }
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("用户赋予角色失败");
    }

}
