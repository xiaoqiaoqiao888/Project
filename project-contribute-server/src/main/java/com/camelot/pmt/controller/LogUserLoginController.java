package com.camelot.pmt.controller;

import com.camelot.pmt.model.LogUserLogin;
import com.camelot.pmt.service.LogUserLoginService;
import com.camelot.pmt.utils.Constant;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @author gxl
 * @ClassName: LogUserLoginController
 * @Description: TODO(用户登录Controller层)
 */

@Api(description = "登录日志")
@RestController
@RequestMapping("/sys/login-log")
public class LogUserLoginController {

    @Autowired
    private LogUserLoginService logUserLoginService;

    /**
     * 查询用户登陆日志
     *
     * @return ResponseEntity 返回类型
     */
    @GetMapping("")
    @ApiOperation(value = "查询用户登陆日志", notes = "查询用户登陆日志")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码",
                    paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页数量",
                    paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "id", value = "登录用户id",
                    paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "loginUser", value = "登录用户名称",
                    paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "loginTime", value = "登录时间",
                    paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "loginIp", value = "登录IP",
                    paramType = "query", dataType = "Integer")})
    public ResponseEntity<PageInfo<?>> queryList(
            @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize,
            @ApiIgnore LogUserLogin loginUserLogin) {
        return ResponseEntity.ok(logUserLoginService.queryLogUserLoginList(pageNum, pageSize, loginUserLogin));
    }

    /**
     * 删除用户登陆日志
     *
     * @return ResponseEntity 返回类型
     */
    @DeleteMapping("")
    @ApiOperation(value = "删除用户登陆日志", notes = "删除用户登陆日志")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "登录用户ids", required = true,
                    paramType = "query", dataType = "Integer")})
    public ResponseEntity<String> delete(Long[] ids) {
        if (ids == null || ids.length == 0) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(Constant.Manager.REQUEST_DATA_EXCEPTION);
        }
        logUserLoginService.batchDeleteLogUserLogin(ids);
        return ResponseEntity.ok(Constant.Manager.DELETE_SUCCESS);
    }

}
