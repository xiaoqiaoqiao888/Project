package com.camelot.pmt.controller;

import com.camelot.pmt.model.ProjectUserInfo;
import com.camelot.pmt.service.ProjectUserService;
import com.github.pagehelper.PageInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/project/user")
@Api(description = "项目成员管理", value = "项目成员管理")
public class ProjectUserController {

    @Autowired
    private ProjectUserService projectUserService;

    @GetMapping(value = "/by-project")
    @ApiOperation(value = "项目成员统计", notes = "项目成员统计")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "projectId", dataType = "int", value = "项目id",
                    required = true) })
    public ResponseEntity projectUser(Integer projectId) {
        Map<String, Integer> result = projectUserService.sumProjectUser(projectId);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/search")
    @ApiOperation(value = "项目成员查询（条件查询）", notes = "项目成员查询")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "projectId", dataType = "int", value = "项目id",
                    required = true),
            @ApiImplicitParam(paramType = "query", name = "realName", dataType = "string", value = "真实姓名，模糊查询",
                    required = false),
            @ApiImplicitParam(paramType = "query", name = "groupId", dataType = "int", value = "职能组id",
                    required = false),
            @ApiImplicitParam(paramType = "query", name = "page", dataType = "int", value = "页码",
            required = false),
            @ApiImplicitParam(paramType = "query", name = "size", dataType = "int", value = "每页显示条数",
            required = false) })
    public ResponseEntity search(@ApiIgnore ProjectUserInfo info) {
        if (info.getProjectId() == null) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("项目id不能为空");
        }
        if (info.getPage() == null || info.getSize() == null) {
            info.setPage(0);
            info.setSize(10);
        } else {
            info.setPage((info.getPage() - 1) * info.getSize());
        }
        PageInfo<ProjectUserInfo> list = projectUserService.search(info);
        return ResponseEntity.ok(list);
    }
}
