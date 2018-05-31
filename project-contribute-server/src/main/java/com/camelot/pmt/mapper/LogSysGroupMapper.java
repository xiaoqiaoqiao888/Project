package com.camelot.pmt.mapper;

import com.camelot.pmt.model.LogSysGroup;

import java.util.Map;

public interface LogSysGroupMapper {
    /**
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated
     */
    int insert(LogSysGroup record);

    /**
     *
     * @mbggenerated
     */
    int insertSelective(LogSysGroup record);

    /**
     *
     * @mbggenerated
     */
    LogSysGroup selectByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(LogSysGroup record);

    /**
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(LogSysGroup record);

    int addLogSysGroup(Map<String,Object> logMap);

    int addTaskLog(Map<String, Object> map);

    int addWorkLog(Map<String, Object> map);

    int addStageLog(Map<String, Object> map);
}