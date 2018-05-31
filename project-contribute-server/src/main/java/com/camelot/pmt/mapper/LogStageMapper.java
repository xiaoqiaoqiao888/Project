package com.camelot.pmt.mapper;

import com.camelot.pmt.model.LogStage;

import java.util.List;

public interface LogStageMapper {
    /**
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated
     */
    int insert(LogStage record);

    /**
     *
     * @mbggenerated
     */
    int insertSelective(LogStage record);

    /**
     *
     * @mbggenerated
     */
    LogStage selectByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(LogStage record);

    /**
     *
     * @mbggenerated
     */
    int updateByPrimaryKeyWithBLOBs(LogStage record);

    /**
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(LogStage record);

    List<LogStage> selectStageLogByProjectId(Integer projectId);
}