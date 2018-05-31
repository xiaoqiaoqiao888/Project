package com.camelot.pmt.service.impl;

import com.camelot.pmt.mapper.SysRoleMapper;
import com.camelot.pmt.model.LogSysRole;
import com.camelot.pmt.model.SysRole;
import com.camelot.pmt.model.SysUser;
import com.camelot.pmt.service.LogSysRoleService;
import com.camelot.pmt.service.SysRoleService;
import com.camelot.pmt.service.SysUserRoleService;
import com.camelot.pmt.utils.Constant;
import com.camelot.pmt.utils.TokenUtil;
import com.github.pagehelper.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 系统角色
 *
 * @author zlh
 * @date 2018/5/10 9:18
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysRoleServiceImpl implements SysRoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Autowired
    private LogSysRoleService logSysRoleService;

    /**
     * 添加角色
     *
     * @param sysRole SysRole
     * @return boolean
     */
    @Override
    public boolean insertSysRole(SysRole sysRole) {
        if (StringUtil.isEmpty(sysRole.getRoleName())) {
            throw new IllegalArgumentException("角色名称不能为空");
        }
        SysUser user = TokenUtil.getUserFromToken();
        // 验证角色名的唯一性
        if (StringUtil.isNotEmpty(sysRole.getRoleName())) {
            List<SysRole> sysRoles = selectByRoleName(sysRole.getRoleName());
            if (!sysRoles.isEmpty()) {
                throw new IllegalArgumentException("角色名称已存在");
            }
        }
        // 角色默认状态
        if (sysRole.getState() == null) {
            sysRole.setState(Constant.DataStatus.EFFECTIVE);
        }
        sysRole.setCreatedBy(user.getId());
        sysRole.setUpdatedBy(user.getId());
        int insert = sysRoleMapper.insert(sysRole);
        // 插入日志
        if (insert == 1) {
            LogSysRole logSysRole = sysRoleCastLogSysRole(sysRole);
            logSysRole.setCreatedBy(user.getId());
            logSysRole.setUpdatedBy(user.getId());
            logSysRole.setOperateDesc(Constant.RoleLogStatus.ADD_ROLE);
            boolean insertLogResult = logSysRoleService.insertLogSysRole(logSysRole);
            return insertLogResult;
        }
        return false;
    }

    /**
     * 删除角色
     *
     * @param id id
     * @return boolean
     */
    @Override
    public boolean deleteById(Integer id) {
        SysRole sysRole = sysRoleMapper.selectById(id);
        int delete = sysRoleMapper.deleteById(id);
        sysUserRoleService.deleteByRoleOrUserIds(id, null);
        // 插入日志
        if (delete == 1) {
            LogSysRole logSysRole = sysRoleCastLogSysRole(sysRole);
            Integer userId = TokenUtil.getUserFromToken().getId();
            logSysRole.setCreatedBy(userId);
            logSysRole.setUpdatedBy(userId);
            logSysRole.setOperateDesc(Constant.RoleLogStatus.DELETE_ROLE);
            boolean insertLogResult = logSysRoleService.insertLogSysRole(logSysRole);
            return insertLogResult;
        }
        return false;
    }

    /**
     * 根据id修改角色
     *
     * @param sysRole SysRole
     * @return boolean
     */
    @Override
    public boolean updateById(SysRole sysRole) {
        SysUser user = TokenUtil.getUserFromToken();
        sysRole.setUpdatedBy(user.getId());
        int update = sysRoleMapper.updateByIdSelective(sysRole);
        // 插入日志
        if (update == 1) {
            LogSysRole logSysRole = sysRoleCastLogSysRole(sysRole);
            logSysRole.setCreatedBy(user.getId());
            logSysRole.setUpdatedBy(user.getId());
            logSysRole.setOperateDesc(Constant.RoleLogStatus.UPDATE_ROLE);
            boolean insertLogResult = logSysRoleService.insertLogSysRole(logSysRole);
            return insertLogResult;
        }
        return false;
    }

    /**
     * 根据id查看详情
     *
     * @param id
     * @return SysRole
     */
    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public SysRole selectById(Integer id) {
        return sysRoleMapper.selectById(id);
    }

    /**
     * 根据名称查询角色
     *
     * @param name
     * @return
     */
    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<SysRole> selectByRoleName(String name) {
        return sysRoleMapper.selectByRoleName(name);
    }

    /**
     * 查询所有角色
     *
     * @return SysRole
     */
    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<SysRole> selectAll() {
        return sysRoleMapper.selectAll();
    }

    /**
     * 根据用户id查看所拥有的角色
     *
     * @param id
     * @return SysRole
     */
    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<SysRole> selectRoleByUserId(Integer id) {
        return sysRoleMapper.selectRoleByUserId(id);
    }

    /**
     * 实体类转换
     *
     * @param sysRole
     * @return
     */
    private LogSysRole sysRoleCastLogSysRole(SysRole sysRole) {
        if (sysRole == null) {
            return null;
        }
        LogSysRole logSysRole = new LogSysRole();
        if (sysRole.getId() != null) {
            logSysRole.setRoleId(sysRole.getId());
        }
        if (StringUtil.isNotEmpty(sysRole.getRoleName())) {
            logSysRole.setRoleName(sysRole.getRoleName());
        }
        if (StringUtil.isNotEmpty(sysRole.getRoleDesc())) {
            logSysRole.setRoleDesc(sysRole.getRoleDesc());
        }
        if (sysRole.getState() != null) {
            logSysRole.setState(sysRole.getState());
        }
        return logSysRole;
    }
}
