package com.camelot.pmt.service;

import com.camelot.pmt.model.SysGroup;
import com.camelot.pmt.model.SysGroupDTO;
import com.camelot.pmt.model.SysUser;
import com.camelot.pmt.model.SysUserDTO;
import com.camelot.pmt.model.SysUserGroup;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * @Author: lxk
 * @CreateDate: 2018/5/14 16:08
 * @Description: 部门组service接口
 */
public interface SysGroupService {
    boolean add(SysGroup sysGroup);

    boolean delete(Integer id);

    boolean update(SysGroup sysGroup);

    SysGroup get(Integer id);

    List<SysGroup> list(SysGroup sysGroup);

    boolean addSysUserGroup(SysUserGroup sysUserGroup);

    boolean deleteSysUserGroup(Integer id);

    PageInfo<Map<String, Object>> listGroupUser(SysUserDTO sysUserDTO);

    List<SysUser> userListByGroupId(Integer id);

    List<SysGroupDTO> treeList();

    List<SysUser> userListNoRoleByGroupId(Integer id, Integer roleId);

    List<SysUser> userListNoProjectByGroupId(Integer groupId, Integer projectId, String realName);
}
