package com.camelot.pmt.service.impl;

import com.camelot.pmt.mapper.LogSysGroupMapper;
import com.camelot.pmt.mapper.ProjectMapper;
import com.camelot.pmt.mapper.SysUserGroupMapper;
import com.camelot.pmt.mapper.TaskMapper;
import com.camelot.pmt.mapper.WorkMapper;
import com.camelot.pmt.model.Project;
import com.camelot.pmt.model.Stage;
import com.camelot.pmt.model.SysResourceDTO;
import com.camelot.pmt.model.SysUser;
import com.camelot.pmt.model.Task;
import com.camelot.pmt.model.Work;
import com.camelot.pmt.service.CheckManagerService;
import com.camelot.pmt.service.LogProjectService;
import com.camelot.pmt.utils.Constant;
import com.camelot.pmt.utils.TokenUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * * @author muyuanpei * @date 2018/5/14
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CheckManagerServiceImpl implements CheckManagerService {

    @Autowired
    private SysUserGroupMapper sysUserGroupMapper;

    @Autowired
    private LogSysGroupMapper logSysGroupMapper;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private WorkMapper workMapper;

    @Autowired
    private ProjectMapper projectMapper;

    @Autowired
    private CheckManagerService checkManagerService;

    @Autowired
    private LogProjectService logProjectService;

    @Override
    public int updateStateList(SysResourceDTO sysResourceVO) throws CloneNotSupportedException {
        SysUser user = TokenUtil.getUserFromToken();
        String[] split = sysResourceVO.getTrueIdStr().split(",");
        List<String> asList = Arrays.asList(split);
        if (sysResourceVO.getStatus() == Constant.CheckManager.DIFF_TASK_SIGN) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("taskState", Constant.Status.COMPLETED);
            map.put("asList", asList);
            map.put("updateBy", user.getId());
            map.put("createBy", user.getId());
            logSysGroupMapper.addTaskLog(map);
            int result = sysUserGroupMapper.updateTaskList(map);
            return result;
        } else if (sysResourceVO.getStatus() == Constant.CheckManager.DIFF_WORK_SIGN) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("asList", asList);
            map.put("updateBy", user.getId());
            map.put("workState", Constant.Status.COMPLETED);
            map.put("createBy", user.getId());
            int result = sysUserGroupMapper.updateWorkList(map);
            int workLogResult = logSysGroupMapper.addWorkLog(map);
            if (result > 0 && workLogResult > 0) {
                for (String strList : asList) {
                    List<Integer> taskIntList = new ArrayList<>();
                    Work work = new Work();
                    work.setId(Integer.valueOf(strList));
                    List<Task> taskList = sysUserGroupMapper.queryTaskListByWork(work);
                    if (taskList.size() > 0) {
                        for (Task task : taskList) {
                            taskIntList.add(task.getId());
                        }
                        map.put("asList", taskIntList);
                        map.put("updateBy", user.getId());
                        map.put("createBy", user.getId());
                        map.put("taskState", Constant.Status.COMPLETED);
                        sysUserGroupMapper.updateTaskList(map);
                        logSysGroupMapper.addTaskLog(map);
                    }
                }
                return result;
            }
            return result;
        } else {
            HashMap<String, Object> map = new HashMap<>();
            map.put("asList", asList);
            map.put("updateBy", user.getId());
            map.put("createBy", user.getId());
            map.put("stageState", Constant.Status.COMPLETED);
            int result = sysUserGroupMapper.updateStageList(map);
            Boolean checkProject = checkManagerService.checkProject(sysResourceVO);
            if (checkProject == true) {
                int finsh = checkManagerService.updateProjectFinsh(sysResourceVO.getProjectId());
                if (finsh > 0) {
                    Project project = new Project();
                    project.setOperateDesc("项目完成");
                    project.setId(sysResourceVO.getProjectId());
                    logProjectService.logOfAdd(project);
                }
            }
            int stageLogResult = logSysGroupMapper.addStageLog(map);
            if (result > 0 && stageLogResult > 0) {
                for (String strList : asList) {
                    List<Integer> workList = new ArrayList<>();
                    List<Work> works = workMapper.queryWorkListByParams(null, Integer.valueOf(strList));
                    if (works.size() > 0) {
                        for (Work work : works) {
                            List<Task> taskList = sysUserGroupMapper.queryTaskListByWork(work);
                            if (taskList.size() > 0) {
                                List<Integer> taskIntList = new ArrayList<>();
                                for (Task task : taskList) {
                                    taskIntList.add(task.getId());
                                }
                                if (taskIntList.size() > 0) {
                                    map.put("asList", taskIntList);
                                    map.put("updateBy", user.getId());
                                    map.put("createBy", user.getId());
                                    map.put("taskState", Constant.Status.COMPLETED);
                                    sysUserGroupMapper.updateTaskList(map);
                                    logSysGroupMapper.addTaskLog(map);
                                }
                            }
                            if (work.getWorkState() == Constant.Status.WAIT_CHECK) {
                                workList.add(work.getId());
                            }
                        }
                        if (workList.size() > 0) {
                            map.put("asList", workList);
                            map.put("updateBy", user.getId());
                            map.put("createBy", user.getId());
                            map.put("workState", Constant.Status.COMPLETED);
                            sysUserGroupMapper.updateWorkList(map);
                            logSysGroupMapper.addWorkLog(map);
                        }
                    }
                }
                return result;
            } else {
                return result;
            }
        }
    }

    @Override
    public PageInfo<Map<String, Object>> selectTaskListByPageInfo(Task task, Integer pageNum, Integer pageSize) {
        if (null == task) {
            throw new IllegalArgumentException(Constant.CheckManager.REQUEST_DATA_EXCEPTION);
        }
        // 初始化分页信息
        PageHelper.startPage(pageNum == 0 ? 1 : pageNum, pageSize == 0 ? 10 : pageSize);
        // 查询任务list
        List<Map<String, Object>> taskList = sysUserGroupMapper.queryTaskListByParamsOrder(task);
        // pageHelper的收尾
        PageInfo<Map<String, Object>> pageResult = new PageInfo<>(taskList);
        pageResult.setList(taskList);
        return pageResult;
    }

    @Override
    public List<Work> getWorkListByStageId(Stage stage) {
        return workMapper.queryWorkListByParams(null, stage.getId());
    }

    @Override
    public List<Task> getTaskListByWorkId(Work work) {
        return sysUserGroupMapper.queryTaskListByWork(work);
    }

    @Override
    public int updateByPrimaryKeySelective(Task task) {
        return taskMapper.updateByPrimaryKeySelective(task);
    }

    @Override
    public int addTaskLog(Map<String, Object> map) {
        return logSysGroupMapper.addTaskLog(map);
    }

    @Override
    public int addWorkLog(Map<String, Object> map) {
        return logSysGroupMapper.addWorkLog(map);
    }

    @Override
    public int addStageLog(Map<String, Object> map) {
        return logSysGroupMapper.addStageLog(map);
    }

    @Override
    public Boolean checkProject(SysResourceDTO sysResourceVO) {
        Boolean flag = null;
        List<Stage> stageList = sysUserGroupMapper.selectStageListByProjectId(sysResourceVO);
        for (Stage stage : stageList) {
            if (stage.getStageState() == 3) {
                flag = true;
            } else {
                flag = false;
                break;
            }
        }
        return flag;
    }

    @Override
    public int updateProjectFinsh(Integer pid) {
        Project project = new Project();
        project.setId(pid);
        project.setUpdateBy(TokenUtil.getUserFromToken().getId());
        return projectMapper.updateProjectFinsh(project);
    }

}
