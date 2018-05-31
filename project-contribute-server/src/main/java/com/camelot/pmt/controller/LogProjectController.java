package com.camelot.pmt.controller;

import com.camelot.pmt.model.Project;
import com.camelot.pmt.service.LogProjectService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/project/log")
@Api(description = "项目日志", value = "项目日志")
public class LogProjectController {

    @Autowired
    private LogProjectService logProjectService;

    @RequestMapping(value = "/query", method = RequestMethod.GET)
    @ApiOperation(value = "项目日志查询", notes = "项目日志查询所有，根据创建时间倒序")
    @ApiImplicitParam(paramType = "query", name = "projectId", dataType = "int", value = "项目id",
            required = true)
    public ResponseEntity<Object> queryLog(Integer projectId) {
        if (projectId == null) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("项目id不能为空");
        }
        List<Project> projectList = logProjectService.queryAll(projectId);
        return ResponseEntity.ok(projectList);
    }
}
