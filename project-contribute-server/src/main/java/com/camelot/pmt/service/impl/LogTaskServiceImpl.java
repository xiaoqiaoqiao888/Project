package com.camelot.pmt.service.impl;

import com.camelot.pmt.mapper.LogTaskMapper;
import com.camelot.pmt.model.LogTask;
import com.camelot.pmt.model.LogTaskDTO;
import com.camelot.pmt.model.SysUser;
import com.camelot.pmt.model.Task;
import com.camelot.pmt.service.LogTaskService;
import com.camelot.pmt.utils.TokenUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Auther: za
 * @Date: 2018/5/18 14:29
 * @Description: 任务添加日志
 */
@Service
public class LogTaskServiceImpl implements LogTaskService {
    @Autowired
    private LogTaskMapper logTaskMapper;

    @Override
    public int insertLogTask(String operateDesc, Task task) {
        SysUser sysUser = TokenUtil.getUserFromToken();
        LogTask logTask = new LogTask();
        logTask.setOperateDesc(operateDesc);
        logTask.setCreateBy(sysUser.getId());
        logTask.setCreateTime(new Date());
        logTask.setExpEndTime(task.getExpEndTime());
        logTask.setExpStartTime(task.getExpStartTime());
        logTask.setPriority(task.getPriority());
        logTask.setProjectId(task.getProjectId());
        logTask.setRelEndTime(task.getRelEndTime());
        logTask.setRelStartTime(task.getRelStartTime());
        logTask.setStageId(task.getStageId());
        logTask.setState(task.getState());
        logTask.setTaskCode(task.getTaskCode());
        logTask.setTaskDesc(task.getTaskDesc());
        logTask.setTaskId(task.getId());
        logTask.setTaskName(task.getTaskName());
        logTask.setTaskPersonId(task.getTaskPersonId());
        logTask.setTaskSchedule(task.getTaskSchedule());
        logTask.setTaskState(task.getTaskState());
        logTask.setTaskValue(task.getTaskValue());
        logTask.setUpdateBy(sysUser.getId());
        logTask.setUpdateTime(new Date());
        logTask.setWorkId(task.getWorkId());
        logTask.setComplexity(task.getComplexity());
        logTask.setRelTaskTime(task.getRelTaskTime());
        logTask.setExpTaskTime(task.getExpTaskTime());
        int insertnum = logTaskMapper.insertSelective(logTask);
        return insertnum;
    }

    /**
     * 根据任务领取人id查询任务操作记录
     *
     * @param state
     * @param taskPersonId
     * @param monthNum
     * @return
     */
    @Override
    public List<LogTaskDTO> selectByTaskPersonId(Integer state, Integer taskPersonId, Integer monthNum) {

        if (taskPersonId == null || state == null || monthNum == null) {
            return null;
        }
        // 获取指定时间内的操作日志
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, monthNum * (-1));
        return logTaskMapper.selectByTaskPersonId(state, taskPersonId, cal.getTime(), Calendar.getInstance().getTime())
                .stream().sorted(//
                        Comparator.comparing(LogTaskDTO::getUpdateTime))
                .collect(Collectors.toList());
    }
}
