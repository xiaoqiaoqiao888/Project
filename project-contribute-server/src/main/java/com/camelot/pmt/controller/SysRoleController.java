package com.camelot.pmt.controller;

import com.camelot.pmt.model.SysRole;
import com.camelot.pmt.service.SysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * 角色
 *
 * @author zlh
 * @date 2018/5/10 10:06
 */

@RestController
@RequestMapping(value = "/sys-role")
@Api(value = "角色管理接口", description = "角色管理接口")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    @PostMapping(value = "")
    @ApiOperation(value = "添加角色", notes = "添加角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleName", value = "角色名称", required = true,
                    paramType = "form", dataType = "string"),
            @ApiImplicitParam(name = "roleDesc", value = "角色描述",
                    paramType = "form", dataType = "string"),
            @ApiImplicitParam(name = "state", value = "角色状态0无效1有效",
                    paramType = "form", dataType = "Integer") })
    public ResponseEntity<String> insert(@ApiIgnore SysRole sysRole) {
        boolean result = sysRoleService.insertSysRole(sysRole);
        if (result) {
            return ResponseEntity.status(HttpStatus.OK).body("添加角色成功");
        }
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("添加角色失败");
    }

    @DeleteMapping(value = "")
    @ApiOperation(value = "删除角色", notes = "删除角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "角色id", required = true,
                    paramType = "query", dataType = "Integer") })
    public ResponseEntity<String> delete(Integer id) {
        boolean result = sysRoleService.deleteById(id);
        if (result) {
            return ResponseEntity.status(HttpStatus.OK).body("删除角色成功");
        }
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("删除角色失败");
    }

    @PutMapping(value = "")
    @ApiOperation(value = "修改角色", notes = "修改角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "角色id", required = true,
                    paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "roleName", value = "角色名称",
                    paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "roleDesc", value = "角色描述",
                    paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "state", value = "角色状态0无效1有效",
                    paramType = "query", dataType = "Integer") })
    public ResponseEntity<String> update(@ApiIgnore SysRole sysRole) {
        boolean result = sysRoleService.updateById(sysRole);
        if (result) {
            return ResponseEntity.status(HttpStatus.OK).body("修改角色成功");
        }
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("修改角色失败");
    }

    @GetMapping(value = "/by-role-id")
    @ApiOperation(value = "查看角色详情", notes = "查看角色详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "角色id", required = true,
                    paramType = "query", dataType = "Integer") })
    public ResponseEntity<SysRole> selectById(Integer id) {
        SysRole sysRole = sysRoleService.selectById(id);
        return ResponseEntity.ok(sysRole);
    }

    @GetMapping(value = "")
    @ApiOperation(value = "查看所有角色", notes = "查看所有角色")
    public ResponseEntity<?> selectAll() {
        List<SysRole> sysRoles = sysRoleService.selectAll();
        return ResponseEntity.ok(sysRoles);
    }

    @GetMapping(value = "/by-user-id")
    @ApiOperation(value = "根据用户id查看所拥有的角色", notes = "根据用户id查看所拥有的角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户id", required = true,
                    paramType = "query", dataType = "Integer") })
    public ResponseEntity<?> selectRoleByUserId(Integer id) {
        List<SysRole> sysRoles = sysRoleService.selectRoleByUserId(id);
        return ResponseEntity.ok(sysRoles);
    }
}
