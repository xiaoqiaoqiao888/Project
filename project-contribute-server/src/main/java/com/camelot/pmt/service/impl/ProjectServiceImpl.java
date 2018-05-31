package com.camelot.pmt.service.impl;

import com.camelot.pmt.mapper.ProjectMapper;
import com.camelot.pmt.mapper.ProjectUserMapper;
import com.camelot.pmt.mapper.StageMapper;
import com.camelot.pmt.model.CoreMember;
import com.camelot.pmt.model.Project;
import com.camelot.pmt.model.ProjectDTO;
import com.camelot.pmt.model.Stage;
import com.camelot.pmt.model.Work;
import com.camelot.pmt.service.LogProjectService;
import com.camelot.pmt.service.ProjectService;
import com.camelot.pmt.service.StageService;
import com.camelot.pmt.service.WorkService;
import com.camelot.pmt.utils.Constant;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectMapper projectMapper;

    @Autowired
    private ProjectUserMapper projectUserMapper;

    @Autowired
    private StageMapper stageMapper;

    @Autowired
    private LogProjectService logProjectService;

    @Autowired
    private WorkService workService;

    @Autowired
    private StageService stageService;

    public static final String LOG_PROJECT_NEW = "新增项目";

    public static final String LOG_PROJECT_UPDATE = "修改项目";

    public static final String LOG_PROJECT_START = "开始项目";

    public static final String LOG_PROJECT_SHUTDOWN = "关闭项目";

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public PageInfo<?> list(Integer pageNum, Integer pageSize, Integer projectState, String projectName) {
        // 初始化分页信息
        PageHelper.startPage(pageNum, pageSize);
        // 查询菜单list
        HashMap<String, Object> listMap = new HashMap<>();
        listMap.put("projectState", projectState);
        if (!StringUtils.isEmpty(projectName)) {
            listMap.put("projectName", projectName);
        }
        List<Map<String, Object>> projectList = projectMapper.selectProjectList(listMap);
        // pageHelper的收尾
        PageInfo<Map<String, Object>> pageResult = new PageInfo<>(projectList);
        return pageResult;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public int add(Project project) throws CloneNotSupportedException {
        projectMapper.insertSelective(project);
        project.setOperateDesc(LOG_PROJECT_NEW);
        project.setProjectId(project.getId());
        int id = project.getId();
        logProjectService.logOfAdd(project);
        return id;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public Project selectByPrimaryKey(Integer id) {
        Project project = projectMapper.selectByPrimaryKey(id);
        return project;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public boolean update(Project project) throws CloneNotSupportedException {
        boolean flag = false;
        int count = projectMapper.updateByPrimaryKeySelective(project);
        if (count > 0) {
            flag = true;
        }
        project.setProjectId(project.getId());
        project.setOperateDesc(LOG_PROJECT_UPDATE);
        logProjectService.logOfAdd(project);
        return flag;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void addCoreMember(List<CoreMember> coreMember) {
        projectUserMapper.addCoreMember(coreMember);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public List<Project> selectProjectLists(Map<String, Object> listMap) {
        return projectMapper.selectProjectLists(listMap);
    }

    /**
     * 开始项目
     * 
     * @throws CloneNotSupportedException
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public boolean updateProjectTime(Project project) throws CloneNotSupportedException {
        boolean flag = false;
        // 开始项目
        int count = projectMapper.updateProjectTime(project);
        if (count > 0) {
            flag = true;
            List<Stage> selectAllStage = stageMapper.selectAllStage(project.getId());
            Stage stage = new Stage();
            stage.setProjectId(project.getId());
            // 阶段状态进行中
            stage.setStageState(Constant.Status.HAVE_IN_HAND);
            for (Stage stages : selectAllStage) {
                if (stages.getStageState() != null && stages.getStageState() == Constant.Status.NO_START) {
                    // 阶段开始
                    stageMapper.updateStageByProjectId(stage);
                }
            }
        }
        project.setProjectId(project.getId());
        project.setOperateDesc(LOG_PROJECT_START);
        project.setProjectState(Constant.Status.HAVE_IN_HAND);
        project.setState(Constant.DataStatus.EFFECTIVE);
        logProjectService.logOfAdd(project);
        return flag;
    }

    /**
     * 定时任务：判断项目是否逾期进行中
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void updateProjectDelay(Date date) {
        Map<String, Object> map = new HashMap<String, Object>();
        List<Project> selectProjectLists = projectMapper.selectProjectLists(map);
        for (Project pro : selectProjectLists) {
            Integer state = pro.getProjectState();
            String endTime = pro.getEndTime();
            if (state != null && state == Constant.Status.HAVE_IN_HAND) {
                try {
                    long projectEndTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endTime).getTime();
                    long nowTime = date.getTime();
                    if (nowTime > projectEndTime) {
                        projectMapper.updateProjectDelay(pro);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

    }

    /**
     * 项目关闭
     * 
     * @throws CloneNotSupportedException
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public boolean updateProjectEnd(Project project) throws CloneNotSupportedException {
        List<Integer> list = new ArrayList<Integer>();
        List<Work> workList = workService.getWorkList(project.getId(), null, null);
        if (workList != null && workList.size() > 0) {
            for (Work work : workList) {
                list.add(work.getId());
                if (work.getWorkState() > 0) {
                    return false;
                }
            }
        }
        // 项目关闭
        projectMapper.updateProjectEnd(project);
        Stage stage = new Stage();
        stage.setState(Constant.DataStatus.INVALID);
        stage.setProjectId(project.getId());
        // 阶段关闭
        stageService.closeStageByProjectID(stage);
        if (list.size() > 0) {
            // 提供工程包id
            int[] ids = new int[list.size()];
            for (int i = 0; i < list.size(); i++) {
                ids[i] = list.get(i);
            }
            // 工程包关闭
            workService.deleteWork(ids);
        }
        project.setProjectId(project.getId());
        project.setOperateDesc(LOG_PROJECT_SHUTDOWN);
        project.setState(Constant.DataStatus.INVALID);
        logProjectService.logOfAdd(project);
        return true;
    }

    /**
     * 定时任务：判断项目是否已完成
     */
    @Override
    public void updateProjectFinsh() {
        List<Project> selectProjectState = projectMapper.selectProjectState();
        for (Project pro : selectProjectState) {
            int countAll = projectMapper.selectProStageAll(pro.getId());
            int count = projectMapper.selectProStageFinsh(pro.getId());
            if (countAll > 0 && count > 0 && countAll == count) {
                projectMapper.updateProjectFinsh(pro);
            }
        }
    }

    @Override
    public int selectProjectNo(String id) {
        int count = projectMapper.selectProjectNo(id);
        return count;
    }

    /**
     * 阶段统计
     */
    @Override
    public List<ProjectDTO> selectStateCount() {
        List<Integer> list = new ArrayList<Integer>();
        List<ProjectDTO> selectStateCount = projectMapper.selectStateCount();
        for (ProjectDTO projectDTO : selectStateCount) {
            list.add(projectDTO.getProjectState());
        }
        if (!list.contains(Constant.Status.NO_START)) {
            selectStateCount.add(new ProjectDTO(Constant.Status.NO_START, 0, new BigDecimal(0)));
        }
        if (!list.contains(Constant.Status.HAVE_IN_HAND)) {
            selectStateCount.add(new ProjectDTO(Constant.Status.HAVE_IN_HAND, 0, new BigDecimal(0)));
        }
        if (!list.contains(Constant.Status.COMPLETED)) {
            selectStateCount.add(new ProjectDTO(Constant.Status.COMPLETED, 0, new BigDecimal(0)));
        }
        if (!list.contains(Constant.Status.DELAY_HAVE_IN_HAND)) {
            selectStateCount.add(new ProjectDTO(Constant.Status.DELAY_HAVE_IN_HAND, 0, new BigDecimal(0)));
        }
        return selectStateCount;
    }

}
