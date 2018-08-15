package com.camelot.pmt.controller;

import com.camelot.pmt.model.LogTaskDTO;
import com.camelot.pmt.service.LogTaskService;
import com.camelot.pmt.utils.Constant;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author qiaodj
 * @date 2018年5月21日
 */
@Slf4j
@RestController
@RequestMapping("/log-task")
@Api(description = "任务日志服务")
public class LogTaskController {
    @Autowired
    private LogTaskService logTaskService;

    /**
     * 根据任务领取人id查询任务操作记录
     *
     * @param taskPersonId
     * @param state
     * @param monthNum
     * @return
     */
    @GetMapping("/log-task-operate")
    @ApiOperation(value = "根据任务领取人id查询任务操作记录", notes = "根据任务领取人id查询任务操作记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskPersonId", value = "任务领取人id", required = true, paramType = "query"),
            @ApiImplicitParam(name = "state", value = "状态值", required = true, paramType = "query"),
            @ApiImplicitParam(name = "monthNum", value = "月份值", required = true, paramType = "query") })
    public ResponseEntity<?> selectPeriodTotalTask(Integer taskPersonId, Integer state, Integer monthNum) {
        log.info("taskPersonId={},state={},monthNum={}", taskPersonId, state, monthNum);
        List<LogTaskDTO> logTaskDTOS = logTaskService.selectByTaskPersonId(state, taskPersonId, monthNum);
        if (logTaskDTOS != null && !logTaskDTOS.isEmpty()) {
            return ResponseEntity.ok(logTaskDTOS);
        }
        return ResponseEntity.ok(Constant.Manager.REQUEST_DATA_EXCEPTION);
    }
}
