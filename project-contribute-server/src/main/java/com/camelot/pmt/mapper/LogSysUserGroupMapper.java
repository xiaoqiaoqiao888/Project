package com.camelot.pmt.mapper;

import com.camelot.pmt.model.LogSysUserGroup;

import java.util.Map;

public interface LogSysUserGroupMapper {
    /**
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated
     */
    int insert(LogSysUserGroup record);

    /**
     *
     * @mbggenerated
     */
    int insertSelective(LogSysUserGroup record);

    /**
     *
     * @mbggenerated
     */
    LogSysUserGroup selectByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(LogSysUserGroup record);

    /**
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(LogSysUserGroup record);

    int addLogSysGroupUser(Map<String,Object> logMap);
}