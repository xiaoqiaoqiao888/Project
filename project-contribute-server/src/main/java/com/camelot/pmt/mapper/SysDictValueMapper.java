package com.camelot.pmt.mapper;

import com.camelot.pmt.model.SysDictValue;

public interface SysDictValueMapper {
    /**
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * @mbggenerated
     */
    int insert(SysDictValue record);

    /**
     * @mbggenerated
     */
    int insertSelective(SysDictValue record);

    /**
     * @mbggenerated
     */
    SysDictValue selectByPrimaryKey(Integer id);

    /**
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(SysDictValue record);

    /**
     * @mbggenerated
     */
    int updateByPrimaryKey(SysDictValue record);
}