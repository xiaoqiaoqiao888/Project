package com.camelot.pmt.controller;

import com.camelot.pmt.model.LogStage;
import com.camelot.pmt.model.Project;
import com.camelot.pmt.model.Stage;
import com.camelot.pmt.model.SysResourceDTO;
import com.camelot.pmt.model.SysUser;
import com.camelot.pmt.model.Task;
import com.camelot.pmt.model.Work;
import com.camelot.pmt.service.CheckManagerService;
import com.camelot.pmt.service.LogProjectService;
import com.camelot.pmt.service.LogStageService;
import com.camelot.pmt.service.LogTaskService;
import com.camelot.pmt.service.ProjectService;
import com.camelot.pmt.service.StageService;
import com.camelot.pmt.service.WorkService;
import com.camelot.pmt.utils.Constant;
import com.camelot.pmt.utils.TokenUtil;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * * @author muyuanpei * @date 2018/5/14
 */
@RestController
@RequestMapping("/check-manager")
@Api(value = "验收管理", description = "验收管理,status值  1-任务  2-工程包  3-阶段")
public class CheckManagerController {

    @Autowired
    private WorkService workService;

    @Autowired
    private LogTaskService logTaskService;

    @Autowired
    private StageService stageService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private CheckManagerService checkManagerService;

    @Autowired
    private LogStageService logStageService;

    @Autowired
    private LogProjectService logProjectService;

    /**
     * 分页查询未验收的任务列表
     *
     * @param sysResourceVO
     *            接受分页数据
     */
    @PostMapping("/list")
    @ApiOperation(value = "分页查询验收的任务列表", notes = "分页查询验收的任务列表")
    @ApiImplicitParams({ @ApiImplicitParam(name = "status", value = "查询列表类型", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "projectId", value = "项目id", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "stageId", value = "阶段id", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "workId", value = "工程包id", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "taskId", value = "任务id", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageNum", value = "页码", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "页大小", paramType = "query", dataType = "int"), })
    public ResponseEntity<PageInfo<?>> list(@RequestBody SysResourceDTO sysResourceVO) {
        // 1-任务 2-工程包 3-阶段
        if (sysResourceVO.getStatus() == Constant.CheckManager.DIFF_TASK_SIGN) {
            Task task = new Task();
            task.setProjectId(sysResourceVO.getProjectId());
            task.setStageId(sysResourceVO.getStageId());
            task.setWorkId(sysResourceVO.getWorkId());
            task.setId(sysResourceVO.getTaskID());
            PageInfo<Map<String, Object>> taskList = checkManagerService.selectTaskListByPageInfo(task,
                    sysResourceVO.getPageNum(), sysResourceVO.getPageSize());
            return ResponseEntity.ok(taskList);
        } else if (sysResourceVO.getStatus() == Constant.CheckManager.DIFF_WORK_SIGN) {
            PageInfo<Work> workListByPage = workService.getWorkListByPage(sysResourceVO.getPageNum(),
                    sysResourceVO.getPageSize(), sysResourceVO.getProjectId(), sysResourceVO.getStageId(), null, null);
            return ResponseEntity.ok(workListByPage);
        } else {
            PageInfo<Stage> stateList = stageService.selectStateListByProjectId(sysResourceVO.getProjectId(),
                    sysResourceVO.getPageNum(), sysResourceVO.getPageSize());
            return ResponseEntity.ok(stateList);
        }
    }

    /**
     * 查询项目列表,可以根据项目名称模糊查询
     */
    @PostMapping("/project-list")
    @ApiOperation(value = "查询项目列表", notes = "查询项目列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectName", value = "项目名称", paramType = "query", dataType = "int") })
    public ResponseEntity<Object> projectList(@RequestBody Project project) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("projectName", project.getProjectName());
        List<Project> projectsList = projectService.selectProjectLists(map);
        return ResponseEntity.ok(projectsList);
    }

