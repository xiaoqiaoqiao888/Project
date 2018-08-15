package com.camelot.pmt.mapper;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import com.camelot.pmt.model.ProjectUserInfo;
import com.camelot.pmt.model.SysUserTaskDTO;
import com.camelot.pmt.model.Task;
import com.camelot.pmt.model.TaskDto;
import com.camelot.pmt.model.TaskHourCost;

public interface TaskMapper {
    /**
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * @mbggenerated
     */
    int insert(Task record);

    /**
     * @mbggenerated
     */
    int insertSelective(Task record);

    /**
     * @mbggenerated
     */
    Task selectByPrimaryKey(Integer id);

    /**
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(Task record);

    /**
     * @mbggenerated
     */
    int updateByPrimaryKeyWithBLOBs(Task record);

    /**
     * @mbggenerated
     */
    int updateByPrimaryKey(Task record);

    /**
     * @param listMap
     * @Title selectTaskList
     * @Description TODO 查询(任务详情,可领取任务,我的任务)通用接口
     */
    List<Map<String, Object>> selectTaskList(HashMap<String, Object> listMap);

    /**
     * 分页查询任务清单列表
     */
    List<TaskDto> selectTasksList(Task task);

    /**
     * 依据项目id，阶段id，工程包id，查询当前工程包下子任务信息
     *
     * @param projectId
     * @param stageId
     * @param id
     * @return
     */
    List<Task> queryTaskListByParams(@Param(value = "project_id") Integer projectId,
            @Param(value = "stage_id") Integer stageId, @Param(value = "work_id") Integer id);

    /**
     * 个人任务统计总任务
     *
     * @param id
     * @return
     */
    Integer countTaskSum(Integer id);

    // /**
    // * 个人任务统计延期任务
    // *
    // * @param id
    // * @return
    // */
    // Integer countDelayTask(Integer id);

    /**
     * 个人任务统计超时任务
     *
     * @param id
     * @return
     */
    Integer countOvertimeTask(Integer id);

    /**
     * 查询个人是否有未完成的任务
     *
     * @param id
     * @return
     */
    int isCompleteTask(Integer id);

    /**
     * 查询所有任务
     */
    List<TaskDto> selectTaskAll();

    /**
     * 根据状态查询任务和用户关联
     */
    List<TaskDto> selectTaskAndUserByTaskState(@Param("state1") Integer state1, @Param("state2") Integer state2);

    /**
     * 插入任务每小时的预算成本
     */
    int insertTaskHourCost(TaskHourCost taskHourCost);

    /**
     * 查询任务每小时的预算成本
     */
    List<TaskHourCost> selectTaskHourCostByTaskId(Integer taskId);

    /**
     * 查询某个时间段内的各个任务总和
     *
     * @param beginTime
     * @param endTime
     * @param taskPersonId
     * @param state
     * @return
     */
    List<SysUserTaskDTO> selectPeriodTotalTask(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime,
            @Param("taskPersonId") Integer taskPersonId, @Param("state") Integer state);

    /**
     * 定时查询是否有延期任务,
     *
     * @return
     */
    List<Task> isOverTask();

    /**
     * 修改延期任务状态(进行中-延期进行中)
     *
     * @param list
     */
    void updateOverTask(List<Task> list);

    /**
     * 根据是否超预算状态值来查询任务清单
     */
    TaskHourCost selectTaskByBudgetState(Integer taskId);

    List<TaskDto> selectTaskByUserId(Integer userId);

    /**
     * @param id
     * @return
     */
    Map<String, Object> taskDetail(Integer id);

    /**
     * 查询成本记录表
     *
     * @return
     */
    List<TaskHourCost> selectTaskHourCost();

    /**
     * 根据任务id 查询成本记录表
     *
     * @param taskId
     * @return
     */
    TaskHourCost selectLastTaskHourCostByTaskId(@Param("taskId") Integer taskId);

    /**
     * 根据任务id删除人本记录表的记录
     *
     * @param taskId
     */
    void deleteTaskHourCostByTaskId(Integer taskId);

    /**
     * 根据工程包id,查询工程包下所有任务,状态0,1,4
     *
     * @param workId
     * @return
     */
    int selectByWorkId(Integer workId);

    /**
     * 项目下的总任务
     */
    int countTask(Task task);

    /**
     * 根据任务信息查询任务成本表
     *
     */
    List<TaskHourCost> selectTaskHourCostByTask(TaskDto taskDto);

    /**
     * @author guodx
     * @param projectId
     * @return according to project id to sum relTaskTime of the project
     */
    Integer sumRealTaskTime(Integer projectId);

    Integer sumExpTaskTime(Integer projectId);

    /**
     * 查询任务成本消耗表的的消耗值
     *
     */
    TaskHourCost taskHourCostLists(TaskHourCost taskHourCost);

    /**
     * 项目成员统计
     * 
     * @param info
     * @return
     */
    @MapKey("key")
    Map<String, Map<String, Double>> userStat(ProjectUserInfo info);

    List<TaskHourCost> taskHourCostProjectUser();

    void deleteTaskHourByProjectId(Integer projectId);

    /**
     *
     * @param workId
     * @return
     */
    Integer selectTaskValueSumByWorkId(Integer workId);
}