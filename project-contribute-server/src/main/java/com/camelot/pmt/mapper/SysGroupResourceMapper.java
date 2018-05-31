package com.camelot.pmt.mapper;

import com.camelot.pmt.model.SysGroupResource;

public interface SysGroupResourceMapper {
    /**
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * @mbggenerated
     */
    int insert(SysGroupResource record);

    /**
     * @mbggenerated
     */
    int insertSelective(SysGroupResource record);

    /**
     * @mbggenerated
     */
    SysGroupResource selectByPrimaryKey(Integer id);

    /**
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(SysGroupResource record);

    /**
     * @mbggenerated
     */
    int updateByPrimaryKey(SysGroupResource record);
}