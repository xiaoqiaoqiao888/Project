package com.camelot.pmt.service;

import com.camelot.pmt.model.Task;
import com.camelot.pmt.model.TaskCountDTO;
import com.camelot.pmt.model.TaskHourCost;
import com.github.pagehelper.PageInfo;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author za
 * @ClassName: TaskService
 * @Description: TODO 任务管理接口
 * @date 2018年5月14日
 */
public interface TaskService {
    /**
     * @param n
     * @param page
     * @param rows
     * @param taskName
     * @param personId
     * @param orderByState
     * @param taskPersonId @return PageInfo<?>
     * @throws list
     * @Description TODO 查询(任务详情,可领取任务,我的任务)通用接口
     */
    PageInfo<Map<String, Object>> list(Integer page, Integer rows, String taskName, String projectName, Integer
            personId, Integer orderByState, Integer taskState);

    /**
     * @param task
     * @return int
     * @throws updateTask
     * @Description TODO (修改任务,提交任务,领取任务)通用接口
     */
    int updateTask(Task task) throws CloneNotSupportedException;

    /**
     * @param task
     * @return boolean
     * @throws add
     * @Description: TODO 添加拆分任务
     */
    boolean add(Task task);

    /**
     * 分页查询任务清单
     *
     * @param pageNum
     * @param pageSize
     * @param task
     * @return
     */
    PageInfo<?> selectTaskListByPageInfo(Task task, Integer pageNum, Integer pageSize);

    /**
     * 查询任务不同状态的个数
     *
     * @param task
     * @return
     */
    TaskCountDTO selectTasklistByTaskState(Task task);



    /**
     * 个人任务统计
     *
     * @param id
     * @return
     */
    Map<String, Integer> countTask(Integer id);

    /**
     * 查询个人是否有未完成的任务
     *
     * @param id
     * @return
     */
    int isCompleteTask(Integer id);

    /**
     * 查询某个时间段内的各个任务总和
     *
     * @param taskPersonId
     * @param state
     * @param monthNum
     * @return
     */
    Map<String, Object> selectPeriodTotalTask(Integer taskPersonId, Integer state, Integer monthNum);

    /**
     * 定时查询是否有延期任务并修改状态
     */
    void isOverTask();

    /**
     * 根据是否超预算状态值来查询任务清单
     */

    PageInfo<?> selectTaskByBudgetState(Task task, Integer budgetState,Integer pageNum,Integer pageSize);

    /**
     * 根据任务id查询任务详情
     *
     * @param id
     * @return
     */
    Map<String, Object> taskDetail(Integer id);

    /**
     * 根据任务id查询日志
     *
     * @param taskId
     * @return
     */
    List<Map<String, Object>> logTaskByTaskId(Integer taskId);


    BigDecimal selectTaskHourCostList(TaskHourCost taskHourCost);

    /**
     *检测工程包下的任务是否超过工程包价值分
     * @param workId
     * @param taskId
     * @param taskValue
     * @return
     */
    ResponseEntity<String> checkTaskValue(Integer workId, Integer taskId, Integer taskValue);
}
