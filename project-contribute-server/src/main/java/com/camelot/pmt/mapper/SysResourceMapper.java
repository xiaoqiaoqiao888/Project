package com.camelot.pmt.mapper;

import com.camelot.pmt.model.SysResource;
import com.camelot.pmt.model.SysResourceDTO;
import com.camelot.pmt.model.SysResourceVo;

import java.util.List;

public interface SysResourceMapper {
    /**
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * @mbggenerated
     */
    int insert(SysResource record);

    /**
     * @mbggenerated
     */
    int insertSelective(SysResource record);

    /**
     * @mbggenerated
     */
    SysResource selectByPrimaryKey(Integer id);

    /**
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(SysResource record);

    /**
     * @mbggenerated
     */
    int updateByPrimaryKey(SysResource record);

    // 查询
    List<SysResource> selectSelective(SysResourceDTO sysResourceVO);

    // 逻辑删除
    int updateStateByPrimaryKey(Integer id);

    //树形结构展示菜单列表
    List<SysResource> treeList(SysResource sysResource);
}