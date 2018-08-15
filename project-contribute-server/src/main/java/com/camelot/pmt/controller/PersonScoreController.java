package com.camelot.pmt.controller;

import com.camelot.pmt.model.PersonScore;
import com.camelot.pmt.model.PersonScoreDTO;
import com.camelot.pmt.model.PersonScoreDetailsDTO;
import com.camelot.pmt.service.PersonScoreService;
import com.camelot.pmt.utils.Constant;
import com.camelot.pmt.utils.Constant.DataStatus;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@RestController
@RequestMapping("/person-score")
@Api(description = "价值分服务")
public class PersonScoreController {
    @Autowired
    private PersonScoreService personScoreService;

    /**
     * 价值分明细（分页查询）
     *
     * @param personScore
     * @return
     */
    @GetMapping("/score-details")
    @ApiOperation(value = "价值分明细", notes = "价值分明细")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageNum", value = "当前页数", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页数量", required = false, paramType = "query", dataType = "int") })
    public ResponseEntity<?> selectBaseInfo(@ApiIgnore PersonScore personScore, Integer pageNum, Integer pageSize) {
        log.info("userId={},pageNum={},pageSize={}", personScore.getUserId(), pageNum, pageSize);
        PageInfo<PersonScoreDetailsDTO> list = personScoreService.selectScoreDetails(personScore.getUserId(),
                DataStatus.EFFECTIVE, pageNum, pageSize);
        return ResponseEntity.ok(list);
    }

    /**
     * 个人价值分总分以及排名查询
     *
     * @return
     */
    @GetMapping("/socre-rank")
    @ApiOperation(value = "个人价值分总分以及排名查询", notes = "个人价值分总分以及排名查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", required = true, paramType = "query", dataType = "int") })
    public ResponseEntity<?> sumScoreAndRank(@ApiIgnore PersonScore personScore) {
        log.info("userId={}", personScore.getUserId());
        PersonScoreDTO personScoreDTO = personScoreService.sumScoreAndRank(personScore.getUserId(),
                Constant.DataStatus.EFFECTIVE);
        return ResponseEntity.ok(personScoreDTO);
    }

    /**
     * 增加个人分表数据
     *
     * @param personScore
     * @return
     */
    @PostMapping("/add")
    @ApiOperation(value = "增加个人分表数据", notes = "增加个人分表数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "projectId", value = "项目id", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "stageId", value = "阶段id", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "workId", value = "工程包id", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "taskId", value = "任务id", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "taskValue", value = "任务价值分", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "state", value = "状态值", required = true, paramType = "query", dataType = "int") })
    public ResponseEntity<?> insert(@ApiIgnore PersonScore personScore) {
        log.info("userId={},projectId={},stageId={},workId={},taskId={},taskValue={},state={}", personScore.getUserId(),
                personScore.getProjectId(), personScore.getStageId(), personScore.getWorkId(), personScore.getTaskId(),
                personScore.getTaskValue(), personScore.getState());
        int num = personScoreService.insert(personScore);
        if (num > 0) {
            return ResponseEntity.ok(Manager.INSERT_SUCCESS);
        }
        return new ResponseEntity<Object>(Manager.REQUEST_DATA_EXCEPTION, HttpStatus.NOT_ACCEPTABLE);
    }

    /**
     * 根据id删除价值分表
     *
     * @param personScore
     * @return
     */
    @ApiIgnore
    @DeleteMapping("/by-id")
    @ApiOperation(value = "根据id删除价值分表", notes = "根据id删除价值分表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键id", required = true, paramType = "query", dataType = "int") })
    public ResponseEntity<?> deleteByPrimaryKey(@ApiIgnore PersonScore personScore) {
        log.info("id={}", personScore.getId());
        int num = personScoreService.deleteByPrimaryKey(personScore.getId());
        if (num > 0) {
            return ResponseEntity.ok(Manager.DELETE_SUCCESS);
        }
        return new ResponseEntity<Object>(Manager.REQUEST_DATA_EXCEPTION, HttpStatus.NOT_ACCEPTABLE);
    }

    /**
     * 根据id更新价值表数据
     *
     * @param personScore
     * @return
     */
    @PutMapping("/by-id")
    @ApiOperation(value = "根据id更新价值表数据", notes = "根据id更新价值表数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键id", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "userId", value = "用户id", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "projectId", value = "项目id", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "stageId", value = "阶段id", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "workId", value = "工程包id", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "taskId", value = "任务id", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "taskValue", value = "任务价值分", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "state", value = "状态值", required = false, paramType = "query", dataType = "int") })
    public ResponseEntity<?> updateByPrimaryKeySelective(@ApiIgnore PersonScore personScore) {
        log.info("id={},userId={},projectId={},stageId={},workId={},taskId={},taskValue={},state={}",
                personScore.getId(), personScore.getUserId(), personScore.getProjectId(), personScore.getStageId(),
                personScore.getWorkId(), personScore.getTaskId(), personScore.getTaskValue(), personScore.getState());
        int num = personScoreService.updateByPrimaryKeySelective(personScore);
        if (num > 0) {
            return ResponseEntity.ok(Manager.UPDATE_SUCCESS);
        }
        return new ResponseEntity<Object>(Manager.REQUEST_DATA_EXCEPTION, HttpStatus.NOT_ACCEPTABLE);
    }
}
