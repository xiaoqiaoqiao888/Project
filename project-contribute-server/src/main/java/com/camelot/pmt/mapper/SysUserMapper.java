package com.camelot.pmt.mapper;

import com.camelot.pmt.model.SysUser;
import com.camelot.pmt.model.SysUserDTO;

import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

public interface SysUserMapper {

    /**
     * 个人基本信息-基本资料查询
     *
     * @param userNo
     * @return
     */
    SysUserDTO selectBaseInfo(Integer userNo);

    /**
     * 根据id删除用户
     *
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 系统设置-员工管理-添加成员
     *
     * @param sysUser
     * @return
     */
    int insert(SysUser sysUser);

    /**
     * 根据id查询用户
     *
     * @param id
     * @return
     */
    SysUser selectByPrimaryKey(Integer id);

    /**
     * 根据id更新用户信息
     *
     * @param sysUser
     * @return
     */
    int updateByPrimaryKeySelective(SysUser sysUser);

    /**
     * 查询所有用户
     *
     * @return
     */
    List<SysUser> selectAll();

    /**
     * @mbggenerated
     */
    int updateByPrimaryKey(SysUser record);

    @Select("select * from sys_user where user_name =#{userName}")
    SysUser queryByUserName(String userName);

    @Select("select * from sys_user where user_no =#{userNo}")
    SysUser queryByUserNo(Integer userNo);

    // 依据人员id，查询人员成本信息
    @Select("SELECT COST FROM SYS_USER WHERE  ID= #{taskPersonId}")
    BigDecimal querySysUserListByParams(Integer taskPersonId);

    /**
     * 根据角色id查看对应用户
     *
     * @param id
     * @return
     */
    List<SysUser> selectUsersByRoleId(Integer id);
}