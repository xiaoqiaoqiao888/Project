package com.camelot.pmt.mapper;

import com.camelot.pmt.model.SysGroup;
import com.camelot.pmt.model.SysGroupDTO;

import java.util.List;

public interface SysGroupMapper {
    /**
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * @mbggenerated
     */
    int insert(SysGroup record);

    /**
     * @mbggenerated
     */
    int insertSelective(SysGroup record);

    /**
     * @mbggenerated
     */
    SysGroup selectByPrimaryKey(Integer id);

    /**
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(SysGroup record);

    /**
     * @mbggenerated
     */
    int updateByPrimaryKey(SysGroup record);

    int updateStateByPrimaryKey(SysGroup record);

    List<SysGroup> listSelectById(SysGroup sysGroup);

    List<SysGroupDTO> treeListSelectBySysGroup(SysGroup sysGroup);
}