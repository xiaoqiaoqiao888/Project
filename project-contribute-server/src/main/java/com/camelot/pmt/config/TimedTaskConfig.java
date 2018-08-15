package com.camelot.pmt.config;

import com.camelot.pmt.service.ProjectService;
import com.camelot.pmt.service.StageService;
import com.camelot.pmt.service.TaskService;
import com.camelot.pmt.service.WorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Auther: za
 * @Date: 2018/5/17 09:58
 * @Description:
 */
@Component
public class TimedTaskConfig {

    @Autowired
    private TaskService taskService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private StageService stageService;

    @Autowired
    private WorkService workService;

    /**
     * 定时任务查询是否有延期进行中任务,并修改状态(进行中-延期进行中,每天凌晨00:01执行)
     * 
     * @throws Exception
     */
    @Scheduled(cron = "0 1 0 * * ?")
    public void isOverTask() throws Exception {
        taskService.isOverTask();
    }

    /**
     * 判断项目状态是否逾期进行中(每天凌晨00:06:00执行) cron属性：cron表达式。定时任务触发是时间的一个字符串表达形式
     */
    @Scheduled(cron = "0 6 0 * * ?")
    public void scheduledProOvertime() {
        projectService.updateProjectDelay(new Date());

    }

    /**
     * 判断项目状态是否完成(每天凌晨00:03:00执行) cron属性：cron表达式。定时任务触发是时间的一个字符串表达形式
     */
    @Scheduled(cron = "0 3 0 * * ?")
    public void scheduledProFinsh() {
        projectService.updateProjectFinsh();

    }

    /**
     * 判断阶段状态是否逾期进行中(每天凌晨00:00:00执行)
     *
     * cron属性：cron表达式。定时任务触发是时间的一个字符串表达形式
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void scheduledStage() {
        stageService.updateStageState(new Date());

    }

    /**
     * 判断功能包状态是否逾期进行中(每天凌晨00:05:00执行)
     *
     * cron属性：cron表达式。定时任务触发是时间的一个字符串表达形式
     * 
     * @Auther: xueyj
     */
    @Scheduled(cron = "0 5 0 * * ?")
    public void scheduledWorkStatus() {
        workService.isOverWork();
    }
}
