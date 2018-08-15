package com.camelot.pmt.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.camelot.pmt.model.LogStage;
import com.camelot.pmt.model.Project;
import com.camelot.pmt.model.Stage;
import com.camelot.pmt.model.WorkCountDTO;
import com.camelot.pmt.service.LogStageService;
import com.camelot.pmt.service.ProjectService;
import com.camelot.pmt.service.StageService;
import com.camelot.pmt.utils.Constant;
import com.github.pagehelper.PageInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/**
 * StageController class
 *
 * @author zhangzhan
 * @date 2018/5/14
 */

@RestController
@RequestMapping("/state")
@Api(description = "阶段管理", value = "阶段管理")
public class StageController {
    @Autowired
    private StageService stageService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private LogStageService logStageService;

    @GetMapping("/state-by-id")
    @ApiOperation(value = "根据阶段id查询", notes = "根据阶段id查询")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", dataType = "String", value = "阶段id", required = true) })
    public ResponseEntity<Stage> selectById(@ApiIgnore Stage stage) {
        Stage stage1 = stageService.selectById(stage);
        return ResponseEntity.ok(stage1);
    }

    @PostMapping("")
    @ApiOperation(value = "添加阶段", notes = "添加阶段")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "projectId", dataType = "String", value = "项目id", required = true),
            @ApiImplicitParam(paramType = "query", name = "stageName", dataType = "String", value = "阶段名称", required = true),
            @ApiImplicitParam(paramType = "query", name = "stageBudget", dataType = "String", value = "阶段预算", required = true),
            @ApiImplicitParam(paramType = "query", name = "startTime", dataType = "String", value = "预计开始时间", required = false),
            @ApiImplicitParam(paramType = "query", name = "endTime", dataType = "String", value = "预计结束时间", required = false),
            @ApiImplicitParam(paramType = "query", name = "stageDesc", dataType = "String", value = "阶段描述") })
    public ResponseEntity<String> insertStage(@ApiIgnore Stage stage) throws CloneNotSupportedException {
        if (stage.getStartTime() != null && stage.getEndTime() != null) {
            long time = stage.getStartTime().getTime();
            long time1 = stage.getEndTime().getTime();
            if (time > time1) {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("阶段开始时间不能大于结束时间");
            }
        }
        if (StringUtils.isEmpty(StringUtils.trim(stage.getStageName()))) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("阶段名称不能为空");
        }
        if (stage.getStageBudget() == null) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("阶段预算不能为空");
        }
        if (stage.getProjectId() == null) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("项目id不能为空");
        }
        if (stage.getStartTime() == null) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("阶段开始时间不能为空");
        }
        if (stage.getEndTime() == null) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("阶段结束时间不能为空");
        }
        Project project = projectService.selectByPrimaryKey(stage.getProjectId());
        Integer projectState = project.getProjectState();
        if (projectState == Constant.Status.COMPLETED) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("已完成不能划分阶段");
        }
        Boolean flag = stageService.insertStage(stage);
        if (flag) {
            return ResponseEntity.ok("添加成功");
        }
        return ResponseEntity.ok("添加失败！");
    }

    @PostMapping("/put")
    @ApiOperation(value = "编辑阶段", notes = "编辑阶段")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", name = "id", dataType = "String", value = "阶段id", required = true),
            @ApiImplicitParam(paramType = "query", name = "projectId", dataType = "String", value = "项目id", required = true),
            @ApiImplicitParam(paramType = "form", name = "stageName", dataType = "String", value = "阶段名称"),
            @ApiImplicitParam(paramType = "form", name = "stageBudget", dataType = "String", value = "阶段预算"),
            @ApiImplicitParam(paramType = "form", name = "startTime", dataType = "date", value = "预计开始时间(格式：xxxx/xx/xx xx:xx:xx)"),
            @ApiImplicitParam(paramType = "form", name = "endTime", dataType = "date", value = "预计结束时间(格式：xxxx/xx/xx xx:xx:xx)"),
            @ApiImplicitParam(paramType = "form", name = "stageDesc", dataType = "String", value = "阶段描述") })
    public ResponseEntity<String> updateStage(@ApiIgnore Stage stage) throws CloneNotSupportedException {
        if (stage.getStartTime() != null && stage.getEndTime() != null) {
            long time = stage.getStartTime().getTime();
            long time1 = stage.getEndTime().getTime();
            if (time > time1) {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("阶段开始时间不能大于结束时间");
            }
        }
        if (StringUtils.isEmpty(StringUtils.trim(stage.getStageName()))) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("阶段名称不能为空");
        }
        if (stage.getStageBudget() == null) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("阶段预算不能为空");
        }
        if (stage.getProjectId() == null) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("项目id不能为空");
        }
        if (stage.getStartTime() == null) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("阶段开始时间不能为空");
        }
        if (stage.getEndTime() == null) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("阶段结束时间不能为空");
        }
        Boolean flag = stageService.updateStage(stage);
        if (flag) {
            return ResponseEntity.ok("修改成功");
        }
        return ResponseEntity.ok("修改失败！");
    }

    @PostMapping("/state-sum")
    @ApiOperation(value = "阶段预算统计", notes = "阶段预算统计")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "projectId", dataType = "String", value = "项目id", required = true) })
    public ResponseEntity<Double> statisticsStage(Integer projectId) {
        Double flag = stageService.statisticsStage(projectId);
        return ResponseEntity.ok(flag);
    }

    @PostMapping("/list-page")
    @ApiOperation(value = "阶段列表", notes = "阶段列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "projectId", dataType = "String", value = "项目id", required = true),
            @ApiImplicitParam(paramType = "query", name = "pageSize", dataType = "String", value = "页码大小", required = true),
            @ApiImplicitParam(paramType = "query", name = "pageNum", dataType = "String", value = "页码", required = true),
            @ApiImplicitParam(paramType = "query", name = "stageName", dataType = "String", value = "阶段名称"),
            @ApiImplicitParam(paramType = "query", name = "stageState", dataType = "String", value = "阶段状态") })
    public ResponseEntity<PageInfo<Stage>> statisticsStage(@ApiIgnore Stage stage,
            @RequestParam(value = "pageNum", defaultValue = "10") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "1") Integer pageSize) {
        PageInfo<Stage> flag = stageService.stateList(stage, pageNum, pageSize);
        return ResponseEntity.ok(flag);
    }

    @PostMapping("/delete")
    @ApiOperation(value = "阶段删除", notes = "阶段删除")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", dataType = "String", value = "阶段id", required = true),
            @ApiImplicitParam(paramType = "query", name = "projectId", dataType = "String", value = "项目id", required = true) })
    public ResponseEntity<String> statisticsStage(@ApiIgnore Stage stage) throws CloneNotSupportedException {
        Boolean flag = stageService.deleteByStageId(stage);
        if (flag) {
            return ResponseEntity.ok("删除成功");
        }
        return ResponseEntity.ok("删除失败");
    }

    @GetMapping("/all-stages")
    @ApiOperation(value = "查询所有阶段", notes = "查询所有阶段")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "projectId", dataType = "String", value = "项目id", required = true) })
    public ResponseEntity<List<Stage>> selectAllStage(Integer projectId) {
        List<Stage> stages = stageService.selectAllStage(projectId);
        return ResponseEntity.ok(stages);
    }

    @GetMapping("/stage-static")
    @ApiOperation(value = "阶段统计", notes = "阶段统计")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "projectId", dataType = "String", value = "项目id", required = true) })
    public ResponseEntity<WorkCountDTO> stageStatic(Integer projectId) {
        WorkCountDTO workDTO = stageService.stageStatic(projectId);
        return ResponseEntity.ok(workDTO);
    }

    @GetMapping("/stage-log")
    @ApiOperation(value = "阶段日志", notes = "阶段日志")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "stageId", dataType = "String", value = "阶段id", required = true) })
    public ResponseEntity<List<LogStage>> selectStageLogByProjectId(Integer stageId) {
        List<LogStage> logStages = logStageService.selectStageLogByProjectId(stageId);
        return ResponseEntity.ok(logStages);
    }

    @GetMapping("/stage-budget-list")
    @ApiOperation(value = "超预算和未超预算列表", notes = "超预算和未超预算列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "projectId", dataType = "String", value = "项目id", required = true),
            @ApiImplicitParam(paramType = "query", name = "stageBudget", dataType = "String", value = "阶段状态：0未超预算，1超预算", required = true),
            @ApiImplicitParam(paramType = "query", name = "pageSize", dataType = "String", value = "页码大小", required = true),
            @ApiImplicitParam(paramType = "query", name = "pageNum", dataType = "String", value = "页码", required = true) })
    public ResponseEntity<PageInfo<Stage>> selectStageBudgetByProjectId(@ApiIgnore Stage stage, Integer pageNum,
            Integer pageSize) {
        PageInfo<Stage> stages = stageService.selectStageBudgetByProjectId(stage, pageNum, pageSize);
        return ResponseEntity.ok(stages);
    }

    /*
     * @GetMapping("/state-by-name")
     * 
     * @ApiOperation(value = "根据阶段名称查询", notes = "根据阶段名称查询")
     * 
     * @ApiImplicitParams({
     * 
     * @ApiImplicitParam(paramType = "query", name = "stageName", dataType =
     * "String", value = "阶段名称", required = true)}) public
     * ResponseEntity<List<Stage>> selectByStageName(@ApiIgnore Stage stage) {
     * List<Stage> stage1 = stageService.selectByStageName(stage); return
     * ResponseEntity.ok(stage1); }
     */

    /*
     * @GetMapping("/stage-static-state")
     * 
     * @ApiOperation(value = "根据阶段状态统计", notes = "根据阶段状态统计")
     * 
     * @ApiImplicitParams({
     * 
     * @ApiImplicitParam(paramType = "query", name = "projectId", dataType =
     * "String", value = "项目id", required = true),
     * 
     * @ApiImplicitParam(paramType = "query", name = "stageState", dataType =
     * "String", value = "阶段状态", required = true),
     * 
     * @ApiImplicitParam(paramType = "query", name = "pageSize", dataType =
     * "String", value = "页码大小", required = true),
     * 
     * @ApiImplicitParam(paramType = "query", name = "pageNum", dataType = "String",
     * value = "页码", required = true)}) public ResponseEntity<PageInfo<Stage>>
     * stageStaticByStageState(@ApiIgnore Stage stage, Integer pageNum,Integer
     * pageSize) { PageInfo<Stage> stages =
     * stageService.stageStaticByStageState(stage,pageNum,pageSize); return
     * ResponseEntity.ok(stages); }
     */
}
