package com.camelot.pmt.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.camelot.pmt.mapper.StageMapper;
import com.camelot.pmt.model.LogStage;
import com.camelot.pmt.model.Project;
import com.camelot.pmt.model.Stage;
import com.camelot.pmt.model.SysUser;
import com.camelot.pmt.model.TaskHourCost;
import com.camelot.pmt.model.WorkCountDTO;
import com.camelot.pmt.model.WorkDTO;
import com.camelot.pmt.service.LogStageService;
import com.camelot.pmt.service.ProjectService;
import com.camelot.pmt.service.StageService;
import com.camelot.pmt.service.SysUserService;
import com.camelot.pmt.service.TaskService;
import com.camelot.pmt.service.WorkService;
import com.camelot.pmt.utils.Constant;
import com.camelot.pmt.utils.TokenUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * StageServiceImpl class
 *
 * @author zhangzhan
 * @date 2018/5/14
 */

@Service

public class StageServiceImpl implements StageService {
    private static final String LOG_STAGE_NEW = "划分阶段";
    private static final String LOG_STAGE_UPDATE = "修改阶段";
    private static final String LOG_STAGE_DELETE = "删除阶段";
    private static final String LOG_STAGE_STATE_UPDATE = "修改阶段状态";
    @Autowired
    private StageMapper stageMapper;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private LogStageService logStageService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private WorkService workService;
    @Autowired
    private TaskService taskService;

