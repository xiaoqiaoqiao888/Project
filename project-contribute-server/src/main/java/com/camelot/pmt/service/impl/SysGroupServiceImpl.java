package com.camelot.pmt.service.impl;

import com.camelot.pmt.mapper.LogSysGroupMapper;
import com.camelot.pmt.mapper.LogSysUserGroupMapper;
import com.camelot.pmt.mapper.SysGroupMapper;
import com.camelot.pmt.mapper.SysUserGroupMapper;
import com.camelot.pmt.model.SysGroup;
import com.camelot.pmt.model.SysGroupDTO;
import com.camelot.pmt.model.SysUser;
import com.camelot.pmt.model.SysUserDTO;
import com.camelot.pmt.model.SysUserGroup;
import com.camelot.pmt.service.SysGroupService;
import com.camelot.pmt.utils.Constant;
import com.camelot.pmt.utils.TokenUtil;
import com.camelot.pmt.utils.TreeUtilCommon;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @Author: lxk
 * @CreateDate: 2018/5/14 15:51
 * @Description: 部门组serviceImpl
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysGroupServiceImpl implements SysGroupService {
    @Autowired
    SysGroupMapper sysGroupMapper;
    @Autowired
    SysUserGroupMapper sysUserGroupMapper;
    @Autowired
    LogSysGroupMapper logSysGroupMapper;
    @Autowired
    LogSysUserGroupMapper logSysUserGroupMapper;

    private static final Logger log = LoggerFactory.getLogger(SysGroupServiceImpl.class);
    /**
     * 部门组级别
     */
    private static final int GROUPLEVEL = 4;
    /**
     * 部门组级别
     */
    private static final String DELETEWARING = "此部门组下有员工，不可删除";

    @Override
    public boolean add(SysGroup sysGroup) {
        sysGroup.setCreateBy(userId());
        sysGroup.setUpdateBy(userId());
        sysGroup.setState(Constant.DataStatus.EFFECTIVE);
        int groupLevel = sysGroup.getGroupLevel();
        // 最多添加三层部门信息
        if (groupLevel >= GROUPLEVEL) {
            throw new IllegalArgumentException(Constant.OperateDesc.MAXGROUPLEVEL);
        }
        int result = sysGroupMapper.insert(sysGroup);
        if (result != 0) {
            // 日志
            sysGroup.setCreateTime(new Date());
            sysGroup.setUpdateTime(new Date());
            saveLog(sysGroup, Constant.OperateDesc.ADD);
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(Integer id) {
        int countResult = 0;
        SysGroup sysGroup = new SysGroup();
        sysGroup = sysGroupMapper.selectByPrimaryKey(id);
        if (Objects.nonNull(sysGroup)) {
            // 判断是否是一级部门组
            boolean levelFlag = (0 == sysGroup.getParentId()) || (1 == sysGroup.getGroupLevel());
            if (levelFlag) {
                List<SysGroup> sysGroupList = sysGroupMapper.listSelectById(sysGroup);
                if (Objects.nonNull(sysGroupList) && sysGroupList.size() > 0) {
                    List<Integer> idList = new ArrayList<>();
                    for (SysGroup sysGroupTemp : sysGroupList) {
                        idList.add(sysGroupTemp.getId());
                    }
                    countResult = sysUserGroupMapper.countGroupUserByList(idList);
                }
            } else {
                // 1、判断此部门下是否有员工
                countResult = sysUserGroupMapper.countGroupUser(id);
            }
        }
        if (countResult != 0) {
            throw new IllegalArgumentException(DELETEWARING);
        }
        // 修改此部门组下所有子部门组
        sysGroup = new SysGroup();
        sysGroup.setUpdateBy(userId());
        sysGroup.setId(id);
        int result = sysGroupMapper.updateStateByPrimaryKey(sysGroup);
        if (result != 0) {
            // 日志
            saveLog(sysGroupMapper.selectByPrimaryKey(id), Constant.OperateDesc.DELETE);
            return true;
        }
        return false;
    }

    @Override
    public boolean update(SysGroup sysGroup) {
        sysGroup.setUpdateBy(userId());
        sysGroup.setState(Constant.DataStatus.EFFECTIVE);
        int result = sysGroupMapper.updateByPrimaryKeySelective(sysGroup);
        if (result != 0) {
            // 日志
            sysGroup.setUpdateTime(new Date());
            sysGroup.setCreateBy(userId());
            saveLog(sysGroup, Constant.OperateDesc.UPDATE);
            return true;
        }
        return false;
    }

    @Override
    public SysGroup get(Integer id) {
        return sysGroupMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<SysGroup> list(SysGroup sysGroup) {
        return sysGroupMapper.listSelectById(sysGroup);
    }

    /**
     * 添加部门组成员
     *
     * @param sysUserGroup
     * @return
     */
    @Override
    public boolean addSysUserGroup(SysUserGroup sysUserGroup) {
        SysUser sysUser = TokenUtil.getUserFromToken();
        if (Objects.isNull(sysUserGroup)) {
            throw new IllegalArgumentException(Constant.OperateDesc.OPERATOR_NOT_EXIST);
        }
        sysUserGroup.setCreateBy(sysUser.getId());
        sysUserGroup.setUpdateBy(sysUser.getId());
        sysUserGroup.setState(Constant.DataStatus.EFFECTIVE);
        int result = sysUserGroupMapper.insert(sysUserGroup);
        if (result != 0) {
            // 日志
            sysUserGroup.setCreateTime(new Date());
            sysUserGroup.setUpdateTime(new Date());
            saveGroupUserLog(sysUserGroup, Constant.OperateDesc.ADD);
            return true;
        }
        return false;
    }

    /**
     * 删除部门组成员
     *
     * @param id
     * @return
     */
    @Override
    public boolean deleteSysUserGroup(Integer id) {
        SysUserGroup sysUserGroup = new SysUserGroup();
        sysUserGroup.setId(id);
        sysUserGroup.setUpdateBy(userId());
        int result = sysUserGroupMapper.updateStateByPrimaryKey(sysUserGroup);
        if (result != 0) {
            // 日志
            sysUserGroup.setCreateTime(new Date());
            sysUserGroup.setUpdateTime(new Date());
            saveGroupUserLog(sysUserGroupMapper.selectByPrimaryKey(id), Constant.OperateDesc.DELETE);
            return true;
        }
        return false;
    }

    /**
     * 分页条件显示组成员列表
     *
     * @param sysUserDTO
     * @return
     */
    @Override
    public PageInfo<Map<String, Object>> listGroupUser(SysUserDTO sysUserDTO) {
        if (null == sysUserDTO) {
            throw new IllegalArgumentException(Constant.OperateDesc.REQUEST_DATA_EXCEPTION);
        }
        Integer pageNum = sysUserDTO.getPageNum();
        Integer pageSize = sysUserDTO.getPageSize();
        // 初始化分页信息
        PageHelper.startPage((pageNum == null || pageNum == 0) ? 1 : pageNum,
                (pageSize == null || pageSize == 0) ? 10 : pageSize);
        // 查询产品list
        List<Map<String, Object>> sysUserList = sysUserGroupMapper.selectGroupUserSelective(sysUserDTO);
        // pageHelper的收尾
        PageInfo<Map<String, Object>> pageResult = new PageInfo<>(sysUserList);
        pageResult.setList(sysUserList);
        return pageResult;
    }

    /**
     * 根据部门组id查询其下全部成员
     *
     * @param id
     * @return
     */
    @Override
    public List<SysUser> userListByGroupId(Integer id) {
        return sysUserGroupMapper.userListByGroupId(id);
    }

    /**
     * 树形结构展示部门组信息
     *
     * @return
     */
    @Override
    public List<SysGroupDTO> treeList() {
        List<SysGroupDTO> sysGroupTree = new ArrayList<>();
        SysGroup sysGroup = new SysGroup();
        // 查询list集合
        List<SysGroupDTO> sysGroupDTOList = sysGroupMapper.treeListSelectBySysGroup(sysGroup);
        // 判断DTO是否为null
        if (Objects.nonNull(sysGroupDTOList) && sysGroupDTOList.size() > 0) {
            try {
                sysGroupTree = TreeUtilCommon.buildTree(sysGroupDTOList, TreeUtilCommon.SYSGROUP, "id", "parentId",
                        "sysGroupDTO");
                return sysGroupTree;
            } catch (Exception e) {
                log.error("其他异常：%s", e);
                throw new IllegalArgumentException("树形转换异常：%s", e);
            }
        }
        return sysGroupTree;
    }

    /**
     * 根据部门组id查询未分配角色的成员
     *
     * @param id
     * @param roleId
     * @return
     */
    @Override
    public List<SysUser> userListNoRoleByGroupId(Integer id, Integer roleId) {
        return sysUserGroupMapper.userListNoRoleByGroupId(id, roleId);
    }

    /**
     * 根据部门组id查询未分配项目的成员
     *
     * @param groupId
     * @param projectId
     * @param realName
     * @return
     */
    @Override
    public List<SysUser> userListNoProjectByGroupId(Integer groupId, Integer projectId, String realName) {
        return sysUserGroupMapper.userListNoProjectByGroupId(groupId, projectId, realName);
    }

    /**
     * 获取操作人id
     */
    public int userId() {
        SysUser sysUser = TokenUtil.getUserFromToken();
        if (Objects.isNull(sysUser)) {
            throw new IllegalArgumentException(Constant.OperateDesc.OPERATOR_NOT_EXIST);
        }
        return sysUser.getId();
    }

    /**
     * 添加部门组日志方法
     */
    private int saveLog(SysGroup sysGroup, String operate) {
        Map<String, Object> logMap = new HashMap<>();
        logMap.put("sysGroup", sysGroup);
        StringBuilder operateDesc = new StringBuilder(TokenUtil.getUserFromToken().getRealName());
        operateDesc.append(":").append("\t").append(operate).append("\t").append(sysGroup.getGroupName());
        logMap.put("operateDesc", operateDesc.toString());
        return logSysGroupMapper.addLogSysGroup(logMap);
    }

    /**
     * 添加部门组日志方法
     */
    private int saveGroupUserLog(SysUserGroup sysUserGroup, String operate) {
        Map<String, Object> logMap = new HashMap<>();
        logMap.put("sysUserGroup", sysUserGroup);
        StringBuilder operateDesc = new StringBuilder(TokenUtil.getUserFromToken().getRealName());
        operateDesc.append(":").append("\t").append(operate).append("\t").append("成员");
        logMap.put("operateDesc", operateDesc.toString());
        return logSysUserGroupMapper.addLogSysGroupUser(logMap);
    }
}
