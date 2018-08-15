package com.camelot.pmt.controller;

import com.camelot.pmt.model.SysUser;
import com.camelot.pmt.model.SysUserDTO;
import com.camelot.pmt.service.SysUserService;
import com.camelot.pmt.service.TaskService;
import com.camelot.pmt.utils.Constant.Manager;
import com.github.pagehelper.PageInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;

import java.util.Map;

/**
 * @author qiaodj
 * @date 2018年5月10日
 */
@Slf4j
@RestController
@RequestMapping("/sys-user")
@Api(description = "用户服务")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private TaskService taskService;

    /**
     * 个人基本信息-基本资料查询
     *
     * @param sysUser
     * @return
     */
    @GetMapping("/base-info")
    @ApiOperation(value = "个人基本信息-基本资料查询", notes = "个人基本信息-基本资料查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userNo", value = "员工号", required = true, paramType = "query", dataType = "int") })
    public ResponseEntity<?> selectBaseInfo(@ApiIgnore SysUser sysUser) {
        log.info("userNo={}", sysUser.getUserNo());
        SysUserDTO sysUserDTO = sysUserService.selectBaseInfo(sysUser.getUserNo());
        return ResponseEntity.ok(sysUserDTO);
    }

    /**
     * 根据id查询用户
     *
     * @param sysUser
     * @return
     */
    @GetMapping("/by-id")
    @ApiOperation(value = "根据id查询用户", notes = "根据id查询用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户id", required = true, paramType = "query", dataType = "int") })
    public ResponseEntity<?> selectByPrimaryKey(@ApiIgnore SysUser sysUser) {
        log.info("id={}", sysUser.getId());
        sysUser = sysUserService.selectByPrimaryKey(sysUser.getId());
        return ResponseEntity.ok(sysUser);
    }

    /**
     * 根据id更新用户信息
     *
     * @param sysUser
     * @return
     */
    @PutMapping("/by-id")
    @ApiOperation(value = "根据id更新用户信息", notes = "根据id更新用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户id", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "userNo", value = "员工号", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "realName", value = "真实名", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "password", value = "密码", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "email", value = "电子邮件", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "tel", value = "电话", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "userDesc", value = "描述/备注", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "cost", value = "人员成本", required = false, paramType = "query", dataType = "double"),
            @ApiImplicitParam(name = "state", value = "状态值", required = false, paramType = "query", dataType = "int"), })
    public ResponseEntity<?> updateByPrimaryKeySelective(@ApiIgnore SysUser sysUser, HttpServletRequest request) {
        log.info("userNo={}", sysUser.getUserNo());
        int num = sysUserService.updateByPrimaryKeySelective(sysUser);
        if (num > 0) {
            return ResponseEntity.ok(Manager.UPDATE_SUCCESS);
        }
        return new ResponseEntity<Object>(Manager.REQUEST_DATA_EXCEPTION, HttpStatus.NOT_ACCEPTABLE);
    }

    /**
     * 查询所有用户(分页)
     *
     * @param pageInfo
     * @return
     */
    @GetMapping("/by-page")
    @ApiOperation(value = "查询所有用户(分页)", notes = "查询所有用户(分页)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "当前页数", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页数量", required = true, paramType = "query", dataType = "int") })
    public ResponseEntity<?> selectAllByPage(@ApiIgnore PageInfo<?> pageInfo) {
        log.info("pageNum={},pageSize={}", pageInfo.getPageNum(), pageInfo.getPageSize());
        PageInfo<SysUser> list = sysUserService.selectAllByPage(pageInfo.getPageNum(), pageInfo.getPageSize());
        return ResponseEntity.ok(list);
    }

    /**
     * 根据角色id查看对应用户
     *
     * @param roleId
     * @return
     */
    @GetMapping(value = "by-role-id")
    @ApiOperation(value = "根据角色id查看对应用户", notes = "根据角色id查看对应用户")
    @ApiImplicitParams({ @ApiImplicitParam(name = "roleId", value = "角色id", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pageNum", value = "当前页数", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页数量", required = true, paramType = "query", dataType = "int") })
    public ResponseEntity<?> selectUsersByRoleId(Integer roleId, Integer pageNum, Integer pageSize) {
        PageInfo<SysUser> sysUserPageInfo = sysUserService.selectUsersByRoleId(roleId, pageNum, pageSize);
        return ResponseEntity.ok(sysUserPageInfo);
    }

    /**
     * 根据用户名查询用户（系统设置-员工管理-添加成员时可以用到）
     *
     * @param userName
     * @return
     */
    @ApiIgnore
    @GetMapping(value = "by-userName")
    @ApiOperation(value = "根据用户名查询用户（系统设置-员工管理-添加成员时可以用到）", notes = "根据用户名查询用户（系统设置-员工管理-添加成员时可以用到）")
    @ApiImplicitParams({ @ApiImplicitParam(name = "userName", value = "用户名", required = true, paramType = "query") })
    public ResponseEntity<SysUser> queryByUserName(String userName) {
        SysUser sysUser = sysUserService.queryByUserName(userName);
        return ResponseEntity.ok(sysUser);
    }

    /**
     * 系统设置-员工管理-添加成员
     *
     * @param sysUser
     * @param groupId
     * @param roleId
     * @return
     */
    @PostMapping("/add")
    @ApiOperation(value = "系统设置-员工管理-添加成员", notes = "系统设置-员工管理-添加成员")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userNo", value = "员工号", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "realName", value = "真实名", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "email", value = "电子邮件", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "tel", value = "电话", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "cost", value = "人员成本", required = true, paramType = "query", dataType = "double"),
            @ApiImplicitParam(name = "userDesc", value = "描述/备注", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "roleId", value = "角色id", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "groupId", value = "部门组id", required = true, paramType = "query", dataType = "int") })
    public ResponseEntity<?> insert(@ApiIgnore SysUser sysUser, @ApiIgnore Integer groupId, Integer[] roleId) {
        log.info("userNo={},realName={},email={},tel={},cost={},userDesc={},roleId={},groupId={}", sysUser.getUserNo(),
                sysUser.getRealName(), sysUser.getEmail(), sysUser.getTel(), sysUser.getCost(), sysUser.getUserDesc(),
                roleId, groupId);
        SysUser sysUserDB = sysUserService.queryByUserNo(sysUser.getUserNo());
        if (sysUserDB != null) {
            return new ResponseEntity<Object>("此员工号已存在，请查证后再次添加！", HttpStatus.NOT_ACCEPTABLE);
        }
        // 保存用户表
        int num = sysUserService.insert(sysUser, roleId, groupId);
        if (num > 0) {
            return ResponseEntity.ok(Manager.INSERT_SUCCESS);
        }
        return new ResponseEntity<Object>(Manager.REQUEST_DATA_EXCEPTION, HttpStatus.NOT_ACCEPTABLE);
    }

    /**
     * 根据员工号查询用户（系统设置-员工管理-添加成员时可以用到）
     *
     * @param userNo
     * @return
     */
    @GetMapping(value = "by-userNo")
    @ApiOperation(value = "根据员工号查询用户（系统设置-员工管理-添加成员时可以用到）", notes = "根据员工号查询用户（系统设置-员工管理-添加成员时可以用到）")
    @ApiImplicitParams({ @ApiImplicitParam(name = "userNo", value = "员工号", required = true, paramType = "query") })
    public ResponseEntity<SysUser> queryByUserNo(Integer userNo) {
        log.info("userNo={}", userNo);
        SysUser sysUser = sysUserService.queryByUserNo(userNo);
        return ResponseEntity.ok(sysUser);
    }

    /**
     * 工作轨迹追溯
     *
     * @param taskPersonId
     * @param state
     * @param monthNum
     * @return
     */
    @GetMapping("/task-trace")
    @ApiOperation(value = "工作轨迹追溯", notes = "工作轨迹追溯")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskPersonId", value = "任务领取人id", required = true, paramType = "query"),
            @ApiImplicitParam(name = "state", value = "状态值", required = true, paramType = "query"),
            @ApiImplicitParam(name = "monthNum", value = "月份值", required = true, paramType = "query") })
    public ResponseEntity<?> selectPeriodTotalTask(Integer taskPersonId, Integer state, Integer monthNum) {
        log.info("taskPersonId={},state={},monthNum={}", taskPersonId, state, monthNum);
        Map<String, Object> map = taskService.selectPeriodTotalTask(taskPersonId, state, monthNum);
        return ResponseEntity.ok(map);
    }
}