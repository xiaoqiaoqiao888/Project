package com.camelot.pmt.mapper;

import com.camelot.pmt.model.SysUserRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysUserRoleMapper {
    /**
     * 根据用户id角色id删除用户的角色
     *
     * @param id
     * @param ids
     * @return
     */
    int deleteByRoleOrUserIds(@Param("id") Integer id, @Param("ids") Integer[] ids);

    /**
     * @mbggenerated
     */
    int roleInsertUser(List<SysUserRole> list);

    /**
     * @mbggenerated
     */
    int userInsertRole(List<SysUserRole> list);

    /**
     * 根据用户id删除多个角色
     *
     * @param userId
     * @param roleIds
     * @return boolean
     */
    int deleteByUserIdAndRoleIds(@Param("userId") Integer userId, @Param("roleIds") Integer[] roleIds);

    /**
     * @mbggenerated
     */
    List<SysUserRole> selectSysUserRoleListByUserId(Integer userId);

    /**
     * @mbggenerated
     */
    List<SysUserRole> selectSysUserRoleListByRoleIdAndUserIds(@Param("roleId") Integer roleId,
                                                              @Param("userIds") Integer[] userIds);

    /**
     * 根据角色id 查询所有角色用户对应关系
     *
     * @param roleId
     * @return
     */
    List<SysUserRole> selectSysUserRoleByRoleId(Integer roleId);
}