    /**
     * 确认验收
     *
     * @param sysResourceVO
     *            接受数据
     */
    @PutMapping("/check-true")
    @ApiOperation(value = "确认验收", notes = "确认验收")
    @ApiImplicitParams({ @ApiImplicitParam(name = "status", value = "确认验收类型", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "projectId", value = "确认验收所属的项目id", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "trueId", value = "确认验收的id", paramType = "query", dataType = "int") })
    public ResponseEntity<String> checkTrue(@RequestBody SysResourceDTO sysResourceVO)
            throws CloneNotSupportedException {
        SysUser user = TokenUtil.getUserFromToken();
        // 1-任务 2-工程包 3-阶段
        if (sysResourceVO.getStatus() == Constant.CheckManager.DIFF_TASK_SIGN) {
            // 验收单个任务
            Task task = new Task();
            task.setUpdateBy(user.getId());
            task.setId(sysResourceVO.getTrueId());
            task.setTaskState(Integer.valueOf(Constant.Status.COMPLETED));
            int result = checkManagerService.updateByPrimaryKeySelective(task);
            int logResult = logTaskService.insertLogTask(Constant.CheckManager.CHECK_TASK, task);
            if (result > 0 && logResult > 0) {
                return ResponseEntity.ok(Constant.CheckManager.CHECK_SUCCESS);
            } else {
                return ResponseEntity.ok(Constant.CheckManager.CHECK_FAILE);
            }
        } else if (sysResourceVO.getStatus() == Constant.CheckManager.DIFF_WORK_SIGN) {
            // 验收单个工程包
            Work work = new Work();
            work.setUpdateBy(user.getId());
            work.setId(sysResourceVO.getTrueId());
            work.setWorkState(Integer.valueOf(Constant.Status.COMPLETED));
            // 查询当前工程包下面所有未验收的任务
            List<Task> taskListByWorkId = checkManagerService.getTaskListByWorkId(work);
            if (taskListByWorkId.size() > 0) {
                // 将该包下的所有未验收任务的id添加到一个集合里
                List<Integer> taskIntList = new ArrayList<>();
                for (Task task : taskListByWorkId) {
                    taskIntList.add(task.getId());
                }
                // 将所有任务的id拼接成字符串
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < taskIntList.size(); i++) {
                    stringBuilder.append(taskIntList.get(i) + ",");
                }
                // 调用任务的批量验收，完成该工程包下面的任务的验收
                SysResourceDTO sysResourceDTO = new SysResourceDTO();
                sysResourceDTO.setStatus(1);
                sysResourceDTO.setTrueIdStr(stringBuilder.toString());
                checkManagerService.updateStateList(sysResourceDTO);
            }
            int workResult = workService.updateWork(work);
            if (workResult > 0) {
                return ResponseEntity.ok(Constant.CheckManager.CHECK_SUCCESS);
            } else {
                return ResponseEntity.ok(Constant.CheckManager.CHECK_FAILE);
            }
        } else {
            // 验收单个阶段
            Stage stage = new Stage();
            stage.setUpdateBy(user.getId());
            stage.setId(sysResourceVO.getTrueId());
            stage.setStageState(Integer.valueOf(Constant.Status.COMPLETED));
            // 查询当前阶段下面所有未验收的工程包
            List<Work> workListByStageId = checkManagerService.getWorkListByStageId(stage);
            if (workListByStageId.size() > 0) {
                List<Integer> workIntList = new ArrayList<>();
                for (Work work : workListByStageId) {
                    if (work.getWorkState() == 2) {
                        workIntList.add(work.getId());
                        List<Task> checkTaskListByWorkId = checkManagerService.getTaskListByWorkId(work);
                        if (checkTaskListByWorkId.size() > 0) {
                            // 将该包下的所有未验收任务的id添加到一个集合里
                            List<Integer> taskIntList = new ArrayList<>();
                            for (Task task : checkTaskListByWorkId) {
                                taskIntList.add(task.getId());
                            }
                            // 将所有任务的id拼接成字符串
                            StringBuilder stringBuilder = new StringBuilder();
                            for (int i = 0; i < taskIntList.size(); i++) {
                                stringBuilder.append(taskIntList.get(i) + ",");
                            }
                            // 调用任务的批量验收，完成该工程包下面的任务的验收,添加日志
                            SysResourceDTO sysResourceDTO = new SysResourceDTO();
                            HashMap<String, Object> map = new HashMap<>();
                            sysResourceDTO.setStatus(1);
                            sysResourceDTO.setTrueIdStr(stringBuilder.toString());
                            map.put("asList", taskIntList);
                            map.put("updateBy", user.getId());
                            map.put("createBy", user.getId());
                            map.put("taskState", Constant.Status.COMPLETED);
                            checkManagerService.updateStateList(sysResourceDTO);
                            checkManagerService.addTaskLog(map);
                        }
                    }
                    if (workIntList.size() > 0) {
                        // 将所有工程包的id拼接成字符串
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int i = 0; i < workIntList.size(); i++) {
                            stringBuilder.append(workIntList.get(i) + ",");
                        }
                        // 调用工程包的批量验收，完成该工程包下面的任务的验收
                        SysResourceDTO sysResourceDTO = new SysResourceDTO();
                        HashMap<String, Object> map = new HashMap<>();
                        sysResourceDTO.setStatus(2);
                        sysResourceDTO.setTrueIdStr(stringBuilder.toString());
                        map.put("asList", workIntList);
                        map.put("updateBy", user.getId());
                        map.put("createBy", user.getId());
                        map.put("workState", Constant.Status.COMPLETED);
                        checkManagerService.updateStateList(sysResourceDTO);
                        checkManagerService.addWorkLog(map);
                    }
                }
            }
            LogStage logStage = new LogStage();
            logStage.setOperateDesc(Constant.CheckManager.CHECK_STAGE);
            logStage.setStageId(stage.getId());
            Boolean result = stageService.updateStage(stage);
            Boolean checkProject = checkManagerService.checkProject(sysResourceVO);
            if (checkProject == true) {
                int projectFinish = checkManagerService.updateProjectFinsh(sysResourceVO.getProjectId());
                if (projectFinish > 0) {
                    Project project = new Project();
                    project.setId(sysResourceVO.getProjectId());
                    project.setOperateDesc("项目完成");
                    logProjectService.logOfAdd(project);
                }
            }
            logStageService.logOfAdd(logStage);
            if (result == true) {
                return ResponseEntity.ok(Constant.CheckManager.CHECK_SUCCESS);
            } else {
                return ResponseEntity.ok(Constant.CheckManager.CHECK_FAILE);
            }
        }
    }

    /**
     * 确认批量验收
     *
     * @param sysResourceVO
     *            接受数据
     */
    @PutMapping("/check-true-list")
    @ApiOperation(value = "批量确认验收", notes = "批量确认验收")
    @ApiImplicitParams({ @ApiImplicitParam(name = "status", value = "确认验收类型", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "trueIdStr", value = "确认验收的id", paramType = "query", dataType = "String") })
    public ResponseEntity<String> checkTrueList(@RequestBody SysResourceDTO sysResourceVO)
            throws CloneNotSupportedException {
        // 1-任务 2-工程包 3-阶段
        int result = checkManagerService.updateStateList(sysResourceVO);
        if (result > 0) {
            return ResponseEntity.ok(Constant.CheckManager.CHECK_SUCCESS);
        } else {
            return ResponseEntity.ok(Constant.CheckManager.CHECK_FAILE);
        }
    }

}