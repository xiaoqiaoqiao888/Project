package com.camelot.pmt.controller;

import com.camelot.pmt.model.SysGroup;
import com.camelot.pmt.model.SysGroupDTO;
import com.camelot.pmt.model.SysUser;
import com.camelot.pmt.model.SysUserDTO;
import com.camelot.pmt.model.SysUserGroup;
import com.camelot.pmt.service.SysGroupService;
import com.camelot.pmt.utils.Constant;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @Author: lxk
 * @CreateDate: 2018/5/14 15:43
 * @Description: 部门组管理--成员管理controller
 */
@RestController
@RequestMapping("/sys-group")
@Api(value = "部门组管理--成员管理", description = "部门组管理--成员管理")
public class SysGroupController {

    @Autowired
    SysGroupService sysGroupService;

    /**
     * 添加系统部门组
     *
     * @param sysGroup 部门组实体
     */
    @PostMapping(value = "")
    @ApiOperation(value = "添加部门组", notes = "添加部门组")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "groupName", value = "部门组名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "groupDesc", value = "部门组描述", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "pId", value = "父级ID", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "groupLevel", value = "部门级别", paramType = "query", dataType = "int")})
    public ResponseEntity<String> save(@RequestBody SysGroup sysGroup) {
        boolean flag = sysGroupService.add(sysGroup);
        if (flag) {
            return ResponseEntity.ok(Constant.OperateDesc.ADD + Constant.OperateDesc.SUCCESS);
        }
        return ResponseEntity.ok(Constant.OperateDesc.ADD + Constant.OperateDesc.FAIL);
    }

