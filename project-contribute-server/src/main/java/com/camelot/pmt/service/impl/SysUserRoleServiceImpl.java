package com.camelot.pmt.service.impl;

import com.camelot.pmt.mapper.SysUserRoleMapper;
import com.camelot.pmt.model.LogSysUserRole;
import com.camelot.pmt.model.SysUserRole;
import com.camelot.pmt.service.LogSysUserRoleService;
import com.camelot.pmt.service.SysUserRoleService;
import com.camelot.pmt.utils.Constant;
import com.camelot.pmt.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zsf
 * @date 2018/5/14 17:27
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysUserRoleServiceImpl implements SysUserRoleService {

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Autowired
    private LogSysUserRoleService logSysUserRoleService;

    /**
     * 根据用户id角色id删除用户的角色
     *
     * @param id
     * @param ids
     * @return
     */
    @Override
    public boolean deleteByRoleOrUserIds(Integer id, Integer[] ids) {
        List<SysUserRole> sysUserRoleList;
        if (ids == null) {
            sysUserRoleList = sysUserRoleMapper.selectSysUserRoleByRoleId(id);
        } else {
            sysUserRoleList = sysUserRoleService.selectSysUserRoleListByRoleIdAndUserIds(id, ids);
        }
        int deleteResult = sysUserRoleMapper.deleteByRoleOrUserIds(id, ids);
        if (deleteResult >= 1) {
            // 添加日志
            List<LogSysUserRole> logSysUserRoleList = new ArrayList<>();
            if (sysUserRoleList != null & sysUserRoleList.size() != 0) {
                for (int i = 0; i < sysUserRoleList.size(); i++) {
                    LogSysUserRole logSysUserRole = sysUserRoleToLogSysUserRole(sysUserRoleList.get(i));
                    logSysUserRole.setOperateDesc(Constant.UserRoleLogStatus.Role_DELETE_User);
                    logSysUserRoleList.add(logSysUserRole);
                }
            }
            logSysUserRoleService.insertLogUserRole(logSysUserRoleList);
            return true;
        }
        return false;
    }

    /**
     * 根据角色ID 批量新增用户
     *
     * @param roleId
     *            , userIds , user
     * @return boolean
     */
    @Override
    public boolean roleInsertUser(Integer roleId, Integer[] userIds) {
        Integer loginUserId = TokenUtil.getUserFromToken().getId();
        Integer state = Constant.DataStatus.EFFECTIVE;
        List<SysUserRole> sysUserRoleList = new ArrayList<>();
        if (userIds != null && userIds.length != 0) {
            for (int i = 0; i < userIds.length; i++) {
                SysUserRole sysUserRole = new SysUserRole();
                sysUserRole.setRoleId(roleId);
                sysUserRole.setUserId(userIds[i]);
                sysUserRole.setCreateBy(loginUserId);
                sysUserRole.setUpdateBy(loginUserId);
                sysUserRole.setState(state);
                sysUserRoleList.add(sysUserRole);
            }
        }
        int insert = sysUserRoleMapper.roleInsertUser(sysUserRoleList);
        if (insert >= 1) {
            // 添加日志
            List<LogSysUserRole> logSysUserRoleList = new ArrayList<>();
            if (sysUserRoleList != null & sysUserRoleList.size() != 0) {
                for (int i = 0; i < sysUserRoleList.size(); i++) {
                    LogSysUserRole logSysUserRole = sysUserRoleToLogSysUserRole(sysUserRoleList.get(i));
                    logSysUserRole.setOperateDesc(Constant.UserRoleLogStatus.Role_Add_User);
                    logSysUserRoleList.add(logSysUserRole);
                }
            }
            logSysUserRoleService.insertLogUserRole(logSysUserRoleList);
            return true;
        }
        return false;
    }

    /**
     * 根据用户ID 批量新增角色
     *
     * @param roleIds
     *            , userId , user
     * @return boolean
     */
    @Override
    public boolean userInsertRole(Integer[] roleIds, Integer userId) {

        Integer loginUserId = TokenUtil.getUserFromToken().getId();
        Integer state = Constant.DataStatus.EFFECTIVE;
        List<SysUserRole> sysUserRoleList = new ArrayList<>();
        if (roleIds != null && roleIds.length != 0) {
            for (int i = 0; i < roleIds.length; i++) {
                SysUserRole sysUserRole = new SysUserRole();
                sysUserRole.setRoleId(roleIds[i]);
                sysUserRole.setUserId(userId);
                sysUserRole.setCreateBy(loginUserId);
                sysUserRole.setUpdateBy(loginUserId);
                sysUserRole.setState(state);
                sysUserRoleList.add(sysUserRole);
            }
        }
        int insert = sysUserRoleMapper.userInsertRole(sysUserRoleList);
        if (insert >= 1) {
            // 添加日志
            List<LogSysUserRole> logSysUserRoleList = new ArrayList<>();
            if (sysUserRoleList != null && sysUserRoleList.size() != 0) {
                for (int i = 0; i < sysUserRoleList.size(); i++) {
                    LogSysUserRole logSysUserRole = sysUserRoleToLogSysUserRole(sysUserRoleList.get(i));
                    logSysUserRole.setOperateDesc(Constant.UserRoleLogStatus.User_ADD_ROLE);
                    logSysUserRoleList.add(logSysUserRole);
                }
            }
            logSysUserRoleService.insertLogUserRole(logSysUserRoleList);
            return true;
        }
        return false;
    }

    /**
     * 根据用户ID 赋予角色
     *
     * @param roleIds
     *            , userId , user
     * @return boolean
     */
    @Override
    public boolean updateUserRole(Integer[] roleIds, Integer userId) {
        sysUserRoleService.deleteByUserIdAndRoleIds(userId, null);
        if (roleIds != null && roleIds.length != 0) {
            sysUserRoleService.userInsertRole(roleIds, userId);
        }
        return true;
    }

    /**
     * 根据用户ID 查询对应关系
     *
     * @param userId
     * @return List
     */
    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<SysUserRole> selectSysUserRoleListByUserId(Integer userId) {
        return sysUserRoleMapper.selectSysUserRoleListByUserId(userId);
    }

    /**
     * 根据角色ID 用户IDS 查询对应关系
     *
     * @param roleId
     * @param userIds
     * @return List
     */
    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<SysUserRole> selectSysUserRoleListByRoleIdAndUserIds(Integer roleId, Integer[] userIds) {
        return sysUserRoleMapper.selectSysUserRoleListByRoleIdAndUserIds(roleId, userIds);
    }

    /**
     * 根据用户id删除多个角色
     *
     * @param userId
     * @param roleIds
     * @return boolean
     */
    @Override
    public boolean deleteByUserIdAndRoleIds(Integer userId, Integer[] roleIds) {
        List<SysUserRole> sysUserRoles = sysUserRoleService.selectSysUserRoleListByUserId(userId);
        int deleteResult = sysUserRoleMapper.deleteByUserIdAndRoleIds(userId, roleIds);
        if (deleteResult >= 1) {
            // 添加日志
            List<LogSysUserRole> logSysUserRoleList = new ArrayList<>();
            if (sysUserRoles != null && sysUserRoles.size() != 0) {
                for (int i = 0; i < sysUserRoles.size(); i++) {
                    LogSysUserRole logSysUserRole = sysUserRoleToLogSysUserRole(sysUserRoles.get(i));
                    logSysUserRole.setOperateDesc(Constant.UserRoleLogStatus.User_DELETE_ROLE);
                    logSysUserRoleList.add(logSysUserRole);
                }
            }
            logSysUserRoleService.insertLogUserRole(logSysUserRoleList);
            return true;
        }
        return false;
    }

    /**
     * 实体类转换
     *
     * @param sysUserRole
     * @return
     */
    private LogSysUserRole sysUserRoleToLogSysUserRole(SysUserRole sysUserRole) {
        if (sysUserRole == null) {
            return null;
        }
        LogSysUserRole logSysUserRole = new LogSysUserRole();
        if (sysUserRole.getId() != null) {
            logSysUserRole.setUserRoleId(sysUserRole.getId());
        }
        if (sysUserRole.getRoleId() != null) {
            logSysUserRole.setRoleId(sysUserRole.getRoleId());
        }
        if (sysUserRole.getUserId() != null) {
            logSysUserRole.setUserId(sysUserRole.getUserId());
        }
        if (sysUserRole.getState() != null) {
            logSysUserRole.setState(sysUserRole.getState());
        }
        if (sysUserRole.getCreateBy() != null) {
            logSysUserRole.setCreateBy(sysUserRole.getCreateBy());
        }
        if (sysUserRole.getUpdateBy() != null) {
            logSysUserRole.setUpdateBy(sysUserRole.getUpdateBy());
        }
        return logSysUserRole;
    }

    /**
     * 根据角色id 查询所有角色用户对应关系
     *
     * @param roleId
     * @return
     */
    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<SysUserRole> selectSysUserRoleByRoleId(Integer roleId) {
        return sysUserRoleMapper.selectSysUserRoleByRoleId(roleId);
    }

}
