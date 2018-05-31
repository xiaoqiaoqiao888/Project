package com.camelot.pmt.service.impl;

import com.camelot.pmt.mapper.SysGroupMapper;
import com.camelot.pmt.mapper.SysUserGroupMapper;
import com.camelot.pmt.mapper.SysUserMapper;
import com.camelot.pmt.model.SysGroup;
import com.camelot.pmt.model.SysUser;
import com.camelot.pmt.model.SysUserDTO;
import com.camelot.pmt.model.SysUserGroup;
import com.camelot.pmt.service.SysUserRoleService;
import com.camelot.pmt.service.SysUserService;
import com.camelot.pmt.utils.ChineseToEnglish;
import com.camelot.pmt.utils.Constant;
import com.camelot.pmt.utils.Constant.DataStatus;
import com.camelot.pmt.utils.TokenUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.apache.commons.lang3.StringUtils;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author qiaodj
 * @date 2018年5月10日
 */
@Service
public class SysUserServiceImpl implements SysUserService {
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysGroupMapper sysGroupMapper;
    @Autowired
    private SysUserGroupMapper sysUserGroupMapper;
    @Autowired
    private SysUserRoleService sysUserRoleService;

    /**
     * 个人基本信息-基本资料查询
     *
     * @param userNo
     * @return
     */
    @Override
    public SysUserDTO selectBaseInfo(Integer userNo) {
        if (userNo == null) {
            return null;
        }
        // 只查询有效的个人基本信息
        int effectiveValue = Constant.DataStatus.EFFECTIVE;
        SysUserDTO sysUserDTO = sysUserMapper.selectBaseInfo(userNo);
        if (sysUserDTO != null && sysUserDTO.getState() == effectiveValue) {
            SysGroup sysGroup = sysGroupMapper.selectByPrimaryKey(sysUserDTO.getParentId());
            if (sysGroup != null && sysGroup.getState() == effectiveValue) {
                sysUserDTO.setParentGroupName(sysGroup.getGroupName());
            }
            return sysUserDTO;
        }
        return null;
    }

    /**
     * 根据id查询用户
     *
     * @param id
     * @return
     */
    @Override
    public SysUser selectByPrimaryKey(Integer id) {
        if (id == null) {
            return null;
        }
        // 只查询有效的用户
        int effectiveValue = Constant.DataStatus.EFFECTIVE;
        SysUser sysUser = sysUserMapper.selectByPrimaryKey(id);
        if (sysUser != null && sysUser.getState() == effectiveValue) {
            return sysUser;
        }
        return null;
    }

    /**
     * 系统设置-员工管理-添加成员
     *
     * @param sysUser
     * @param roleId
     * @param groupId
     * @return
     */
    @Override
    @Transactional
    public int insert(SysUser sysUser, Integer[] roleId, Integer groupId) {
        if (sysUser.getUserNo() == null || StringUtils.isEmpty(sysUser.getRealName()) || StringUtils.isEmpty(sysUser
                .getEmail()) || StringUtils.isEmpty(sysUser.getTel()) || sysUser.getCost() == null || groupId == null) {
            return 0;
        }
        // BCry加盐加密
        String password = BCrypt.hashpw("123456", BCrypt.gensalt());
        String userNamePingYin = ChineseToEnglish.getFullSpell(sysUser.getRealName());
        sysUser.setPassword(password);
        sysUser.setUserName(userNamePingYin);
        sysUser.setState(DataStatus.EFFECTIVE);
        sysUser.setCreateBy(TokenUtil.getUserFromToken().getId());
        sysUser.setUpdateBy(TokenUtil.getUserFromToken().getId());
        //保存成员表
        int sysUserNum = sysUserMapper.insert(sysUser);
        if (sysUserNum > 0) {
            //保存用户组表
            SysUserGroup sysUserGroup = new SysUserGroup();
            sysUserGroup.setCreateBy(TokenUtil.getUserFromToken().getId());
            sysUserGroup.setUpdateBy(TokenUtil.getUserFromToken().getId());
            sysUserGroup.setState(Constant.DataStatus.EFFECTIVE);
            sysUserGroup.setUserId(sysUser.getId());
            sysUserGroup.setGroupId(groupId);
            int sysUserGroupNum = sysUserGroupMapper.insert(sysUserGroup);
            //保存用户角色表
            boolean updateUserRoleFlag = false;
            if (roleId != null) {
                updateUserRoleFlag = sysUserRoleService.updateUserRole(roleId, sysUser.getId());
            }
            if (sysUserGroupNum > 0 || updateUserRoleFlag) {
                return 1;
            }
        }
        return 0;
    }

    /**
     * 根据id更新用户信息
     *
     * @param sysUser
     * @return
     */
    @Override
    @Transactional
    public int updateByPrimaryKeySelective(SysUser sysUser) {
        if (sysUser.getUserNo() == null) {
            return 0;
        }
        // BCry加盐加密
        String password = BCrypt.hashpw(sysUser.getPassword(), BCrypt.gensalt());
        sysUser.setPassword(password);
        sysUser.setUpdateBy(TokenUtil.getUserFromToken().getId());
        //获取名字的拼音
        String userNamePingYin = ChineseToEnglish.getFullSpell(sysUser.getRealName());
        sysUser.setUserName(userNamePingYin);
        return sysUserMapper.updateByPrimaryKeySelective(sysUser);
    }

    /**
     * 查询所有用户(分页)
     *
     * @param pageSize
     * @param pageNum
     * @return
     */
    @Override
    public PageInfo<SysUser> selectAllByPage(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum == null || pageNum == 0 ? 1 : pageNum, pageSize == null || pageSize == 0 ? 10 :
                pageSize);
        List<SysUser> list = sysUserMapper.selectAll();
        //分页查询
        PageInfo<SysUser> pageResult = new PageInfo<>(list);
        pageResult.setList(list);
        return pageResult;
    }

    /**
     * 根据用户名查询用户（注册时可以用到）
     *
     * @param userName
     * @return
     */
    @Override
    public SysUser queryByUserName(String userName) {
        if (StringUtils.isEmpty(userName)) {
            return null;
        }
        return sysUserMapper.queryByUserName(userName);
    }

    /**
     * 根据角色id查看对应用户
     *
     * @param roleId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public PageInfo<SysUser> selectUsersByRoleId(Integer roleId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<SysUser> sysUsers = sysUserMapper.selectUsersByRoleId(roleId);
        PageInfo<SysUser> pageResult = new PageInfo<>(sysUsers);
        return pageResult;
    }

    /**
     * 根据员工号查询用户（注册时可以用到）
     *
     * @param userNo
     * @return
     */
    @Override
    public SysUser queryByUserNo(Integer userNo) {
        if (userNo == null) {
            return null;
        }
        return sysUserMapper.queryByUserNo(userNo);
    }
}
