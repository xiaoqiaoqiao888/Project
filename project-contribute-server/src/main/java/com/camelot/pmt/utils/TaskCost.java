package com.camelot.pmt.utils;

import com.camelot.pmt.mapper.ProjectUserMapper;
import com.camelot.pmt.mapper.SysUserMapper;
import com.camelot.pmt.mapper.TaskMapper;
import com.camelot.pmt.model.ProjectUser;
import com.camelot.pmt.model.SysUser;
import com.camelot.pmt.model.TaskDto;
import com.camelot.pmt.model.TaskHourCost;
import com.camelot.pmt.service.BaseCalculateService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: lyh
 * @CreateDate: 2018/5/16  16:20
 * @Description:
 */
@Slf4j
@Transactional
@Component
public class TaskCost {

    @Autowired
    private BaseCalculateService baseCalculateService;
    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private ProjectUserMapper projectUserMapper;
    Boolean flag = false;

    //0 0/5 9,10,11 * * ?
    @Scheduled(cron = "0 0/5 9,10,11 * * ?")
    private void amCosts() {
        Date date = new Date();
        flag = baseCalculateService.isWorkDays(new Date());
        insertList();
        insertCompletAndWait(date);
    }


    //0 0/5 14,15,16,17 * * ?
    //*/10 * * * * ?
    @Scheduled(cron = "0 0/5 14,15,16,17 * * ?")
    private void pmCosts() {
        Date date = new Date();
        flag = baseCalculateService.isWorkDays(date);
        insertList();
        insertCompletAndWait(date);
    }

    //0 0/5 9,10,11 * * ?
    @Scheduled(cron = "0 0/5 9,10,11 * * ?")
    private void amProjectUserCosts() {
        flag = baseCalculateService.isWorkDays(new Date());
        if (flag) {
            insert();
        }
    }

    //0 0/5 14,15,16,17 * * ?
    //*/10 * * * * ?
    @Scheduled(cron = "0 0/5 14,15,16,17 * * ?")
    private void pmProjectUserCosts() {
        flag = baseCalculateService.isWorkDays(new Date());
        if (flag) {
            insert();
        }
    }

    //0 0 0 * * ?
    //0 15 10 ? * *
    @Scheduled(cron = "0 0 0 * * ?")
    private void costs() {
        List<TaskHourCost> taskHourCostList = taskMapper.selectTaskHourCost();
        List<TaskHourCost> taskHourCostListProUser = taskMapper.taskHourCostProjectUser();
        if (taskHourCostList.size() > 0) {
            TaskHourCost thc = new TaskHourCost();
            for (TaskHourCost taskHourCost : taskHourCostList) {
                TaskHourCost taskHourCostObj = taskMapper.selectLastTaskHourCostByTaskId(taskHourCost.getTaskId());
                thc.setProjectId(taskHourCost.getProjectId());
                thc.setStageId(taskHourCost.getStageId());
                thc.setWorkId(taskHourCost.getWorkId());
                thc.setCost(taskHourCost.getSumCost());
                thc.setTaskState(taskHourCostObj.getTaskState());
                thc.setTaskId(taskHourCost.getTaskId());
                thc.setState(taskHourCost.getState());
                thc.setTaskPersonId(taskHourCost.getTaskPersonId());
                thc.setCreateBy(Constant.System.SUPER_ADMIN);
                thc.setUpdateBy(Constant.System.SUPER_ADMIN);
                taskMapper.deleteTaskHourCostByTaskId(taskHourCost.getTaskId());
                taskMapper.insertTaskHourCost(thc);
            }
        }
        if (taskHourCostListProUser.size() > 0) {
            TaskHourCost thc = new TaskHourCost();
            for (TaskHourCost taskHourCost : taskHourCostListProUser) {
                thc.setProjectId(taskHourCost.getProjectId());
                thc.setStageId(taskHourCost.getStageId());
                thc.setWorkId(taskHourCost.getWorkId());
                thc.setCost(taskHourCost.getSumCost());
                thc.setTaskState(0);
                thc.setTaskId(taskHourCost.getTaskId());
                thc.setState(taskHourCost.getState());
                thc.setTaskPersonId(taskHourCost.getTaskPersonId());
                thc.setCreateBy(Constant.System.SUPER_ADMIN);
                thc.setUpdateBy(Constant.System.SUPER_ADMIN);
                taskMapper.deleteTaskHourByProjectId(taskHourCost.getProjectId());
                taskMapper.insertTaskHourCost(thc);
            }
        }
    }

    //核心人员的没做任务的消耗人本
    private void insert() {
        List<ProjectUser> projectUsers = projectUserMapper.selectProjectUserCost();
        if (projectUsers.size() > 0) {
            List<SysUser> sysUsers = new ArrayList<>();
            for (ProjectUser projectUser : projectUsers) {
                SysUser sysUser = sysUserMapper.selectByPrimaryKey(projectUser.getUserId());
                sysUsers.add(sysUser);
            }
            List<SysUser> users = baseCalculateService.getPersonCost(sysUsers, new Date(), false);
            for (ProjectUser projectUser : projectUsers) {
                TaskHourCost taskHourCost = new TaskHourCost();
                taskHourCost.setTaskPersonId(projectUser.getUserId());
                taskHourCost.setProjectId(projectUser.getProjectId());
                taskHourCost.setStageId(0);
                taskHourCost.setWorkId(0);
                taskHourCost.setTaskId(0);
                taskHourCost.setState(projectUser.getState());
                taskHourCost.setTaskState(0);
                taskHourCost.setCreateBy(Constant.System.SUPER_ADMIN);
                taskHourCost.setUpdateBy(Constant.System.SUPER_ADMIN);
                for (SysUser sysUser : users) {
                    if (projectUser.getUserId() == sysUser.getId()) {
                        taskHourCost.setCost(sysUser.getCost().divide(new BigDecimal(12), 2,
                                BigDecimal.ROUND_HALF_UP));
                    }
                }
                taskMapper.insertTaskHourCost(taskHourCost);
            }
        }


    }

