package com.camelot.pmt.mapper;

import com.camelot.pmt.model.SysRole;

import java.util.List;

public interface SysRoleMapper {
    /**
     * 添加角色
     *
     * @param sysRole SysRole
     * @return int
     */
    int insert(SysRole sysRole);

    /**
     * 根据id删除角色
     *
     * @param id id
     * @return int
     */
    int deleteById(Integer id);

    /**
     * 根据id修改角色
     *
     * @param sysRole SysRole
     * @return int
     */
    int updateByIdSelective(SysRole sysRole);

    /**
     * 根据id查看角色详情
     *
     * @param id id
     * @return SysRole
     */
    SysRole selectById(Integer id);

    /**
     * 查看所有角色
     *
     * @return SysRole
     */
    List<SysRole> selectAll();

    /**
     * 根据用户id查看所拥有的角色
     *
     * @param id
     * @return SysRole
     */
    List<SysRole> selectRoleByUserId(Integer id);

    /**
     * 根据名称查询角色
     *
     * @param name
     * @return
     */
    List<SysRole> selectByRoleName(String name);
}