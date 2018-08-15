package com.camelot.pmt.service;

import com.camelot.pmt.model.LogWork;
import com.camelot.pmt.model.Work;
import com.camelot.pmt.model.WorkCountDTO;
import com.camelot.pmt.model.WorkDTO;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author sll
 * @date 2018年5月10日
 */
public interface WorkService {
    int addWork(Work work) throws Exception;

    int updateWork(Work work) throws CloneNotSupportedException;

    int deleteWork(int[] ids);

    String workBudgetStatistics(Integer stageId);

    /**
     * 查询任务是否延期
     * 
     * @Auther: xueyj
     * @return
     */
    boolean isOverWork();

    List<LogWork> getLogWorkBy(Integer workId);

    /**
     * 分页查询功能清单列表
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
    PageInfo<Work> getWorkListByPage(Integer pageNum, Integer pageSize, Integer projectId, Integer stageId,
            Integer workState, Integer workType);

    /**
     * 分页查询职能包清单（含是否超预算）
     * 
     * @param pageNum
     * @param pageSize
     * @param workDTO
     * @author: xueyj
     * @return
     */
    PageInfo<WorkDTO> getWorkCostListByPage(Integer pageNum, Integer pageSize, WorkDTO workDTO);

    /**
     * 分页查询职能包清单（含是否超预算）
     * 
     * @param workDTO
     * @author: xueyj
     * @return
     */
    WorkCountDTO getWorkStatusCount(WorkDTO workDTO);

    /**
     * 查询功能清单list
     * 
     * @param projectId
     * @param stageId
     * @author: xueyj
     * @return
     */
    List<Work> getWorkList(Integer projectId, Integer stageId, Integer workState);

    /**
     * 根据id查询work信息
     * 
     * @param workId
     * @return
     */
    Work getWorkInfoById(Integer workId);

    /**
     * 非分页查询功能包信息
     * 
     * @param workDTO
     * @return
     */
    List<WorkDTO> getWorkCostList(WorkDTO workDTO);

    /**
     * 根据workid查询workDTO信息
     * 
     * @param workDTO
     * @return
     */
    WorkDTO getWorkCostListById(WorkDTO workDTO);
}