    /**
     * 删除部门组
     *
     * @param id 部门组id
     */
    @DeleteMapping(value = "")
    @ApiOperation(value = "删除部门组", notes = "删除部门组")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "部门组idid", paramType = "query", dataType = "int")})
    public ResponseEntity<String> delete(Integer id) {
        boolean flag = sysGroupService.delete(id);
        if (flag) {
            return ResponseEntity.ok(Constant.OperateDesc.DELETE + Constant.OperateDesc.SUCCESS);
        }
        return ResponseEntity.ok(Constant.OperateDesc.DELETE + Constant.OperateDesc.FAIL);
    }

    /**
     * 编辑部门组
     *
     * @param sysGroup 部门组实体
     */
    @PostMapping("/update")
    @ApiOperation(value = "编辑部门组", notes = "编辑部门组")
    @ApiImplicitParams(value = {@ApiImplicitParam(name = "id", value = "部门组id", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pId", value = "父级id", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "groupName", value = "部门组名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "groupDesc", value = "部门组描述", paramType = "query", dataType = "String")})
    public ResponseEntity<String> update(@RequestBody SysGroup sysGroup) {
        boolean flag = sysGroupService.update(sysGroup);
        if (flag) {
            return ResponseEntity.ok(Constant.OperateDesc.UPDATE + Constant.OperateDesc.SUCCESS);
        }
        return ResponseEntity.ok(Constant.OperateDesc.UPDATE + Constant.OperateDesc.FAIL);
    }

    /**
     * 查询单个部门组
     *
     * @param id 字典类型id
     */
    @GetMapping("/simple-group")
    @ApiOperation(value = "查询单个部门组", notes = "查询单个部门组")
    @ApiImplicitParam(name = "id", value = "部门组id", paramType = "query", dataType = "int")
    public ResponseEntity<SysGroup> get(Integer id) {
        SysGroup sysGroup = sysGroupService.get(id);
        return ResponseEntity.ok(sysGroup);
    }

    /**
     * 通用条件查询部门组
     *
     * @param sysGroup 部门组实体
     */
    @PostMapping("/list")
    @ApiOperation(value = "通用条件查询部门组", notes = "通用条件查询部门组")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "部门组id", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pId", value = "父级id", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "groupName", value = "部门组名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "groupDesc", value = "部门组描述", paramType = "query", dataType = "String")})
    public ResponseEntity<List<SysGroup>> list(@RequestBody SysGroup sysGroup) {
        List<SysGroup> sysGroupList = sysGroupService.list(sysGroup);
        return ResponseEntity.ok(sysGroupList);
    }

    /**
     * 添加成员到部门组
     *
     * @param sysUserGroup 部门组成员实体
     */
    @PostMapping(value = "sys-user-group")
    @ApiOperation(value = "添加部门组成员", notes = "添加部门组成员")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "groupId", value = "组ID", paramType = "query", dataType = "int")})
    public ResponseEntity<String> saveSysUserGroup(@RequestBody SysUserGroup sysUserGroup) {
        boolean flag = sysGroupService.addSysUserGroup(sysUserGroup);
        if (flag) {
            return ResponseEntity.ok(Constant.OperateDesc.ADD + Constant.OperateDesc.SUCCESS);
        }
        return ResponseEntity.ok(Constant.OperateDesc.ADD + Constant.OperateDesc.FAIL);
    }

    /**
     * 删除部门组
     *
     * @param id 部门组id
     */
    @DeleteMapping(value = "group-user")
    @ApiOperation(value = "删除部门组成员", notes = "删除部门组成员")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "部门组成员id", paramType = "query", dataType = "int")})
    public ResponseEntity<String> deleteSysUserGroup(Integer id) {
        boolean flag = sysGroupService.deleteSysUserGroup(id);
        if (flag) {
            return ResponseEntity.ok(Constant.OperateDesc.DELETE + Constant.OperateDesc.SUCCESS);
        }
        return ResponseEntity.ok(Constant.OperateDesc.DELETE + Constant.OperateDesc.FAIL);
    }

    /**
     * 分页条件查询组成员
     *
     * @param sysUserDTO 组织部门员工实体
     */
    @PostMapping("/list-group-user")
    @ApiOperation(value = "分页条件查询组成员列表", notes = "分页条件查询组成员列表")
    @ApiImplicitParams({@ApiImplicitParam(name = "pageNum", value = "页码", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "页大小", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "userNo", value = "员工编号", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "realName", value = "员工姓名", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "state", value = "状态", paramType = "query", dataType = "String")})
    public ResponseEntity<PageInfo<Map<String, Object>>> listGroupUser(@RequestBody SysUserDTO sysUserDTO) {
        PageInfo<Map<String, Object>> sysDictTypeList = sysGroupService.listGroupUser(sysUserDTO);
        return ResponseEntity.ok(sysDictTypeList);
    }

    /**
     * 根据部门组ID查询该部门下所有成员
     *
     * @param id 部门组id
     */
    @GetMapping("/group-user-list")
    @ApiOperation(value = "查询部门组下所有成员", notes = "查询部门组下所有成员")
    @ApiImplicitParam(name = "id", value = "部门组id", paramType = "query", dataType = "int")
    public ResponseEntity<List<SysUser>> groupUserList(@RequestParam(required = false) Integer id) {
        List<SysUser> sysUserList = sysGroupService.userListByGroupId(id);
        return ResponseEntity.ok(sysUserList);
    }

    /**
     * 树形结构展示部门组信息
     */
    @GetMapping("/tree-list")
    @ApiOperation(value = "树形结构展示部门组信息", notes = "树形结构展示部门组信息")
    public ResponseEntity<List<SysGroupDTO>> treeList() {
        List<SysGroupDTO> sysGroupList = sysGroupService.treeList();
        return ResponseEntity.ok(sysGroupList);
    }

    /**
     * 根据部门组ID查询该部门下未分配角色的人员
     *
     * @param groupId 部门组id
     * @param roleId  角色id
     */
    @GetMapping("/group-user-list-no-role")
    @ApiOperation(value = "查询该部门下未分配角色的人员", notes = "部门下未分配角色的人员")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "groupId", value = "部门组id", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "roleId", value = "角色id", paramType = "query", dataType = "int")})
    public ResponseEntity<List<SysUser>> groupUserListNoRole(Integer groupId, Integer roleId) {
        List<SysUser> sysUserList = sysGroupService.userListNoRoleByGroupId(groupId, roleId);
        return ResponseEntity.ok(sysUserList);
    }

    /**
     * 根据部门组ID查询该部门下未分配项目的成员
     *
     * @param groupId   部门组id
     * @param projectId 角色id
     * @param realName  成员姓名
     */
    @GetMapping("/group-user-list-no-project")
    @ApiOperation(value = "查询该部门下未分配项目的人员", notes = "部门下未分配项目的人员")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "groupId", value = "部门组id", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "projectId", value = "项目id", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "realName", value = "成员姓名", paramType = "query", dataType = "string")})
    public ResponseEntity<List<SysUser>> groupUserListNoProject(Integer groupId, Integer projectId, String realName) {
        List<SysUser> sysUserList = sysGroupService.userListNoProjectByGroupId(groupId, projectId, realName);
        return ResponseEntity.ok(sysUserList);
    }
}
