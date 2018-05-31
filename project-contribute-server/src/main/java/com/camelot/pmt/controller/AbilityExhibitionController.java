package com.camelot.pmt.controller;

import com.camelot.pmt.service.AbilityExhibitionService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @description: 职能能力展示 前端控制类
 * @author: Gnerv LiGen
 * @date: 2018-05-16
 **/
@RestController
@RequestMapping(value = "/ability-exhibition")
@Api(value = "职能能力展示管理接口", description = "职能能力展示管理接口")
public class AbilityExhibitionController {

    @Autowired
    AbilityExhibitionService abilityExhibitionService;

    @ApiOperation(value = "查询展示职能组人员能力", notes = "查询展示职能组人员能力")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码",
                    paramType = "form", dataType = "Integer"),
            @ApiImplicitParam(name = "rows", value = "条数",
                    paramType = "form", dataType = "Integer"),
            @ApiImplicitParam(name = "groupId", value = "职能组id", required = true,
                    paramType = "form", dataType = "Integer"),
            @ApiImplicitParam(name = "realName", value = "用户真实名",
                    paramType = "form", dataType = "String"),
            @ApiImplicitParam(name = "cycle", value = "查询时间范围",
                    paramType = "form", dataType = "Integer")})
    @RequestMapping(value = "/select-ability-exhibition", method = RequestMethod.POST)
    public ResponseEntity<PageInfo<Map<String, Object>>> selectAbilityExhibition(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "10") Integer rows,
            Integer groupId,
            String realName,
            @RequestParam(value = "cycle", defaultValue = "0") Integer cycle) {
        PageInfo<Map<String, Object>> mapPageInfo =
                abilityExhibitionService.selectAbilityExhibition(page, rows, groupId, realName, cycle);
        return ResponseEntity.ok(mapPageInfo);
    }

    @ApiOperation(value = "查询展示职能组人员价值分明细", notes = "查询展示职能组人员价值分明细")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码",
                    paramType = "form", dataType = "Integer"),
            @ApiImplicitParam(name = "rows", value = "条数",
                    paramType = "form", dataType = "Integer"),
            @ApiImplicitParam(name = "sysUserId", value = "用户id", required = true,
                    paramType = "form", dataType = "Integer")})
    @RequestMapping(value = "/select-value-points-details", method = RequestMethod.POST)
    public ResponseEntity<PageInfo<Map<String, Object>>> selectValuePointsDetailsBySysUserId(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "10") Integer rows,
            Integer sysUserId) {

        PageInfo<Map<String, Object>> mapPageInfo =
                abilityExhibitionService.selectValuePointsDetailsBySysUserId(page, rows, sysUserId);
        return ResponseEntity.ok(mapPageInfo);
    }

    @ApiOperation(value = "查询展示职能组人员指定任务信息详情", notes = "查询展示职能组单个人员指定任务信息详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", value = "任务id", required = true,
                    paramType = "form", dataType = "Integer")})
    @RequestMapping(value = "/select-task-details", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> selectTaskDetailsByTaskId(Integer taskId) {
        Map<String, Object> stringObjectMap = abilityExhibitionService.selectTaskDetailsByTaskId(taskId);
        return ResponseEntity.ok(stringObjectMap);
    }

    @ApiOperation(value = "查询展示职能组人员任务追溯", notes = "查询展示职能组人员任务追溯")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", required = true,
                    paramType = "form", dataType = "Integer"),
            @ApiImplicitParam(name = "cycle", value = "查询时间范围",
                    paramType = "form", dataType = "Integer")})
    @RequestMapping(value = "/select-task-tracing", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> selectTaskTracing(
            Integer userId,
            @RequestParam(value = "cycle", defaultValue = "0") Integer cycle) {
        Map<String, Object> stringObjectMap = abilityExhibitionService.selectTaskTracing(userId, cycle);
        return ResponseEntity.ok(stringObjectMap);
    }

}
