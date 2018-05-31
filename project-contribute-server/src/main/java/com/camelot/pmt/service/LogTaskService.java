package com.camelot.pmt.service;

import com.camelot.pmt.model.LogTaskDTO;
import com.camelot.pmt.model.Task;

import java.util.List;

/**
 * @Auther: za
 * @Date: 2018/5/18 10:25
 * @Description:
 */
public interface LogTaskService {

    int insertLogTask(String operateDesc, Task task);

    /**
     * 根据任务领取人id查询任务操作记录
     *
     * @param state
     * @param taskPersonId
     * @return
     */
    List<LogTaskDTO> selectByTaskPersonId(Integer state, Integer taskPersonId, Integer monthNum);
}
