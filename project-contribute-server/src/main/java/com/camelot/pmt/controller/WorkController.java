package com.camelot.pmt.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.camelot.pmt.model.LogWork;
import com.camelot.pmt.model.Work;
import com.camelot.pmt.model.WorkCountDTO;
import com.camelot.pmt.model.WorkDTO;
import com.camelot.pmt.service.WorkService;
import com.github.pagehelper.PageInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/**
 * Created by 18811 on 2018/5/14.
 */
@RestController
@RequestMapping(value = "/Work")
@Api(value = "工程包管理接口", description = "工程包管理接口")
public class WorkController {

    @Autowired
    private WorkService workService;

    @PostMapping("/add-work")
    @ApiOperation(value = "拆分工程包", notes = "拆分工程包")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectId", value = "项目ID", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "stageId", value = "阶段ID", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "workName", value = "工程包名称", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "workBudget", value = "工程包预算", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "workValue", value = "价值分", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "startTime", value = " 工程包预计开始时间", required = false, paramType = "query", dataType = "date"),
            @ApiImplicitParam(name = "endTime", value = "工程包预计结束时间", required = false, paramType = "query", dataType = "date"),
            @ApiImplicitParam(name = "workState", value = "工程包业务状态自定", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "workType", value = "工程包类型", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "state", value = "状态值 0-无效 1-有效", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "workDesc", value = "工程包描述", required = false, paramType = "query", dataType = "String"), })
    public ResponseEntity<String> addWork(@ApiIgnore Work work) {
        // 校验日期
        if (work.getStartTime().compareTo(work.getEndTime()) > 0) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("工程包预计开始时间不能超过工程包预计结束时间");
        }
        int flag = 0;
        try {
            flag = workService.addWork(work);
            // 校验预算
            if (flag > 0) {
                return ResponseEntity.ok("工程包添加成功");
            }
        } catch (Exception e) {
            if (e.getMessage().equals("超预算")) {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("超预算");
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("工程包添加失败");
    }

    @PostMapping("/update-work")
    @ApiOperation(value = "修改功工程包", notes = "修改功工程包")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "工程包ID", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "projectId", value = "项目ID", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "stageId", value = "阶段ID", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "workName", value = "工程包名称", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "workBudget", value = "工程包预算", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "workValue", value = "价值分", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "startTime", value = " 工程包预计开始时间", required = false, paramType = "query", dataType = "date"),
            @ApiImplicitParam(name = "endTime", value = "工程包预计结束时间", required = false, paramType = "query", dataType = "date"),
            @ApiImplicitParam(name = "workState", value = "工程包业务状态自定", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "workType", value = "工程包类型", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "state", value = "状态值 0-无效 1-有效", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "workDesc", value = "工程包描述", required = false, paramType = "query", dataType = "String"), })
    public ResponseEntity<String> updateWork(@ApiIgnore Work work) throws CloneNotSupportedException {
        int flag = workService.updateWork(work);
        if (flag > 0) {
            return ResponseEntity.ok("工程包修改成功");
        }
        return ResponseEntity.ok("工程包修改失败");
    }

    @DeleteMapping("/delete-work")
    @ApiOperation(value = "刪除工程包", notes = "刪除工程包")
    public ResponseEntity<String> deleteWork(String ids) {
        String[] idS1 = ids.split(",");
        int[] id = new int[idS1.length];
        for (int i = 0; i < idS1.length; i++) {
            id[i] = Integer.parseInt(idS1[i]);
        }
        int flag = workService.deleteWork(id);
        if (flag > 0) {
            return ResponseEntity.ok("工程包刪除成功");
        }
        return ResponseEntity.ok("工程包刪除失败");
    }

    @GetMapping("/work-budget-statistics")
    @ApiOperation(value = "工程包预算统计", notes = "工程包预算统计")
    public ResponseEntity<String> workBudgetStatistics(Integer stageId) {
        String count = workService.workBudgetStatistics(stageId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/get-log-work")
    @ApiOperation(value = "工程包操作日志查询", notes = "工程包操作日志查询")
    public ResponseEntity<List<LogWork>> getLogWorkBy(Integer workId) {
        List<LogWork> list = workService.getLogWorkBy(workId);
        return ResponseEntity.ok(list);
    }

    /**
     * 分页查询功能包清单（含是否超预算）
     * 
     * @param pageNum
     * @param pageSize
     * @param workDTO
     * @author: xueyj
     * @return
     */
    @GetMapping("/work-cost-page-list")
    @ApiOperation(value = "查询功能包列表", notes = "查询功能包列表")
    @ApiImplicitParams({ @ApiImplicitParam(name = "pageNum", value = "页码", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页数量", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "projectId", value = "项目id", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "stageId", value = "阶段id", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "id", value = "功能包id", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "workName", value = "功能包名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "workState", value = "项目状态", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "workType", value = "项目类型", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "workCostStatus", value = "是否超预算（1:未超预算;0:超预算）", paramType = "query", dataType = "int") })
    public ResponseEntity<PageInfo<WorkDTO>> getWorkCostListByPage(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, @ApiIgnore WorkDTO workDTO) {
        PageInfo<WorkDTO> workCostListByPage = workService.getWorkCostListByPage(pageNum, pageSize, workDTO);
        return ResponseEntity.ok(workCostListByPage);
    }

    /**
     * 查询功能包清单状态（饼图统计）
     * 
     * @param workDTO
     * @author: xueyj
     * @return
     */
    @GetMapping("/work-status-count")
    @ApiOperation(value = "查询功能包列表", notes = "查询功能包列表")
    @ApiImplicitParams({ @ApiImplicitParam(name = "projectId", value = "项目id", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "stageId", value = "阶段id", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "workState", value = "项目状态(0:未开始;1:进行中;2:待验收;3:已完成;4:延期进行中)", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "costStatus", value = "是否超预算（1:未超预算;0:超预算）", paramType = "query", dataType = "int") })
    public ResponseEntity<WorkCountDTO> getWorkCostList(@ApiIgnore WorkDTO workDTO) {
        WorkCountDTO workStatusCount = workService.getWorkStatusCount(workDTO);
        return ResponseEntity.ok(workStatusCount);
    }

    /**
     * 查询功能清单列表（验收查询条件）
     * 
     * @param projectId
     * @param stageId
     * @return
     */
    @GetMapping("/work-list")
    @ApiOperation(value = "查询功能包列表", notes = "查询功能包列表")
    @ApiImplicitParams({ @ApiImplicitParam(name = "projectId", value = "项目id", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "stageId", value = "阶段id", paramType = "query", dataType = "int") })
    public ResponseEntity<List<Work>> getWorkList(Integer projectId, Integer stageId) {
        List<Work> workList = workService.getWorkList(projectId, stageId, null);
        return ResponseEntity.ok(workList);
    }

    /**
     * 根据id查询工程包信息
     * 
     * @param workId
     * @return
     */
    @GetMapping("/work-info")
    @ApiOperation(value = "根据id查询工程包信息", notes = "根据id查询工程包信息")
    @ApiImplicitParams({ @ApiImplicitParam(name = "workId", value = "工程包id", paramType = "query", dataType = "int") })
    public ResponseEntity<Work> getWorkList(Integer workId) {
        Work workInfo = workService.getWorkInfoById(workId);
        return ResponseEntity.ok(workInfo);
    }

    /**
     * 根据work_id查询workDTO信息
     * 
     * @param workDTO
     * @return
     */
    @GetMapping("/work-cost-info")
    @ApiOperation(value = "根据id查询工程包信息", notes = "根据id查询工程包信息")
    @ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "工程包id", paramType = "query", dataType = "int") })
    public ResponseEntity<WorkDTO> getWorkCostById(@ApiIgnore WorkDTO workDTO) {
        WorkDTO workDTOInfo = workService.getWorkCostListById(workDTO);
        return ResponseEntity.ok(workDTOInfo);
    }
}
