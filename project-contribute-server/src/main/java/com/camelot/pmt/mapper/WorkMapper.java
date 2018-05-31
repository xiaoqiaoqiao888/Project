package com.camelot.pmt.mapper;

import com.camelot.pmt.model.Stage;
import com.camelot.pmt.model.Work;
import com.camelot.pmt.model.WorkCountDTO;
import com.camelot.pmt.model.WorkDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface WorkMapper {
    /**
     * @mbggenerated
     */
    int deleteByPrimaryKey(String ids);

    /**
     * @mbggenerated
     */
    int insert(Work record);

    /**
     * @mbggenerated
     */
    int insertSelective(Work record);

    /**
     * @mbggenerated
     */
    Work selectByPrimaryKey(Integer id);

    /**
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(Work record);

    /**
     * @mbggenerated
     */

    String workBudgetStatistics(Integer stageId);

    /**
     * 依据项目id，阶段id，查询工程包信息
     *
     * @param projectId
     * @param stageId
     * @return
     */
    List<Work> queryWorkListByParams(@Param(value = "project_id") Integer projectId,
                                     @Param(value = "stage_id") Integer stageId);

    Work queryTaskConsumeByWorkId(@Param(value = "work_id") Integer workId);

    /**
     * 查询功能包清单（符是否超预算）
     * @param workDTO
     * @Auther: xueyj
     * @return
     */
    List<WorkDTO> queryWorkCostStatusList(WorkDTO workDTO);

    /**
     * 查询工程包状态（饼状图统计）
     * @param workDTO
     * @Auther: xueyj
     * @return
     */
    WorkCountDTO queryWorkCostCount(WorkDTO workDTO);

    /**
     * 查询超期功能包信息（工程包id，结束时间，超期时间）
     * @Auther: xueyj
     * @return
     */
    List<WorkDTO> queryIsOverWork();

    /**
     * 批量更新工程包延期状态信息
     * @param workDTOList
     * @Auther: xueyj
     * @return
     */
    int updateIsOverWorkStatus(List<WorkDTO> workDTOList);
    /**
     * 批量删除
     * @Auther: sll
     * @return
     */

    int updateWorkByid(Map<String, Object> map);

    List<Work> queryWorkList(@Param(value = "projectId") Integer projectId,
                             @Param(value = "stageId") Integer stageId,
                             @Param(value = "workState") Integer workState);

    /**
     * 根据id修改工程包状态
     *
     * @param work
     */
    void updateWorkStateById(Work work);

    /**
     * 根据id查询工程包
     * @param workId
     * @return
     */
    Work selectWorkById(@Param(value = "workId") Integer workId);

    Stage getStageByid(Integer stageId);

    Work getWorkById(Integer id);


    int getBudgetaByid(Integer id);
}