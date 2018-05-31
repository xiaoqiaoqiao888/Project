package com.camelot.pmt.controller;

import com.camelot.pmt.model.CoreMember;
import com.camelot.pmt.model.Project;
import com.camelot.pmt.model.ProjectDTO;
import com.camelot.pmt.model.ProjectUser;
import com.camelot.pmt.model.Stage;
import com.camelot.pmt.model.SysUser;
import com.camelot.pmt.service.ProjectService;
import com.camelot.pmt.service.ProjectUserService;
import com.camelot.pmt.service.StageService;
import com.camelot.pmt.utils.Constant;
import com.camelot.pmt.utils.TokenUtil;
import com.github.pagehelper.PageInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.ShiroException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import springfox.documentation.annotations.ApiIgnore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/project")
@Api(description = "项目管理", value = "项目管理")
public class ProjectController {
    private static final Logger log = LoggerFactory.getLogger(ProjectController.class);

    @Autowired
    private ProjectService projectService;
    @Autowired
    private ProjectUserService projectUserService;
    @Autowired
    private StageService stageService;

    @PostMapping("/add")
    @ApiOperation(value = "立项", notes = "立项")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "projectName", dataType = "String", value = "项目名",
                    required = true),
            @ApiImplicitParam(paramType = "query", name = "projectBudget", dataType = "BigDecimal", value = "项目预算",
                    required = true),
            @ApiImplicitParam(paramType = "query", name = "startTime", dataType = "String",
                    value = "预计开始时间(yyyy/MM/dd)", required = true),
            @ApiImplicitParam(paramType = "query", name = "endTime", dataType = "String", value = "预计结束时间(yyyy/MM/dd)",
                    required = true),
            @ApiImplicitParam(paramType = "query", name = "projectDesc", dataType = "String", value = "项目描述",
                    required = false),
            @ApiImplicitParam(paramType = "query", name = "projectCode", dataType = "String", value = "项目编码",
                    required = true) })
    public ResponseEntity<?> add(@ApiIgnore Project project) throws CloneNotSupportedException, ParseException {
        log.info("创建一个新的项目");
        int count = projectService.selectProjectNo(project.getProjectCode());
        if (count > 0) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("项目编号已经存在");
        }
        if (StringUtils.isEmpty(StringUtils.trim(project.getProjectName()))) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("项目名称不能为空");
        }
        if (project.getProjectBudget() == null) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("项目预算不能为空");
        }
        if (StringUtils.isEmpty(StringUtils.trim(project.getStartTime()))) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("预计开始时间不能为空");
        }
        if (StringUtils.isEmpty(StringUtils.trim(project.getEndTime()))) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("预计结束时间不能为空");
        }
        if (StringUtils.isEmpty(StringUtils.trim(project.getProjectCode()))) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("项目编码不能为空");
        }
        long projectEndTime = new SimpleDateFormat("yyyy/MM/dd").parse(project.getEndTime()).getTime();
        long projectStartTime = new SimpleDateFormat("yyyy/MM/dd").parse(project.getStartTime()).getTime();
        long nowTime = new Date().getTime();
        if (projectStartTime > projectEndTime) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("项目预计开始时间不能超过结束时间");
        }
        if (projectEndTime < nowTime) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("项目预计结束时间不能小于当前时间");
        }
        SysUser user = TokenUtil.getUserFromToken();
        project.setCreateBy(user.getId());
        project.setUpdateBy(user.getId());
        project.setState(Constant.DataStatus.EFFECTIVE);
        project.setProjectState(Constant.Status.NO_START);
        int id = projectService.add(project);
        if (id > 0) {
            return ResponseEntity.ok(id);
        }
        return ResponseEntity.ok("添加失败！");
    }

    @PostMapping("/update")
    @ApiOperation(value = "修改项目", notes = "修改项目")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", dataType = "Integer", value = "项目id", required = true),
            @ApiImplicitParam(paramType = "query", name = "projectName", dataType = "String", value = "项目名",
                    required = false),
            @ApiImplicitParam(paramType = "query", name = "projectBudget", dataType = "BigDecimal", value = "项目预算",
                    required = false),
            @ApiImplicitParam(paramType = "query", name = "startTime", dataType = "String",
                    value = "预计开始时间(yyyy/MM/dd)", required = false),
            @ApiImplicitParam(paramType = "query", name = "endTime", dataType = "String", value = "预计结束时间(yyyy/MM/dd)",
                    required = false),
            @ApiImplicitParam(paramType = "query", name = "projectDesc", dataType = "String", value = "项目描述",
                    required = false) })
    public ResponseEntity<?> update(@ApiIgnore Project project) throws CloneNotSupportedException, ParseException {
        SysUser user = TokenUtil.getUserFromToken();
        project.setUpdateBy(user.getId());
        if (!StringUtils.isEmpty(project.getStartTime()) && !StringUtils.isEmpty(project.getEndTime())) {
            long projectEndTime = new SimpleDateFormat("yyyy/MM/dd").parse(project.getEndTime()).getTime();
            long projectStartTime = new SimpleDateFormat("yyyy/MM/dd").parse(project.getStartTime()).getTime();
            long nowTime = new Date().getTime();
            if (projectStartTime > projectEndTime) {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("项目预计开始时间不能超过结束时间");
            }
            if (projectEndTime < nowTime) {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("项目预计结束时间不能小于当前时间");
            }
        }
        boolean flag = projectService.update(project);
        if (flag) {
            return ResponseEntity.ok("修改成功！");
        }
        return ResponseEntity.ok("修改失败！");
    }

    /**
     * 分页条件查询项目列表
     */
    @GetMapping("/list")
    @ApiOperation(value = "分页条件查询项目列表", notes = "分页条件查询项目列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", defaultValue = "1", value = "页码", required = false, paramType = "query",
                    dataType = "int"),
            @ApiImplicitParam(name = "pageSize", defaultValue = "10", value = "页大小", required = false,
                    paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "projectState", value = "项目状态(0-未开始 1-进行中  3-已完成 4-延期进行中)", required = true,
                    paramType = "query", dataType = "int") })
    public ResponseEntity<PageInfo<?>> list(@RequestParam(required = false) Integer pageNum,
            @RequestParam(required = false) Integer pageSize, @RequestParam Integer projectState,
            @RequestParam(required = false) String projectName) {
        PageInfo<?> sysResourceList = projectService.list(pageNum, pageSize, projectState, projectName);
        return ResponseEntity.ok(sysResourceList);
    }

    /**
     * 分页条件查询项目列表
     */
    @GetMapping("/list-condition")
    @ApiOperation(value = "条件查询项目列表(不分页)", notes = "条件查询项目列表(不分页)")
    @ApiImplicitParams({ @ApiImplicitParam(name = "projectName", value = "项目名称", required = false, paramType = "query",
            dataType = "String") })
    public ResponseEntity<?> list(@RequestParam(required = false) String projectName) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (!StringUtils.isEmpty(projectName)) {
            map.put("projectName", projectName);
        }
        List<Project> list = projectService.selectProjectLists(map);
        return ResponseEntity.ok(list);
    }

    /**
     * 根据项目id查询项目详情
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "根据项目id查询项目详情", notes = "根据项目id查询项目详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "项目id", required = true, paramType = "path", dataType = "int") })
    public ResponseEntity<?> get(@PathVariable Integer id) {
        Project project = projectService.selectByPrimaryKey(id);
        List<ProjectUser> list = projectUserService.searchCoreMember(id);
        project.setProjectUserList(list);
        return ResponseEntity.ok(project);
    }

    @PostMapping(value = "/add-core-user")
    @ApiOperation(value = "项目详情添加核心成员（批量添加）",
            notes = "[{projectId：0, " + "userId: 0, inTime: 2018-01-01, outTime: 2018-10-10 }]")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectId", value = "项目id", required = true, paramType = "query",
                    dataType = "int"),
            @ApiImplicitParam(name = "inTime", value = "进项目时间", required = true, paramType = "query",
                    dataType = "date"),
            @ApiImplicitParam(name = "outTime", value = "出项目时间", required = true, paramType = "query",
                    dataType = "date"),
            @ApiImplicitParam(name = "userId", value = "核心成员id", required = true, paramType = "query",
                    dataType = "int"), })
    public ResponseEntity<?> addCoreMember(@RequestBody List<CoreMember> coreMember)
            throws IllegalArgumentException, ShiroException {
        SysUser user = TokenUtil.getUserFromToken();
        Integer loginUserId = user.getId();
        log.info("add core member to project details, creater [{}]", user.getUserName());
        if (coreMember != null) {
            Iterator<CoreMember> iterator = coreMember.iterator();
            while (iterator.hasNext()) {
                CoreMember next = iterator.next();
                if (next.getUserId() == null || next.getProjectId() == null) {
                    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("用户id或项目id不能为空");
                }
                if (next.getInTime() == null || next.getOutTime() == null) {
                    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("进出项目时间不能为空");
                }
                next.setType(CoreMember.PROJECT_USER_TYPE_CORE);
                next.setState(CoreMember.PROJECT_USER_STATE_EFFECTIVE);
                next.setCreateBy(loginUserId);
                next.setCreateTime(new Date());
                next.setUpdateBy(loginUserId);
                next.setUpdateTime(new Date());
            }
        }
        projectUserService.addCoreMember(coreMember);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping(value = "/core-user")
    @ApiOperation(value = "删除核心成员", notes = "删除核心成员")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "projectId", value = "项目id", required = true, paramType = "query",
                    dataType = "int") })
    public ResponseEntity<?> deleteCoreUser(Integer projectId, Integer userId) {
        log.info("delete core member of project by userId = [{}], projectId = [{}]", userId, projectId);
        if (projectId == null || userId == null) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("项目id或成员id不能为空");
        }
        projectUserService.deleteCoreUser(projectId, userId);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    /**
     * 项目开始
     *
     * @param project
     * @return
     */
    @PostMapping("/start-project")
    @ApiOperation(value = "开始项目", notes = "开始项目")
    @ApiImplicitParams({ @ApiImplicitParam(paramType = "query", name = "id", dataType = "Integer", value = "项目id",
            required = true) })
    public ResponseEntity<?> startProject(@ApiIgnore Project project) throws CloneNotSupportedException {
        SysUser user = TokenUtil.getUserFromToken();
        project.setUpdateBy(user.getId());
        boolean flag = projectService.updateProjectTime(project);
        if (flag) {
            return ResponseEntity.ok("修改成功！");
        }
        return ResponseEntity.ok("修改失败！");
    }

    /**
     * 项目关闭
     *
     * @param project
     * @return
     */
    @PostMapping("/end-project")
    @ApiOperation(value = "关闭项目", notes = "关闭项目")
    @ApiImplicitParams({ @ApiImplicitParam(paramType = "query", name = "id", dataType = "Integer", value = "项目id",
            required = true) })
    public ResponseEntity<?> endProject(@ApiIgnore Project project) throws CloneNotSupportedException {
        SysUser user = TokenUtil.getUserFromToken();
        project.setUpdateBy(user.getId());
        Project pro = projectService.selectByPrimaryKey(project.getId());
        if (pro == null) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("项目id不存在");
        }
        boolean flag = projectService.updateProjectEnd(pro);
        if (flag) {
            return ResponseEntity.ok("关闭成功");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("项目不能关闭");
    }

    /**
     * 项目阶段统计
     */
    @GetMapping("/status-count")
    @ApiOperation(value = "项目状态统计", notes = "项目状态统计")
    public ResponseEntity<?> projectStatusCount() {
        List<ProjectDTO> selectStateCount = projectService.selectStateCount();
        return ResponseEntity.ok(selectStateCount);
    }

    /**
     * 预算统计
     */
    @GetMapping("/project-count")
    @ApiOperation(value = "项目预算统计", notes = "项目预算统计")
    @ApiImplicitParams({ @ApiImplicitParam(paramType = "query", name = "id", dataType = "Integer", value = "项目id",
            required = true) })
    public ResponseEntity<?> projectCount(Integer id) {
        Map<String, Object> map = new HashMap<String, Object>();
        List<Stage> selectStageBudget = stageService.selectStageBudget(id);
        if (selectStageBudget != null && selectStageBudget.size() > 0) {
            // 整体进度
            double sumPlan = 0;
            // 消耗成本
            double sumCost = 0;
            for (Stage stage : selectStageBudget) {
                if (!StringUtils.isEmpty(stage.getPlan())) {
                    double plan = Double.parseDouble(stage.getPlan());
                    sumPlan += plan;
                }
                if (!StringUtils.isEmpty(stage.getCost())) {
                    double cost = Double.parseDouble(stage.getCost());
                    sumCost += cost;
                }
            }
            double avgPlan = sumPlan / selectStageBudget.size();
            double avgCost = sumCost / selectStageBudget.size();
            map.put("avgPlan", avgPlan);
            map.put("avgCost", avgCost);
        } else {
            map.put("avgPlan", 0);
            map.put("avgCost", 0);
        }
        return ResponseEntity.ok(map);
    }
}
