package com.camelot.pmt.mapper;

import com.camelot.pmt.model.LogTask;
import com.camelot.pmt.model.LogTaskDTO;
import com.camelot.pmt.model.LogTaskKey;

import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface LogTaskMapper {
    int deleteByPrimaryKey(LogTaskKey key);

    int insert(LogTask record);

    int insertSelective(LogTask record);

    LogTask selectByPrimaryKey(LogTaskKey key);

    int updateByPrimaryKeySelective(LogTask record);

    int updateByPrimaryKeyWithBLOBs(LogTask record);

    int updateByPrimaryKey(LogTask record);

    List<Map<String, Object>> logTaskByTaskId(Integer taskId);

    /**
     * 根据任务领取人id查询任务操作记录
     *
     * @param state
     * @param taskPersonId
     * @param beginTime
     * @param endTime
     * @return
     */
    List<LogTaskDTO> selectByTaskPersonId(@Param("state") Integer state, @Param("taskPersonId") Integer taskPersonId,
                                          @Param("beginTime") Date beginTime, @Param("endTime") Date endTime);
}