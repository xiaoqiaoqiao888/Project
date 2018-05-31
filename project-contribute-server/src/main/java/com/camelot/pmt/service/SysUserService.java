package com.camelot.pmt.service;

import com.camelot.pmt.model.SysUser;
import com.camelot.pmt.model.SysUserDTO;
import com.github.pagehelper.PageInfo;

/**
 * @author qiaodj
 * @date 2018年5月10日
 */

public interface SysUserService {
    /**
     * 个人基本信息-基本资料查询
     *
     * @param userNo
     * @return
     */
    SysUserDTO selectBaseInfo(Integer userNo);

    /**
     * 根据id查询用户
     *
     * @param id
     * @return
     */
    SysUser selectByPrimaryKey(Integer id);

    /**
     * 系统设置-员工管理-添加成员
     *
     * @param sysUser
     * @param roleId
     * @param groupId
     * @return
     */
    int insert(SysUser sysUser, Integer[] roleId, Integer groupId);

    /**
     * 根据id更新用户信息
     *
     * @param sysUser
     * @return
     */
    int updateByPrimaryKeySelective(SysUser sysUser);

    /**
     * 查询所有用户(分页)
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo<SysUser> selectAllByPage(Integer pageNum, Integer pageSize);

    /**
     * 根据用户名查询用户
     *
     * @param userName
     * @return
     */
    SysUser queryByUserName(String userName);

    /**
     * 根据角色id查看对应用户
     *
     * @param roleId
     * @return SysUser
     */
    PageInfo<SysUser> selectUsersByRoleId(Integer roleId, Integer pageNum, Integer pageSize);

    /**
     * 根据员工号查询用户
     *
     * @param userNo
     * @return
     */
    SysUser queryByUserNo(Integer userNo);


}
