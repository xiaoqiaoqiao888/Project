package com.camelot.pmt.service.impl;

import com.camelot.pmt.mapper.ProjectUserMapper;
import com.camelot.pmt.mapper.TaskMapper;
import com.camelot.pmt.model.CoreMember;
import com.camelot.pmt.model.ProjectUser;
import com.camelot.pmt.model.ProjectUserInfo;
import com.camelot.pmt.model.SysUser;
import com.camelot.pmt.service.ProjectUserService;
import com.camelot.pmt.service.TaskService;
import com.camelot.pmt.utils.TokenUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class ProjectUserServiceImpl implements ProjectUserService {

    @Autowired
    private ProjectUserMapper projectUserMapper;

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskMapper taskMapper;

    private static final Logger log = LoggerFactory.getLogger(ProjectUserServiceImpl.class);

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void addCoreMember(List<CoreMember> coreMember) {
        projectUserMapper.addCoreMember(coreMember);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void deleteCoreUserByRecoredId(Integer recoredId) {
        projectUserMapper.deleteByPrimaryKey(recoredId);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public List<ProjectUser> searchCoreMember(Integer projectId) {
        return projectUserMapper.searchCoreMember(projectId);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public boolean inProject(int userId, int projectId) {
        // 查询成员作为普通成员是否存在于列表中
        ProjectUser user = projectUserMapper.selectByUserId(userId, projectId);
        if (user != null) {
            log.info("成员已经在项目中，id = [{}], 项目id = [{}]", userId, projectId);
            Date updateTime = new Date();
            projectUserMapper.updateUserRecord(updateTime, projectId, userId);
            return true;
        } else {
            // 查询成员是否为项目核心成员
            ProjectUser puser = new ProjectUser();
            puser.setProjectId(projectId);
            puser.setUserId(userId);
            puser.setState(CoreMember.PROJECT_USER_STATE_EFFECTIVE);
            puser.setType(CoreMember.PROJECT_USER_TYPE_CORE);
            ProjectUser user2 = projectUserMapper.isCoreMember(puser);
            if (user2 != null) {
                log.info("该成员在项目中为核心成员，并且成员数据状态为有效");
                return true;
            }
        }
        ProjectUser pu = new ProjectUser();
        pu.setUserId(userId);
        pu.setProjectId(projectId);
        pu.setType(CoreMember.PROJECT_USER_TYPE_COMMON);
        pu.setState(CoreMember.PROJECT_USER_STATE_EFFECTIVE);
        pu.setCreateBy(userId);
        pu.setCreateTime(new Date());
        pu.setUpdateBy(userId);
        pu.setUpdateTime(new Date());
        int result = projectUserMapper.insertSelective(pu);
        if (result > 0) {
            return true;
        }
        return false;
    }

    /**
     * @author guodx delete core member according to [projectId] and [userId] if the
     *         member has unfinished tasks in the project change member into
     *         ordinary member else update [projectUser.state] to invalid
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void deleteCoreUser(Integer projectId, Integer userId) {
        SysUser sysUser = TokenUtil.getUserFromToken();
        ProjectUser puser = new ProjectUser();
        puser.setUserId(userId);
        puser.setProjectId(projectId);
        puser.setUpdateTime(new Date());
        puser.setOutTime(new Date());
        puser.setState(CoreMember.PROJECT_USER_STATE_DISABLE);
        puser.setUpdateBy(sysUser.getId());
        int count = taskService.isCompleteTask(userId);
        if (count > 0) {
            log.info("成员还有未完成的任务，转为普通成员");
            // 根据项目成员类型=1（普通成员）查询
            ProjectUser user = projectUserMapper.searchByType(CoreMember.PROJECT_USER_TYPE_COMMON);
            if (user == null) {
                // 项目成员作为普通成员的记录不存在，插入一条作为普通成员的数据
                ProjectUser record = new ProjectUser();
                record.setUserId(userId);
                record.setProjectId(projectId);
                record.setCreateBy(sysUser.getId());
                record.setCreateTime(new Date());
                record.setUpdateBy(sysUser.getId());
                record.setUpdateTime(new Date());
                record.setType(CoreMember.PROJECT_USER_TYPE_COMMON);
                record.setState(CoreMember.PROJECT_USER_STATE_EFFECTIVE);
                projectUserMapper.insertSelective(record);
            }
        }
        // 修改核心成员数据，【逻辑删除核心成员】
        projectUserMapper.deleteCoreUser(puser);
    }

    /**
     * @author guodx 查询参与过项目的成员总数
     */
    @Override
    public Integer countUserByProjectId(Integer projectId) {
        Integer count = projectUserMapper.countUser(projectId);
        return count;
    }

    @Override
    public Integer sumRelTaskTime(Integer projectId) {
        Integer realTaskTimeSum = taskMapper.sumRealTaskTime(projectId);
        return realTaskTimeSum;
    }

    @Override
    public Integer sumExpTaskTime(Integer projectId) {
        return taskMapper.sumExpTaskTime(projectId);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public Map<String, Integer> sumProjectUser(Integer projectId) {
        Map<String, Integer> map = new HashMap<>();
        map.put("userNum", this.countUserByProjectId(projectId));
        Integer sumRelTaskTime = this.sumRelTaskTime(projectId);
        Integer sumExpTaskTime = this.sumExpTaskTime(projectId);
        map.put("realTaskTimeNum", sumRelTaskTime);
        map.put("overtime", (sumExpTaskTime - sumRelTaskTime) >= 0 ? 0 : (sumRelTaskTime - sumExpTaskTime));
        return map;
    }

    @Override
    public PageInfo<ProjectUserInfo> search(ProjectUserInfo info) {
        PageHelper.startPage(info.getPage(), info.getSize());
        List<ProjectUserInfo> list = projectUserMapper.searchUserOfProject(info);
        Iterator<ProjectUserInfo> iterator = list.iterator();
        while (iterator.hasNext()) {
            ProjectUserInfo infos = iterator.next();
            Map<String, Map<String, Double>> userStat = taskMapper.userStat(infos);
            System.out.println(userStat.get("taskValueSum").get("value"));
            infos.setTaskValueSum(userStat.get("taskValueSum").get("value"));
            infos.setRelTaskTimeSum(userStat.get("relTaskTimeSum").get("value"));
            infos.setExpTaskTimeSum(userStat.get("expTaskTimeSum").get("value"));
            infos.setSubmitTaskSum(userStat.get("submitTaskSum").get("value"));
            infos.setFinishedTaskSum(userStat.get("finishedTaskSum").get("value"));
        }
        PageInfo<ProjectUserInfo> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

}
