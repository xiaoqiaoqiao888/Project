package com.camelot.pmt.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.camelot.pmt.mapper.LogTaskMapper;
import com.camelot.pmt.mapper.TaskMapper;
import com.camelot.pmt.mapper.WorkMapper;
import com.camelot.pmt.model.SysUser;
import com.camelot.pmt.model.SysUserTaskDTO;
import com.camelot.pmt.model.Task;
import com.camelot.pmt.model.TaskCountDTO;
import com.camelot.pmt.model.TaskDto;
import com.camelot.pmt.model.TaskHourCost;
import com.camelot.pmt.model.Work;
import com.camelot.pmt.service.BaseCalculateService;
import com.camelot.pmt.service.LogTaskService;
import com.camelot.pmt.service.ProjectUserService;
import com.camelot.pmt.service.TaskService;
import com.camelot.pmt.service.WorkService;
import com.camelot.pmt.utils.Constant;
import com.camelot.pmt.utils.Constant.Status;
import com.camelot.pmt.utils.TokenUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * @author za
 * @ClassName: TaskServiceImpl
 * @Description: TODO 任务管理service
 * @date 2018年5月14日
 */
@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    public static final String dateFormat = "yyyyMMddHHmmssSSS";
    public static final String code = "RW";
    public static final String TOTAL_TASK_NUM = "total_task_num";// 总任务
    public static final String OVER_TIME_NUM = "over_time_num";// 超时
    public static final String PRE_TIME_NUM = "pre_time_num";// 提前完成
    public static final String DOING_TASK_NUM = "doing_task_num";// 进行中任务
    public static final String DELAY_DOING_TASK_NUM = "delay_doing_task_num";// 延期进行中任务
    public static final String WAIT_FOR_TASK_NUM = "wait_for_task_num";// 待验收任务
    public static final String FINISH_TASK_NUM = "finish_task_num";// 已完成任务
    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private WorkMapper workMapper;
    @Autowired
    private BaseCalculateService baseCalculateService;
    @Autowired
    private ProjectUserService projectUserService;
    @Autowired
    private LogTaskService logTaskService;
    @Autowired
    private LogTaskMapper logTaskMapper;
    @Autowired
    private WorkService workService;

    /**
     * 拆分包添加后台数据
     *
     * @param task
     * @return
     */
    public static Task valueAdd(Task task) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern(dateFormat);
        String newsNo = code + LocalDateTime.now().format(fmt);
        task.setTaskCode(newsNo);
        task.setState(Constant.DataStatus.EFFECTIVE);
        task.setTaskState(Constant.Status.NO_START);
        long time = System.currentTimeMillis();
        task.setCreateTime(new Date(time));
        task.setUpdateTime(new Date(time));
        return task;
    }

    /**
     * 查询(任务详情,可领取任务,我的任务)通用接口
     */
    public PageInfo<Map<String, Object>> list(Integer page, Integer rows, String taskName, String projectName,
            Integer taskPersonId, Integer orderByState, Integer taskState) {
        // 初始化分页信息
        if (page != null && rows != null) {
            PageHelper.startPage(page, rows);
        }
        // 查询菜单list
        HashMap<String, Object> listMap = new HashMap<>();
        listMap.put("taskName", taskName);
        listMap.put("projectName", projectName);
        listMap.put("taskPersonId", taskPersonId);
        listMap.put("orderByState", orderByState);
        listMap.put("taskState", taskState);
        List<Map<String, Object>> taskList = taskMapper.selectTaskList(listMap);
        // pageHelper的收尾
        PageInfo<Map<String, Object>> pageResult = new PageInfo<>(taskList);
        pageResult.setList(taskList);
        return pageResult;
    }

    /**
     * (修改任务,提交任务,领取任务)通用接口
     */
    @Override
    public int updateTask(Task task) throws CloneNotSupportedException {
        if (null != task.getTaskState()) {
            // 判断是否为领取任务
            if (task.getTaskState() == 1) {
                SysUser sysUser = TokenUtil.getUserFromToken();
                task.setTaskPersonId(sysUser.getId());
                task.setRelStartTime(new Date());
                logTaskService.insertLogTask(Constant.LogStatus.PULL_TASK, task);
                projectUserService.inProject(sysUser.getId(), task.getProjectId());
            }
            // 判断是否为提交任务
            if (task.getTaskState() == 2) {
                task.setRelEndTime(new Date());
                Task task1 = taskMapper.selectByPrimaryKey(task.getId());
                // 计算实际工时
                double workHours = baseCalculateService.getWorkHours(task1.getRelStartTime(), task.getRelEndTime());
                task.setRelTaskTime(workHours);
                int updateNum = taskMapper.updateByPrimaryKeySelective(task);
                // 根据工程包id,查询工程包下所有任务,状态0,1,4
                int count = taskMapper.selectByWorkId(task1.getWorkId());
                if (count == 0) {
                    Work work = new Work();
                    work.setId(task1.getWorkId());
                    work.setWorkState(2);
                    workService.updateWork(work);
                }
                logTaskService.insertLogTask(Constant.LogStatus.COMMENT_TASK, task);
                // 更新
                return updateNum;
            }
            // 判断是否为关闭任务
            if (task.getState() == 0) {
                logTaskService.insertLogTask(Constant.LogStatus.CLOSE_TASK, task);
                isOverTask();
            }
        }
        return taskMapper.updateByPrimaryKeySelective(task);
    }

    /**
     * 添加拆分任务
     *
     * @param task
     * @return
     */
    @Override
    public boolean add(Task task) {
        task = valueAdd(task);
        Work work = workMapper.selectWorkById(task.getWorkId());
        if (work.getWorkState() == Constant.Status.COMPLETED) {
            throw new IllegalArgumentException("请求参数异常！工程包已完成不能拆分任务");
        }
        Long time = System.currentTimeMillis();
        Date date = new Date(time);
        // DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        boolean tr = work.getEndTime().after(date);
        if (tr) {
            work.setWorkState(Constant.Status.HAVE_IN_HAND);
            work.setStageId(task.getStageId());
            workMapper.updateWorkStateById(work);
        } else {
            work.setWorkState(Constant.Status.DELAY_HAVE_IN_HAND);
            work.setStageId(task.getStageId());
            workMapper.updateWorkStateById(work);
        }
        logTaskService.insertLogTask(Constant.LogStatus.SPLIT_TASK, task);
        int start = taskMapper.insertSelective(task);
        if (start == 1) {
            return true;
        }
        return false;
    }

    @Override
    public PageInfo<?> selectTaskListByPageInfo(Task task, Integer pageNum, Integer pageSize) {
        if (null == task) {
            throw new IllegalArgumentException("请求参数异常！");
        }
        // 初始化分页信息
        PageHelper.startPage(pageNum == 0 ? 1 : pageNum, pageSize == 0 ? 10 : pageSize);
        List<TaskDto> taskListItem = taskMapper.selectTasksList(task);
        // 未开始的任务
        List<TaskDto> collect0 = taskListItem.stream().filter(e -> e.getTaskState() == Constant.Status.NO_START)
                .sorted(Comparator.comparing(TaskDto::getCreateTime)).collect(Collectors.toList());
        for (TaskDto task0 : collect0) {
            task0.setUseCostRatio(new BigDecimal(0));
        }
        // 进行中的任务
        List<TaskDto> collect1 = taskListItem.stream().filter(e -> e.getTaskState() == Constant.Status.HAVE_IN_HAND)
                .sorted(Comparator.comparing(TaskDto::getUpdateTime)).collect(Collectors.toList());
        collect1 = traversalList(collect1);
        // 延期进行中的任务
        List<TaskDto> collect4 = taskListItem.stream()
                .filter(e -> e.getTaskState() == Constant.Status.DELAY_HAVE_IN_HAND)
                .sorted(Comparator.comparing(TaskDto::getUpdateTime)).collect(Collectors.toList());
        collect4 = traversalList(collect4);
        // 已完成的任务
        List<TaskDto> collect3 = taskListItem.stream().filter(e -> e.getTaskState() == Constant.Status.COMPLETED)
                .sorted(Comparator.comparing(TaskDto::getUpdateTime)).collect(Collectors.toList());
        collect3 = traversalList(collect3);
        // 待验收的任务
        List<TaskDto> collect2 = taskListItem.stream().filter(e -> e.getTaskState() == Constant.Status.WAIT_CHECK)
                .sorted(Comparator.comparing(TaskDto::getUpdateTime)).collect(Collectors.toList());
        collect2 = traversalList(collect2);
        taskListItem.clear();
        taskListItem.addAll(collect0);
        taskListItem.addAll(collect1);
        taskListItem.addAll(collect4);
        taskListItem.addAll(collect3);
        taskListItem.addAll(collect2);
        // pageHelper的收尾
        PageInfo<TaskDto> pageResult = new PageInfo<>(taskListItem);
        pageResult.setList(taskListItem);
        return pageResult;
    }

    // 计算任务消耗成本
    private List<TaskDto> traversalList(List<TaskDto> collect) {
        for (TaskDto task : collect) {
            if (task.getTaskState() == Constant.Status.HAVE_IN_HAND) {
                List<TaskHourCost> taskHourCostList = taskMapper.selectTaskHourCostByTask(task);
                if (taskHourCostList.size() == 0) {
                    task.setUseCostRatio(new BigDecimal(0));
                } else {
                    TaskHourCost taskHourCost = taskMapper.selectTaskByBudgetState(task.getId());
                    Work work = workMapper.selectByPrimaryKey(task.getWorkId());
                    BigDecimal workBudget = work.getWorkBudget();
                    BigDecimal sum = taskHourCost.getSumCost();
                    if (workBudget.compareTo(new BigDecimal(0)) == 1) {
                        BigDecimal userCostRatio;
                        userCostRatio = sum.divide(workBudget, 2, BigDecimal.ROUND_HALF_UP);
                        task.setUseCostRatio(userCostRatio);
                    } else {
                        task.setUseCostRatio(new BigDecimal(0));
                    }
                }
            } else if (task.getTaskState() == Status.DELAY_HAVE_IN_HAND) {
                TaskHourCost taskHourCost = taskMapper.selectTaskByBudgetState(task.getId());
                Work work = workMapper.selectByPrimaryKey(task.getWorkId());
                BigDecimal workBudget = work.getWorkBudget();
                BigDecimal sum = taskHourCost.getSumCost();
                if (workBudget.compareTo(new BigDecimal(0)) == 1) {
                    BigDecimal userCostRatio;
                    userCostRatio = sum.divide(workBudget, 2, BigDecimal.ROUND_HALF_UP);
                    task.setUseCostRatio(userCostRatio);
                } else {
                    task.setUseCostRatio(new BigDecimal(0));
                }
            } else if (task.getTaskState() == Status.COMPLETED || task.getTaskState() == Status.WAIT_CHECK) {
                List<TaskHourCost> taskHourCostList = taskMapper.selectTaskHourCostByTask(task);
                if (taskHourCostList.size() == 0) {
                    task.setUseCostRatio(new BigDecimal(0));
                } else {
                    TaskHourCost taskHourCost = taskMapper.selectTaskByBudgetState(task.getId());
                    Work work = workMapper.selectByPrimaryKey(task.getWorkId());
                    BigDecimal workBudget = work.getWorkBudget();
                    BigDecimal sum = taskHourCost.getSumCost();
                    if (workBudget.compareTo(new BigDecimal(0)) == 1) {
                        BigDecimal userCostRatio;
                        userCostRatio = sum.divide(workBudget, 2, BigDecimal.ROUND_HALF_UP);
                        task.setUseCostRatio(userCostRatio);
                    } else {
                        task.setUseCostRatio(new BigDecimal(0));
                    }
                }

            }
        }
        return collect;

    }

    @Override
    public TaskCountDTO selectTasklistByTaskState(Task task) {

        if (null == task) {
            throw new IllegalArgumentException("请求参数异常！");
        }
        List<TaskDto> taskList = taskMapper.selectTasksList(task);
        int count1 = 0;
        int count2 = 0;
        int count3 = 0;
        int count4 = 0;
        int count5 = 0;
        int count6 = 0;
        int count7 = 0;
        for (TaskDto taskObj : taskList) {
            if (taskObj.getTaskState() == Constant.Status.NO_START) {
                count1++;
            } else if (taskObj.getTaskState() == Constant.Status.HAVE_IN_HAND) {
                count2++;
            } else if (taskObj.getTaskState() == Constant.Status.DELAY_HAVE_IN_HAND) {
                count3++;
            } else if (taskObj.getTaskState() == Constant.Status.COMPLETED) {
                count4++;
            } else if (taskObj.getTaskState() == Constant.Status.WAIT_CHECK) {
                count5++;
            }
        }
        List<TaskDto> taskLists = this.selectTaskListNoPage(task);
        for (TaskDto taskObj : taskLists) {
            if (taskObj.getUseCostRatio().compareTo(new BigDecimal(1)) == 1) {
                count6++;
            } else {
                count7++;
            }
        }
        TaskCountDTO taskCountDTO = new TaskCountDTO();
        // 未开始
        taskCountDTO.setNoStart(count1);
        // 进行中
        taskCountDTO.setHaveInIand(count2);
        // 延期进行中
        taskCountDTO.setDelayHaveInHand(count3);
        // 已完成
        taskCountDTO.setCompleted(count4);
        // 待验收
        taskCountDTO.setWaitCheck(count5);
        // 超预算
        taskCountDTO.setOverBudget(count6);
        // 未超预算
        taskCountDTO.setOnOverBudget(count7);
        // 总任务数
        Integer countTask = taskMapper.countTask(task);
        taskCountDTO.setTotal(countTask);
        return taskCountDTO;
    }

    /**
     * 个人任务统计
     *
     * @param id
     * @return
     */
    @Override
    public Map<String, Integer> countTask(Integer id) {
        Map<String, Integer> map = new HashMap<>();
        Integer count = taskMapper.countTaskSum(id);
        Integer countOvertime = taskMapper.countOvertimeTask(id);
        map.put("count", count);
        map.put("countOvertime", countOvertime);
        return map;
    }

    /**
     * 查询个人是否有未完成的任务
     *
     * @param id
     * @return
     */
    @Override
    public int isCompleteTask(Integer id) {
        return taskMapper.isCompleteTask(id);
    }

    /**
     * 查询某个时间段内的各个任务总和
     *
     * @param taskPersonId
     * @param state
     * @param monthNum
     * @return
     */
    @Override
    public Map<String, Object> selectPeriodTotalTask(Integer taskPersonId, Integer state, Integer monthNum) {
        if (taskPersonId == null || state == null || monthNum == null) {
            return null;
        }
        // 获取某个时间段（近一个月，近三个月，近六个月，近十二个月）减去 1 3 6 12等
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, monthNum * (-1));

        List<SysUserTaskDTO> sysUserTaskDTOS = taskMapper.selectPeriodTotalTask(cal.getTime(), new Date(), taskPersonId,
                state);
        Map<String, Object> map = new HashMap<>();

        sysUserTaskDTOS.stream().forEach(e -> {
            int overCount = 0;// 超时数
            int preCount = 0;// 提前完成数
            int doingCount = 0;// 进行中任务数
            int delayCount = 0;// 延期进行中任务数
            int waitCount = 0;// 待验收任务数
            int finishedCount = 0;// 已完成任务数
            // 总任务
            map.put(TOTAL_TASK_NUM, sysUserTaskDTOS.size());
            // 超时任务
            if (e.getRelTaskTime() > e.getExpTaskTime()) {
                overCount += e.getTotalTask();
                map.put(OVER_TIME_NUM, overCount);
            } else if (e.getRelTaskTime() < e.getExpTaskTime()) {
                // 耗时<预估任务 提前完成
                preCount += e.getTotalTask();
                map.put(PRE_TIME_NUM, preCount);
            }
            // 进行中任务
            if (e.getTaskState() == Status.HAVE_IN_HAND) {
                doingCount += e.getTotalTask();
                map.put(DOING_TASK_NUM, doingCount);
            }
            // 延期进行中任务
            if (e.getTaskState() == Status.DELAY_HAVE_IN_HAND) {
                delayCount += e.getTotalTask();
                map.put(DELAY_DOING_TASK_NUM, delayCount);
            }
            // 待验收任务
            if (e.getTaskState() == Status.WAIT_CHECK) {
                waitCount += e.getTotalTask();
                map.put(WAIT_FOR_TASK_NUM, waitCount);
            }
            // 已完成任务
            if (e.getTaskState() == Status.COMPLETED) {
                finishedCount += e.getTotalTask();
                map.put(FINISH_TASK_NUM, finishedCount);
            }
        });
        return map;
    }

    /**
     * 定时查询是否有延期任务并修改状态
     */
    @Override
    public void isOverTask() {
        // 查询是否有延期进行中任务
        List<Task> overTaskList = taskMapper.isOverTask();
        // 修改成延期进行中任务
        if (overTaskList.size() != 0) {
            taskMapper.updateOverTask(overTaskList);
        }
    }

    @Override
    public PageInfo<?> selectTaskByBudgetState(Task task, Integer budgetState, Integer pageNum, Integer pageSize) {
        if (null == task) {
            throw new IllegalArgumentException("请求参数异常！");
        }
        // 初始化分页信息
        PageHelper.startPage(pageNum == 0 ? 1 : pageNum, pageSize == 0 ? 10 : pageSize);
        List<TaskDto> taskList = this.selectTaskListNoPage(task);
        if (budgetState == Constant.Status.OVER_BUDGET) {
            List<TaskDto> taskList1 = taskList.stream()
                    .filter(e -> e.getUseCostRatio().compareTo(new BigDecimal(1)) == 1).collect(Collectors.toList());
            taskList.clear();
            taskList.addAll(taskList1);
        } else if (budgetState == Constant.Status.NO_OVER_BUDGET) {
            List<TaskDto> taskList1 = taskList.stream()
                    .filter(e -> e.getUseCostRatio().compareTo(new BigDecimal(1)) == 0
                            || e.getUseCostRatio().compareTo(new BigDecimal(1)) == -1)
                    .collect(Collectors.toList());
            taskList.clear();
            taskList.addAll(taskList1);
        }
        // pageHelper的收尾
        PageInfo<TaskDto> pageResult = new PageInfo<>(taskList);
        pageResult.setList(taskList);
        return pageResult;
    }

    /**
     * 根据任务id查询任务详情
     *
     * @param id
     * @return
     */
    @Override
    public Map<String, Object> taskDetail(Integer id) {
        return taskMapper.taskDetail(id);
    }

    /**
     * 根据任务id查询日志
     *
     * @param taskId
     * @return
     */
    @Override
    public List<Map<String, Object>> logTaskByTaskId(Integer taskId) {
        return logTaskMapper.logTaskByTaskId(taskId);
    }

    @Override
    public BigDecimal selectTaskHourCostList(TaskHourCost taskHourCost) {
        if (null == taskHourCost) {
            throw new IllegalArgumentException("请求参数异常！");
        }
        BigDecimal sumCost = null;
        TaskHourCost taskHourCostObj = taskMapper.taskHourCostLists(taskHourCost);
        if (taskHourCostObj.getCount() > 0) {
            return taskHourCostObj.getSumCost();
        }
        return sumCost;
    }

    /**
     * 检测工程包下的任务是否超过工程包价值分
     * 
     * @param workId
     * @param taskId
     * @param taskValue
     * @return
     */
    @Override
    public ResponseEntity<String> checkTaskValue(Integer workId, Integer taskId, Integer taskValue) {
        Integer sumTaskValue = taskMapper.selectTaskValueSumByWorkId(workId);
        if (sumTaskValue == null) {
            sumTaskValue = 0;
        }
        Work work = workMapper.selectByPrimaryKey(workId);
        if (taskId == null) {
            if (sumTaskValue == 0) {
                sumTaskValue = taskValue;
            } else {
                sumTaskValue = sumTaskValue + taskValue;
            }
            if (sumTaskValue > work.getWorkValue()) {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("任务总价值分超出工程包价值分");
            }
            return ResponseEntity.ok("任务总价值分未超出工程包价值分");
        }
        Task task = taskMapper.selectByPrimaryKey(taskId);
        sumTaskValue = sumTaskValue - task.getTaskValue();
        sumTaskValue = sumTaskValue + taskValue;
        if (sumTaskValue > work.getWorkValue()) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("任务总价值分超出工程包价值分");
        }
        return ResponseEntity.ok("任务总价值分未超出工程包价值分");
    }

    /**
     * 不分页的任务清单的
     *
     * @param task
     * @return
     */
    public List<TaskDto> selectTaskListNoPage(Task task) {
        List<TaskDto> taskListItem = taskMapper.selectTasksList(task);
        // 未开始的任务
        List<TaskDto> collect0 = taskListItem.stream().filter(e -> e.getTaskState() == Constant.Status.NO_START)
                .sorted(Comparator.comparing(TaskDto::getCreateTime)).collect(Collectors.toList());
        for (TaskDto task0 : collect0) {
            task0.setUseCostRatio(new BigDecimal(0));
        }
        // 进行中的任务
        List<TaskDto> collect1 = taskListItem.stream().filter(e -> e.getTaskState() == Constant.Status.HAVE_IN_HAND)
                .sorted(Comparator.comparing(TaskDto::getUpdateTime)).collect(Collectors.toList());
        collect1 = traversalList(collect1);
        // 延期进行中的任务
        List<TaskDto> collect4 = taskListItem.stream()
                .filter(e -> e.getTaskState() == Constant.Status.DELAY_HAVE_IN_HAND)
                .sorted(Comparator.comparing(TaskDto::getUpdateTime)).collect(Collectors.toList());
        collect4 = traversalList(collect4);
        // 已完成的任务
        List<TaskDto> collect3 = taskListItem.stream().filter(e -> e.getTaskState() == Constant.Status.COMPLETED)
                .sorted(Comparator.comparing(TaskDto::getUpdateTime)).collect(Collectors.toList());
        collect3 = traversalList(collect3);
        // 待验收的任务
        List<TaskDto> collect2 = taskListItem.stream().filter(e -> e.getTaskState() == Constant.Status.WAIT_CHECK)
                .sorted(Comparator.comparing(TaskDto::getUpdateTime)).collect(Collectors.toList());
        collect2 = traversalList(collect2);
        taskListItem.clear();
        taskListItem.addAll(collect0);
        taskListItem.addAll(collect1);
        taskListItem.addAll(collect4);
        taskListItem.addAll(collect3);
        taskListItem.addAll(collect2);
        return taskListItem;
    }

}
