package com.camelot.pmt.service;

import com.camelot.pmt.model.SysUserRole;

import java.util.List;

/**
 * @author zsf
 * @date 2018/5/14 17:26
 */
public interface SysUserRoleService {

    boolean deleteByRoleOrUserIds(Integer id, Integer[] ids);

    boolean roleInsertUser(Integer roleId, Integer[] userIds);

    boolean deleteByUserIdAndRoleIds(Integer userId, Integer[] roleIds);

    boolean userInsertRole(Integer[] roleIds, Integer userId);

    boolean updateUserRole(Integer[] roleIds, Integer userId);

    List<SysUserRole> selectSysUserRoleListByUserId(Integer userId);

    List<SysUserRole> selectSysUserRoleListByRoleIdAndUserIds(Integer roleId, Integer[] userIds);

    List<SysUserRole> selectSysUserRoleByRoleId(Integer roleId);
}