    @Override
    public List<Stage> selectByStageName(Stage stage) {
        List<Stage> list = stageMapper.selectByStageName(stage);
        // 根据阶段名称查询阶段
        return list;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public Boolean insertStage(Stage stage) throws CloneNotSupportedException {
        Project project = projectService.selectByPrimaryKey(stage.getProjectId());
        Integer projectState = project.getProjectState();
        // 根据项目id获取项目状态
        SysUser sysUser = TokenUtil.getUserFromToken();
        // 获取用户信息
        stage.setCreateBy(sysUser.getId());
        stage.setCreateTime(new Date());
        stage.setState(Constant.DataStatus.EFFECTIVE);
        stage.setStageState(projectState);
        // 阶段状态0-未开始，1-进行中，4-延期进行中，2-待验收,3-已完成
        stage.setUpdateBy(sysUser.getId());
        stage.setUpdateTime(new Date());
        // 插入相应信息到阶段
        Boolean flag = true;
        int insert = stageMapper.insert(stage);
        // 插入数据库
        if (insert <= 0) {
            flag = false;
        }
        LogStage logStage = new LogStage();
        logStage.setOperateDesc(LOG_STAGE_NEW);
        logStage.setStageId(stage.getId());
        logStage.setStageName(stage.getStageName());
        logStage.setProjectId(stage.getProjectId());
        logStage.setStageBudget(stage.getStageBudget());
        logStage.setState(stage.getState());
        logStageService.logOfAdd(logStage);
        // 判断是否成功
        return flag;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public Boolean updateStage(Stage stage) throws CloneNotSupportedException {
        SysUser sysUser = TokenUtil.getUserFromToken();
        // 获取用户信息
        stage.setUpdateBy(sysUser.getId());
        stage.setUpdateTime(new Date());
        if (stage.getStageState() != null && stage.getStageState() == Constant.Status.WAIT_CHECK) {
            stage.setEndSubmitTime(new Date());
        }
        // 插入相关信息到阶段
        Boolean flag = true;
        int insert = stageMapper.updateByPrimaryKeySelective(stage);
        // 修改数据库
        if (insert <= 0) {
            flag = false;
        }
        LogStage logStage = new LogStage();
        logStage.setOperateDesc(LOG_STAGE_UPDATE);
        logStage.setStageId(stage.getId());
        logStage.setStageName(stage.getStageName());
        logStage.setProjectId(stage.getProjectId());
        logStage.setStageBudget(stage.getStageBudget());
        logStage.setState(stage.getState());
        logStageService.logOfAdd(logStage);
        return flag;
    }

    @Override
    public Stage selectById(Stage stage) {
        Integer id = stage.getId();
        // 获取阶段id
        Stage stage1 = stageMapper.selectByPrimaryKey(id);
        // 通过阶段id查询
        return stage1;
    }

    @Override
    public Double statisticsStage(Integer projectId) {
        Double flag = stageMapper.selectSumStageByProjectId(projectId);
        // 通过项目id进行查询
        return flag;
    }

    @Override
    public PageInfo<Stage> stateList(Stage stage1, Integer pageNum, Integer pageSize) {
        // 初始化分页信息
        PageHelper.startPage(pageNum, pageSize);
        // 查询任务list
        List<Stage> stage = stageMapper.selectStateByProjectId(stage1);
        commonMotner(stage1.getProjectId(), stage);
        // pageHelper的收尾
        PageInfo<Stage> pageResult = new PageInfo<>(stage);
        pageResult.setList(stage);
        return pageResult;
    }

    @Override
    public PageInfo<Stage> selectStateListByProjectId(Integer projectId, Integer pageNum, Integer pageSize) {
        // 初始化分页信息
        PageHelper.startPage(pageNum, pageSize);
        // 查询任务list
        List<Stage> stage = stageMapper.selectByProjectId(projectId);
        List<Stage> stages = new ArrayList<>();
        for (Stage stage1 : stage) {
            Integer updateBy = stage1.getUpdateBy();
            SysUser sysUsers = sysUserService.selectByPrimaryKey(updateBy);
            String userName = sysUsers.getUserName();
            stage1.setUserName(userName);
            String realName = sysUsers.getRealName();
            stage1.setCherealname(realName);
            stages.add(stage1);
        }
        stage.clear();
        stage.addAll(stages);
        // pageHelper的收尾
        PageInfo<Stage> pageResult = new PageInfo<>(stage);
        pageResult.setList(stage);
        return pageResult;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public boolean updateStageState(Date date) {
        List<Stage> stage = stageMapper.selectStageUnderWay();
        Stage stage2 = new Stage();
        for (Stage stage1 : stage) {
            Date endTime = stage1.getEndTime();
            long stageEndTime = endTime.getTime();
            long nowTime = date.getTime();
            if (nowTime > stageEndTime) {
                stage2.setId(stage1.getId());
                stage2.setStageState(Constant.Status.DELAY_HAVE_IN_HAND);
                // 阶段状态0-未开始，1-进行中，4-延期进行中，2-待验收,3-已完成
                int count = stageMapper.updateByPrimaryKeySelective(stage2);
                if (count <= 0) {
                    return false;
                }
            }

        }
        return true;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public Boolean deleteByStageId(Stage stage) throws CloneNotSupportedException {
        Boolean flag = true;
        stage.setState(Constant.DataStatus.INVALID);
        int delete = stageMapper.updateByPrimaryKeySelective(stage);
        if (delete <= 0) {
            flag = false;
        }
        LogStage logStage = new LogStage();
        logStage.setOperateDesc(LOG_STAGE_DELETE);
        logStage.setStageId(stage.getId());
        logStage.setStageName(stage.getStageName());
        logStage.setProjectId(stage.getProjectId());
        logStage.setStageBudget(stage.getStageBudget());
        logStage.setState(stage.getState());
        logStageService.logOfAdd(logStage);
        return flag;
    }

    @Override
    public List<Stage> selectAllStage(Integer projectId) {
        List<Stage> stages = stageMapper.selectAllStage(projectId);
        return stages;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public boolean closeStageByProjectID(Stage stage) throws CloneNotSupportedException {
        Boolean flag = true;
        int close = stageMapper.updateStageByProjectID(stage);
        if (close <= 0) {
            flag = false;
        }
        LogStage logStage = new LogStage();
        logStage.setOperateDesc(LOG_STAGE_STATE_UPDATE);
        logStage.setProjectId(stage.getProjectId());
        logStage.setState(stage.getState());
        logStageService.logOfAdd(logStage);
        return flag;
    }

    @Override
    public WorkCountDTO stageStatic(Integer projectId) {
        WorkCountDTO workCountDTO1 = stageMapper.selectStageStatic(projectId);
        return workCountDTO1;
    }

    @Override
    public PageInfo<Stage> stageStaticByStageState(Stage stage, Integer pageNum, Integer pageSize) {
        // 初始化分页信息
        PageHelper.startPage(pageNum, pageSize);
        // 查询任务list
        List<Stage> stages = stageMapper.stageStaticByStageState(stage);
        // pageHelper的收尾
        for (Stage stage1 : stages) {
            Double costSum = null;
            Double plan = null;
            WorkDTO workDTO = new WorkDTO();
            Integer id = stage1.getId();
            workDTO.setProjectId(stage1.getProjectId());
            workDTO.setStageId(id);
            List<WorkDTO> workList = workService.getWorkCostList(workDTO);
            workDTO.setState(Constant.Status.COMPLETED);
            List<WorkDTO> workList1 = workService.getWorkCostList(workDTO);
            Integer size = workList.size();
            Integer size1 = workList1.size();
            if (size1 != null && size != null && size != 0) {
                plan = size1 / size * 1.0;
                String plan1 = plan.toString();
                stage1.setPlan(plan1);
            }
            if (stage1.getPlan() == null) {
                String plan1 = "0";
                stage1.setPlan(plan1);
            }
            Project project = projectService.selectByPrimaryKey(stage1.getProjectId());
            BigDecimal projectBudget = project.getProjectBudget();
            // 项目预算
            Double projectCost = null;
            if (project != null) {
                projectCost = projectBudget.doubleValue();
            }
            TaskHourCost taskHourCost = new TaskHourCost();
            taskHourCost.setStageId(stage1.getId());
            BigDecimal bigDecimal1 = taskService.selectTaskHourCostList(taskHourCost);
            // 阶段实际预算
            Double stageCost = null;
            if (bigDecimal1 != null) {
                stageCost = bigDecimal1.doubleValue();
            }
            if (stageCost != null && projectCost != null && projectCost != 0) {
                costSum = stageCost / projectCost;
                String stringCost = costSum.toString();
                stage1.setCost(stringCost);
            } else {
                String stringCost = "0";
                stage1.setCost(stringCost);
            }
        }
        PageInfo<Stage> pageResult = new PageInfo<>(stages);
        pageResult.setList(stages);
        return pageResult;
    }

    @Override
    public boolean updateStageByProjectId(Stage stage) {
        Integer falg = stageMapper.updateStageByProjectId(stage);
        Boolean state = true;
        if (falg <= 0) {
            state = false;
        }
        return state;
    }

    @Override
    public PageInfo<Stage> selectStageBudgetByProjectId(Stage stage, Integer pageNum, Integer pageSize) {
        // 初始化分页信息
        PageHelper.startPage(pageNum, pageSize);
        // 查询任务list
        List<Stage> stage1 = stageMapper.selectStateByProjectId(stage);
        List<Stage> list = new ArrayList<>();
        List<Stage> list2 = new ArrayList<>();
        for (Stage stages : stage1) {
            TaskHourCost taskHourCost = new TaskHourCost();
            taskHourCost.setStageId(stages.getId());
            BigDecimal bigDecimal = taskService.selectTaskHourCostList(taskHourCost);
            Double stageCost = null;
            if (bigDecimal != null) {
                stageCost = bigDecimal.doubleValue();
            }
            if (stageCost != null) {
                if (stageCost > stages.getStageBudget()) {
                    list.add(stages);
                } else if (stageCost <= stages.getStageBudget()) {
                    list2.add(stages);
                }
            } else {
                list2.add(stages);
            }
        }
        stage1.clear();
        if (stage.getStageBudget() == Constant.Status.OVER_BUDGET) {
            stage1.addAll(list);
            // 超预算
        } else if (stage.getStageBudget() == Constant.Status.NO_OVER_BUDGET) {
            stage1.addAll(list2);
            // 未超预算
        }
        PageInfo<Stage> pageResult = new PageInfo<>(stage1);
        pageResult.setList(stage1);
        return pageResult;
    }

    @Override
    public List<Stage> selectStageBudget(Integer projectId) {
        Stage stage1 = new Stage();
        stage1.setProjectId(projectId);
        List<Stage> stage = stageMapper.selectStateByProjectId(stage1);
        commonMotner(projectId, stage);
        return stage;
    }

    private void commonMotner(Integer projectId, List<Stage> stage) {
        for (Stage stage1 : stage) {
            Double costSum = null;
            Double plan = null;
            WorkDTO workDTO = new WorkDTO();
            Integer id = stage1.getId();
            workDTO.setProjectId(projectId);
            workDTO.setStageId(id);
            List<WorkDTO> workList = workService.getWorkCostList(workDTO);
            workDTO.setState(Constant.Status.COMPLETED);
            List<WorkDTO> workList1 = workService.getWorkCostList(workDTO);
            Integer size = workList.size();
            Integer size1 = workList1.size();
            if (size1 != null && size != null && size != 0) {
                plan = size1 / size * 1.0;
                String plan1 = plan.toString();
                stage1.setPlan(plan1);
            }
            if (stage1.getPlan() == null) {
                String plan1 = "0";
                stage1.setPlan(plan1);
            }
            Double projectCost = null;
            Project project = projectService.selectByPrimaryKey(projectId);
            // 项目预算
            if (project != null) {
                BigDecimal projectBudget = project.getProjectBudget();
                projectCost = projectBudget.doubleValue();
            }
            TaskHourCost taskHourCost = new TaskHourCost();
            taskHourCost.setStageId(stage1.getId());
            BigDecimal bigDecimal1 = taskService.selectTaskHourCostList(taskHourCost);
            // 阶段实际预算
            Double stageCost = null;
            if (bigDecimal1 != null) {
                stageCost = bigDecimal1.doubleValue();
            }
            if (stageCost != null && projectCost != null && projectCost != 0) {
                costSum = stageCost / projectCost;
                String stringCost = costSum.toString();
                stage1.setCost(stringCost);
            } else {
                String stringCost = "0";
                stage1.setCost(stringCost);
            }

        }
    }
}
