package com.camelot.pmt.controller;

import com.camelot.pmt.model.SysUser;
import com.camelot.pmt.model.Task;
import com.camelot.pmt.model.TaskCountDTO;
import com.camelot.pmt.model.TaskHourCost;
import com.camelot.pmt.service.TaskService;
import com.camelot.pmt.utils.TokenUtil;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author za
 * @ClassName: TaskController
 * @Description: TODO 任务管理
 * @date 2018年5月14日
 */
@RestController
@RequestMapping("/task")
@Api(value = "任务管理接口", description = "任务管理接口")
public class TaskController {

    @Autowired
    private TaskService taskService;

    /**
     * 查询(任务详情,可领取任务,我的任务)通用接口
     *
     * @param taskPersonId
     * @return ResponseEntity
     */
    @PostMapping("/list")
    @ApiOperation(value = "查询(可领取任务,我的任务)通用接口", notes = "查询(任务详情,可领取任务,我的任务)通用接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", defaultValue = "1", value = "页码", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "rows", defaultValue = "10", value = "页数量", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "taskName", value = "任务名称", required = false, paramType = "form", dataType = "string"),
            @ApiImplicitParam(name = "projectName", value = "項目名称", required = false, paramType = "form", dataType = "string"),
            @ApiImplicitParam(name = "taskPersonId", value = "负责人ID", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "orderByState", value = "排序状态码", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "taskState", value = "任务状态", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "id", value = "任务id", required = false, paramType = "query", dataType = "int") })
    public ResponseEntity<PageInfo<?>> list(@RequestParam(required = true, defaultValue = "1") Integer page,
            @RequestParam(required = true, defaultValue = "10") Integer rows,
            @RequestParam(required = false) String taskName, @RequestParam(required = false) String projectName,
            @RequestParam(required = false) Integer taskPersonId, @RequestParam(required = false) Integer orderByState,
            @RequestParam(required = false) Integer taskState) {
        PageInfo<?> taskList = taskService.list(page, rows, taskName, projectName, taskPersonId, orderByState,
                taskState);
        return ResponseEntity.ok(taskList);
    }

    /**
     * 根据任务id查询任务详情
     *
     * @param id
     * @return
     */
    @GetMapping("/taskDetail")
    @ApiOperation(value = "根据任务id查询任务详情", notes = "根据任务id查询任务详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "任务id", required = true, paramType = "query", dataType = "int") })
    public ResponseEntity<Map<String, Object>> taskDetail(@RequestParam(required = true) Integer id) {
        Map<String, Object> taskDetail = taskService.taskDetail(id);
        return ResponseEntity.ok(taskDetail);
    }

    @GetMapping("/logTaskByTaskId")
    @ApiOperation(value = "根据任务id查询日志", notes = "根据任务id查询日志")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", value = "任务id", required = true, paramType = "query", dataType = "int") })
    public ResponseEntity<List<Map<String, Object>>> logTaskById(@RequestParam(required = true) Integer taskId) {
        List<Map<String, Object>> taskDetail = taskService.logTaskByTaskId(taskId);
        return ResponseEntity.ok(taskDetail);
    }

    /**
     * @Title: updateTask @Description: TODO @param @param
     *         task @param @return @return ResponseEntity<?> @throws
     */
    @PutMapping("")
    @ApiOperation(value = "(修改任务,提交任务,删除,领取任务)通用接口", notes = "(修改任务,提交任务,删除,领取任务)通用接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "任务id", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "projectId", value = "项目Id", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "stageId", value = "阶段id", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "workId", value = "工程包id", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "taskName", value = "任务名称", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "taskCode", value = "任务编码", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "taskValue", value = "任务价值点", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "relTaskTime", value = "任务实际消耗工时", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "expStartTime", value = "任务预计开始时间", required = false, paramType = "query", dataType = "date"),
            @ApiImplicitParam(name = "expEndTime", value = "任务预计结束时间", required = false, paramType = "query", dataType = "date"),
            @ApiImplicitParam(name = "priority", value = "优先级", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "complexity", value = "复杂度", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "taskState", value = "任务状态", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "taskPersonId", value = "负责人ID", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "taskSchedule", value = "任务进度", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "taskDesc", value = "任务描述", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "expTaskTime", value = "任务估时", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "relStartTime", value = "任务实际开始时间", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "state", value = "状态值", required = false, paramType = "query", dataType = "int") })
    public ResponseEntity<?> updateTask(@ApiIgnore Task task) throws CloneNotSupportedException {
        // 更新时间
        task.setUpdateTime(new Date());
        // 获取当前登录人
        SysUser sysUser = TokenUtil.getUserFromToken();
        task.setUpdateBy(sysUser.getId());
        if (null != task.getTaskState()) {
            // 判断是否为领取任务
            if (task.getTaskState() == 1) {
                // 查询是否有为完成的任务
                int count = taskService.isCompleteTask(sysUser.getId());
                // 如果没有则更新
                if (count == 0) {
                    task.setUpdateBy(sysUser.getId());
                    int updateNum = taskService.updateTask(task);
                    if (updateNum > 0) {
                        return ResponseEntity.ok("更新成功");
                    } else {
                        return ResponseEntity.ok("更新失败");
                    }
                }
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("你还有一个任务未完成,不能领取任务");
            }
        }
        task.setUpdateBy(sysUser.getId());
        int updateNum = taskService.updateTask(task);
        if (updateNum > 0) {
            return ResponseEntity.ok("更新成功");
        } else {
            return ResponseEntity.ok("更新失败");
        }

    }

    /**
     * 拆分包新增任务
     *
     * @param task
     */
    @PostMapping(value = "")
    @ApiOperation(value = "添加任务", notes = "添加任务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectId", value = "项目id", required = true, paramType = "form", dataType = "String"),
            @ApiImplicitParam(name = "stageId", value = "阶段id", required = true, paramType = "form", dataType = "String"),
            @ApiImplicitParam(name = "workId", value = "工程包id", required = true, paramType = "form", dataType = "String"),
            @ApiImplicitParam(name = "taskName", value = "任务名称", required = true, paramType = "form", dataType = "String"),
            @ApiImplicitParam(name = "taskValue", value = "价值分", required = true, paramType = "form", dataType = "String"),
            @ApiImplicitParam(name = "expTaskTime", value = "任务预估时间", required = true, paramType = "form", dataType = "String"),
            @ApiImplicitParam(name = "expStartTime", value = "任务预计开始时间", required = true, paramType = "form", dataType = "date"),
            @ApiImplicitParam(name = "expEndTime", value = "任务预计结束时间", required = true, paramType = "form", dataType = "date"),
            @ApiImplicitParam(name = "priority", value = "优先级", required = true, paramType = "form", dataType = "String"),
            @ApiImplicitParam(name = "complexity", value = "复杂度", required = true, paramType = "form", dataType = "String"),
            @ApiImplicitParam(name = "taskDesc", value = "任务描述", required = true, paramType = "form", dataType = "String") })
    public ResponseEntity<String> save(@ApiIgnore @Valid Task task) {
        SysUser sysUser = TokenUtil.getUserFromToken();
        task.setCreateBy(sysUser.getId());
        task.setUpdateBy(sysUser.getId());
        boolean flag = taskService.add(task);
        if (flag) {
            return ResponseEntity.ok("添加成功！");
        }

        return ResponseEntity.ok("添加失败！");
    }

    /**
     * 分页查询任务清单
     *
     * @param pageNum
     * @param pageSize
     * @param task
     * @return
     */
    @GetMapping("/select-TaskList-By-PageInfo")
    @ApiOperation(value = "分页查询任务清单列表", notes = "分页查询任务清单列表")
    @ApiImplicitParams({ @ApiImplicitParam(name = "pageNum", value = "页码", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "页大小", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "projectId", value = "项目ID", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "stageId", value = "阶段ID", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "workId", value = "工程包ID", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "taskName", value = "任务名称", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "taskState", value = "任务状态", paramType = "query", dataType = "int") })
    public ResponseEntity<PageInfo<?>> selectTaskListByPageInfo(@ApiIgnore Task task,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        PageInfo<?> taskList = taskService.selectTaskListByPageInfo(task, pageNum, pageSize);
        return ResponseEntity.ok(taskList);
    }

    /**
     * 查询任务不同状态的个数
     *
     * @param task
     * @return
     */

    @GetMapping("/stateCount-budgetCount")
    @ApiOperation(value = "查询任务不同状态的个数和是否超预算的个数", notes = "查询任务不同状态的个数和是否超预算的个数")
    @ApiImplicitParams({ @ApiImplicitParam(name = "projectId", value = "项目ID", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "stageId", value = "阶段ID", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "workId", value = "工程包ID", paramType = "query", dataType = "int") })
    public ResponseEntity<?> stateCountList(@ApiIgnore Task task) {
        TaskCountDTO taskCountDTO = taskService.selectTasklistByTaskState(task);
        return ResponseEntity.ok(taskCountDTO);
    }

    /**
     * 查询任务清单是否超预算状态返回任务清单详情
     *
     * @param task
     * @return
     */

    @GetMapping("/budget-List")
    @ApiOperation(value = "查询任务清单是否超预算状态返回任务清单详情", notes = "查询任务清单是否超预算状态返回任务清单详情")
    @ApiImplicitParams({ @ApiImplicitParam(name = "projectId", value = "项目ID", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "stageId", value = "阶段ID", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "workId", value = "工程包ID", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "budgetState", value = "是否超预算状态值", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageNum", value = "页码", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "页大小", paramType = "query", dataType = "int") })
    public ResponseEntity<?> budgetList(@ApiIgnore Task task, Integer budgetState,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        PageInfo<?> taskList = taskService.selectTaskByBudgetState(task, budgetState, pageNum, pageSize);
        return ResponseEntity.ok(taskList);
    }

    /**
     * 个人任务统计
     *
     * @return map
     */
    @PostMapping("/count-task")
    @ApiOperation(value = "个人任务统计", notes = "个人任务统计")
    public ResponseEntity<?> countTask() {
        SysUser sysUser = TokenUtil.getUserFromToken();
        Map<String, Integer> map = taskService.countTask(sysUser.getId());
        return ResponseEntity.ok(map);
    }

    /**
     * 分页查询任务清单
     *
     * @param taskHourCost
     * @return
     */
    @GetMapping("/select-TaskHourCostList")
    @ApiOperation(value = "查询项目成本清单", notes = "查询项目成本清单")
    @ApiImplicitParams({ @ApiImplicitParam(name = "projectId", value = "项目ID", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "stageId", value = "阶段ID", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "workId", value = "工程包ID", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "taskId", value = "任务ID", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "beginTime", value = "开始时间", paramType = "query", dataType = "date"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", paramType = "query", dataType = "date") })
    public ResponseEntity<BigDecimal> selectTaskHourCost(@ApiIgnore TaskHourCost taskHourCost) {
        BigDecimal sumCost = taskService.selectTaskHourCostList(taskHourCost);
        return ResponseEntity.ok(sumCost);
    }

    @GetMapping("/check_task_value")
    @ApiOperation(value = "检测工程包下的任务是否超过工程包价值分", notes = "检测工程包下的任务是否超过工程包价值分")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "workId", value = "工程包ID", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "taskId", value = "任务ID", paramType = "query", required = false, dataType = "int"),
            @ApiImplicitParam(name = "taskValue", value = "任务价值分", paramType = "query", required = true, dataType = "int") })
    public ResponseEntity<String> checkTaskValue(@RequestParam(required = true) Integer workId,
            @RequestParam(required = false) Integer taskId, @RequestParam(required = true) Integer taskValue) {
        ResponseEntity<String> message = taskService.checkTaskValue(workId, taskId, taskValue);
        return message;
    }

}
