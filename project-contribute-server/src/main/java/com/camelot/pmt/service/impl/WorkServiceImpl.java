package com.camelot.pmt.service.impl;

import com.camelot.pmt.mapper.LogWorkMapper;
import com.camelot.pmt.mapper.WorkMapper;
import com.camelot.pmt.model.LogWork;
import com.camelot.pmt.model.Stage;
import com.camelot.pmt.model.SysUser;
import com.camelot.pmt.model.Work;
import com.camelot.pmt.model.WorkCountDTO;
import com.camelot.pmt.model.WorkDTO;
import com.camelot.pmt.service.StageService;
import com.camelot.pmt.service.WorkService;
import com.camelot.pmt.utils.ComUtil;
import com.camelot.pmt.utils.Constant;
import com.camelot.pmt.utils.TokenUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by sll on 2018/5/14.
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WorkServiceImpl implements WorkService {

    @Autowired
    private WorkMapper workMapper;

    @Autowired
    private LogWorkMapper logWorkMapper;

    @Autowired
    private StageService stageService;

    /*
     * 拆分职能包
     */

    @Override
    public int addWork(Work work) throws Exception {
        SysUser sysUser = TokenUtil.getUserFromToken();
        work.setCreateBy(sysUser.getId());
        work.setCreateTime(new Date());
        work.setUpdateBy(sysUser.getId());
        work.setUpdateTime(new Date());
        work.setState(Constant.DataStatus.EFFECTIVE);// 有效
        work.setWorkState(Constant.Status.NO_START);
        LogWork logWork = new LogWork();
        workMapper.insert(work);
        int flage = workMapper.getBudgetaByid(work.getStageId());
        if (flage == 1) {
            throw new Exception("超预算");
        }
        // 插入日志信息
        BeanUtils.copyProperties(work, logWork);
        logWork.setWorkId(work.getId());
        logWork.setOperateDesc(Constant.WorkManager.ADD_WORK);
        logWork.setUpdateUserName(sysUser.getRealName());
        int inser1 = logWorkMapper.insertLogWork(logWork);
        // 更新阶段状态-进行中
        this.updateStageByid(work.getStageId(), Constant.Status.HAVE_IN_HAND);
        return inser1;
    }

    /*
     * 工程包修改
     */

    @Override
    public int updateWork(Work work) throws CloneNotSupportedException {

        SysUser sysUser = TokenUtil.getUserFromToken();
        work.setUpdateBy(sysUser.getId());
        work.setUpdateTime(new Date());
        if (work.getWorkState() != null && work.getWorkState() == Constant.Status.WAIT_CHECK) {
            work.setEndSubmitTime(new Date());
        }
        // 更新主表
        workMapper.updateByPrimaryKeySelective(work);
        // 插入日志信息
        LogWork logWork = new LogWork();
        BeanUtils.copyProperties(work, logWork);
        logWork.setWorkId(work.getId());
        logWork.setUpdateUserName(sysUser.getRealName());
        if (work.getWorkState() != null && work.getWorkState() == Constant.Status.HAVE_IN_HAND) {
            logWork.setOperateDesc(Constant.WorkManager.CONFIRM_WORK);
        } else if (work.getWorkState() != null && work.getWorkState() == Constant.Status.COMPLETED) {
            logWork.setOperateDesc(Constant.WorkManager.CHECK_WORK);
        } else if (work.getWorkState() != null && work.getWorkState() == Constant.Status.WAIT_CHECK) {
            logWork.setOperateDesc(Constant.WorkManager.SUBMIT_WORK);
        } else {
            logWork.setOperateDesc(Constant.WorkManager.UPDATE_WORK);
        }
        int inser1 = logWorkMapper.insertLogWork(logWork);
        // 更新阶段状态-待验收
        if (work.getWorkState() != null && (work.getWorkState() == Constant.Status.WAIT_CHECK
                || work.getWorkState() == Constant.Status.COMPLETED)) {

            this.updateStageByid(work.getId(), Constant.Status.WAIT_CHECK);
        }
        // 更新阶段状态-进行中
        if (work.getWorkState() != null && (work.getWorkState() == Constant.Status.HAVE_IN_HAND
                || work.getWorkState() == Constant.Status.DELAY_HAVE_IN_HAND)) {

            this.updateStageByid(work.getStageId(), Constant.Status.HAVE_IN_HAND);
        }
        return inser1;
    }
    /*
     * 刪除工程包
     */

    @Override
    public int deleteWork(int[] ids) {
        SysUser sysUser = TokenUtil.getUserFromToken();
        Map<String, Object> map = new HashMap<>();
        map.put("array", ids);
        map.put("updateBy", sysUser.getId());
        map.put("createBy", sysUser.getId());
        map.put("updateUserName", sysUser.getRealName());
        map.put("operateDesc", Constant.WorkManager.DELETE_WORK);
        map.put("state", Constant.DataStatus.INVALID);
        // 更新数据
        workMapper.updateWorkByid(map);
        // 插入日志信息
        int inser1 = logWorkMapper.andLogWorkBydelete(map);
        return inser1;
    }

    /*
     * 工程包预算统计
     */

    @Override
    public String workBudgetStatistics(Integer stageId) {
        return workMapper.workBudgetStatistics(stageId);
    }

    /**
     * 查询是否有超期功能包信息，并修改功能包延期状态
     * 
     * @author: xueyj
     * @return
     */
    @Override
    public boolean isOverWork() {
        List<WorkDTO> workDTOList = workMapper.queryIsOverWork();
        int updateIsOverWorkStatusTotal = workMapper.updateIsOverWorkStatus(workDTOList);
        return updateIsOverWorkStatusTotal > 0;
    }

    @Override
    public List<LogWork> getLogWorkBy(Integer workId) {
        return logWorkMapper.selectLogWork(workId);
    }

    /**
     * 依据项目id，阶段id，分页查询功能包清单信息(含验收人)
     * 
     * @param pageNum
     * @param pageSize
     * @param projectId
     * @param stageId
     * @param workState
     * @param workType
     * @author: xueyj
     * @return
     */
    @Override
    public PageInfo<Work> getWorkListByPage(Integer pageNum, Integer pageSize, Integer projectId, Integer stageId,
            Integer workState, Integer workType) {
        // 初始化分页信息
        PageHelper.startPage(pageNum, pageSize);
        // 根据项目id，阶段id，查询对应功能包基本信息
        List<Work> workList = workMapper.queryWorkListByParams(projectId, stageId);
        // pageHelper的收尾
        PageInfo<Work> pageResult = new PageInfo<>(workList);
        pageResult.setList(workList);
        return pageResult;
    }

    /**
     * 分页查询职能包清单（含是否超预算）
     * 
     * @param pageNum
     * @param pageSize
     * @param workDTO
     * @return
     */
    @Override
    public PageInfo<WorkDTO> getWorkCostListByPage(Integer pageNum, Integer pageSize, WorkDTO workDTO) {
        // 初始化分页信息
        PageHelper.startPage(pageNum, pageSize);
        List<WorkDTO> workDTOList = workMapper.queryWorkCostStatusList(workDTO);
        // 未开始列表（按照创建时间排序）
        List<WorkDTO> noStartCollect = workDTOList.stream().filter(e -> e.getWorkState() == 0)
                .sorted(Comparator.comparing(WorkDTO::getCreateTime)).collect(Collectors.toList());
        // 进行中列表（按照确认时间，即修改时间排序）
        List<WorkDTO> haveInHandCollect = workDTOList.stream().filter(e -> e.getWorkState() == 1)
                .sorted(Comparator.comparing(WorkDTO::getUpdateTime)).collect(Collectors.toList());
        // 延期进行中（按照确认时间，即修改时间排序）
        List<WorkDTO> delayHaveInHandCollect = workDTOList.stream().filter(e -> e.getWorkState() == 4)
                .sorted(Comparator.comparing(WorkDTO::getUpdateTime)).collect(Collectors.toList());
        // 待验收（按照确认时间，即修改时间排序）
        List<WorkDTO> waitForAcceptanceCollect = workDTOList.stream().filter(e -> e.getWorkState() == 2)
                .sorted(Comparator.comparing(WorkDTO::getUpdateTime)).collect(Collectors.toList());
        // 已完成（按照确认时间，即修改时间排序）
        List<WorkDTO> completedCollect = workDTOList.stream().filter(e -> e.getWorkState() == 3)
                .sorted(Comparator.comparing(WorkDTO::getUpdateTime)).collect(Collectors.toList());
        workDTOList.clear();
        workDTOList.addAll(noStartCollect);
        workDTOList.addAll(haveInHandCollect);
        workDTOList.addAll(delayHaveInHandCollect);
        workDTOList.addAll(waitForAcceptanceCollect);
        workDTOList.addAll(completedCollect);
        // pageHelper的收尾
        PageInfo<WorkDTO> pageResult = new PageInfo<>(workDTOList);
        pageResult.setList(workDTOList);
        return pageResult;
    }

    /**
     * 查询职能包状态统计
     * 
     * @param workDTO
     * @author: xueyj
     * @return
     */
    @Override
    public WorkCountDTO getWorkStatusCount(WorkDTO workDTO) {
        WorkCountDTO workCountDTO = workMapper.queryWorkCostCount(workDTO);
        return workCountDTO;
    }

    /**
     * 查询功能清单list
     * 
     * @param projectId
     * @param stageId
     * @return
     */
    @Override
    public List<Work> getWorkList(Integer projectId, Integer stageId, Integer workState) {
        List<Work> workList = workMapper.queryWorkList(projectId, stageId, workState);
        return workList;
    }

    /**
     * 依据id查询工程包信息
     * 
     * @param workId
     * @return
     */
    @Override
    public Work getWorkInfoById(Integer workId) {
        Work work = workMapper.selectByPrimaryKey(workId);
        return work;
    }

    /**
     * 非分页查询功能包信息
     * 
     * @param workDTO
     * @return
     */
    @Override
    public List<WorkDTO> getWorkCostList(WorkDTO workDTO) {
        List<WorkDTO> workDTOList = workMapper.queryWorkCostStatusList(workDTO);
        return workDTOList;
    }

    /**
     * 根据workid查询workDTO信息
     * 
     * @param workDTO
     * @return
     */
    @Override
    public WorkDTO getWorkCostListById(WorkDTO workDTO) {
        List<WorkDTO> workDTOList = workMapper.queryWorkCostStatusList(workDTO);
        WorkDTO workDTOInfo = null;
        if (workDTOList.size() > 0) {
            workDTOInfo = workDTOList.get(0);
        }
        return workDTOInfo;
    }

    public void updateStageByid(int id, int flage) throws CloneNotSupportedException {
        if (flage == Constant.Status.HAVE_IN_HAND) {
            Stage stage1 = new Stage();
            Stage stage = workMapper.getStageByid(id);
            if (!ComUtil.isEmpty(stage)) {
                stage1.setId(stage.getId());
                if (stage.getState() == 1) {
                    stage1.setStageState(Constant.Status.DELAY_HAVE_IN_HAND);
                    stageService.updateStage(stage1);
                } else {
                    stage1.setStageState(Constant.Status.HAVE_IN_HAND);
                    stageService.updateStage(stage1);
                }
            }
        } else if (flage == Constant.Status.WAIT_CHECK) {
            Work work1 = workMapper.getWorkById(id);
            if (work1.getState() == 0) {
                Stage stage = new Stage();
                stage.setId(work1.getStageId());
                stage.setStageState(Constant.Status.WAIT_CHECK);
                stageService.updateStage(stage);
            }
        }
    }
}