    private void insertCompletAndWait(Date date) {
        if (flag) {
            List<TaskDto> taskLists = taskMapper.selectTaskAndUserByTaskState(Constant.Status.COMPLETED,
                    Constant.Status.WAIT_CHECK);
            if (taskLists.size() > 0) {
                List<SysUser> sysUsers = new ArrayList<>();
                for (TaskDto taskObj : taskLists) {
                    Long realEndTime = (taskObj.getRelEndTime().getTime()) / (1000 * 60);
                    Long dateTime = date.getTime() / (1000 * 60);
                    if ((5 - (dateTime - realEndTime)) > 0) {
                        SysUser sysUser = sysUserMapper.selectByPrimaryKey(taskObj.getTaskPersonId());
                        if (!sysUsers.contains(sysUser)) {
                            sysUsers.add(sysUser);
                        }
                    }
                }
                List<SysUser> users = baseCalculateService.getPersonCost(sysUsers, new Date(), false);
                for (SysUser sysUser : users) {
                    BigDecimal cost = sysUser.getCost().divide(new BigDecimal(60), 2, BigDecimal.ROUND_HALF_UP);
                    List<TaskDto> taskDtoList = taskMapper.selectTaskByUserId(sysUser.getId());
                    for (TaskDto task1 : taskDtoList) {
                        if (task1.getTaskState() == Constant.Status.COMPLETED
                                || task1.getTaskState() == Constant.Status.WAIT_CHECK) {
                            Long realEndTime = (task1.getRelEndTime().getTime()) / (1000 * 60);
                            Long dateTime = date.getTime() / (1000 * 60);
                            if ((5 - (dateTime - realEndTime)) > 0) {
                                BigDecimal costs = cost.multiply(new BigDecimal((5 - (dateTime - realEndTime))));
                                TaskHourCost taskHourCost = new TaskHourCost();
                                taskHourCost.setTaskPersonId(sysUser.getId());
                                taskHourCost.setCost(costs);
                                taskHourCost.setProjectId(task1.getProjectId());
                                taskHourCost.setStageId(task1.getStageId());
                                taskHourCost.setWorkId(task1.getWorkId());
                                taskHourCost.setTaskId(task1.getId());
                                taskHourCost.setState(task1.getState());
                                taskHourCost.setTaskState(task1.getTaskState());
                                taskHourCost.setCreateBy(Constant.System.SUPER_ADMIN);
                                taskHourCost.setUpdateBy(Constant.System.SUPER_ADMIN);
                                taskMapper.insertTaskHourCost(taskHourCost);
                            }

                        }
                    }

                }
            }

        }
    }

    //做任务的人员消耗成本
    private void insertList() {
        List<TaskDto> taskList = taskMapper.selectTaskAndUserByTaskState(Constant.Status.HAVE_IN_HAND,
                Constant.Status.DELAY_HAVE_IN_HAND);
        List<SysUser> sysUsers = new ArrayList<>();
        if (taskList.size() > 0) {
            for (TaskDto taskObj : taskList) {
                SysUser sysUser = sysUserMapper.selectByPrimaryKey(taskObj.getTaskPersonId());
                sysUsers.add(sysUser);
            }
            List<SysUser> users = baseCalculateService.getPersonCost(sysUsers, new Date(), false);
            for (SysUser sysUser : users) {
                BigDecimal cost = sysUser.getCost().divide(new BigDecimal(12), 2, BigDecimal.ROUND_HALF_UP);
                List<TaskDto> taskDtoList = taskMapper.selectTaskByUserId(sysUser.getId());
                for (TaskDto task1 : taskDtoList) {
                    if (task1.getTaskState() == Constant.Status.HAVE_IN_HAND
                            || task1.getTaskState() == Constant.Status.DELAY_HAVE_IN_HAND) {
                        TaskHourCost taskHourCost = new TaskHourCost();
                        taskHourCost.setTaskPersonId(sysUser.getId());
                        taskHourCost.setCost(cost);
                        taskHourCost.setProjectId(task1.getProjectId());
                        taskHourCost.setStageId(task1.getStageId());
                        taskHourCost.setWorkId(task1.getWorkId());
                        taskHourCost.setTaskId(task1.getId());
                        taskHourCost.setState(task1.getState());
                        taskHourCost.setTaskState(task1.getTaskState());
                        taskHourCost.setCreateBy(Constant.System.SUPER_ADMIN);
                        taskHourCost.setUpdateBy(Constant.System.SUPER_ADMIN);
                        taskMapper.insertTaskHourCost(taskHourCost);
                    }
                }

            }
        }


    }

}
