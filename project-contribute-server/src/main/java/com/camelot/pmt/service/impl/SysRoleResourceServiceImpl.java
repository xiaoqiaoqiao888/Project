package com.camelot.pmt.service.impl;

import com.camelot.pmt.mapper.SysRoleResourceMapper;
import com.camelot.pmt.model.SysResource;
import com.camelot.pmt.model.SysRoleResource;
import com.camelot.pmt.model.SysRoleResourceDTO;
import com.camelot.pmt.model.SysUser;
import com.camelot.pmt.service.SysRoleResourceService;
import com.camelot.pmt.utils.Constant;
import com.camelot.pmt.utils.TokenUtil;
import com.camelot.pmt.utils.TreeUtilCommon;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author gxl
 * @ClassName: SysRoleResourceServiceImpl
 * @Description: TODO(资源 ( 菜单)权限接口)
 * @date 2018年5月14日 上午10:20:56
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class SysRoleResourceServiceImpl implements SysRoleResourceService {

    @Autowired
    private SysRoleResourceMapper sysRoleResourceMapper;

    /**
     * 查询资源(菜单)权限集合
     *
     * @param sysRoleResource 资源(菜单)权限对象
     * @return 资源(菜单)权限集合
     */
    @Override
    public PageInfo<?> querySysRoleResourceList(Integer pageNum, Integer pageSize, SysRoleResource sysRoleResource) {
        // 初始化分页信息
        PageHelper.startPage(pageNum, pageSize);
        // 查询list集合
        List<SysRoleResource> sysRoleResourceList = sysRoleResourceMapper.querySysRoleResourceList(sysRoleResource);
        PageInfo<SysRoleResource> pageResult = new PageInfo<>(sysRoleResourceList);
        return pageResult;
    }

    /**
     * 查询资源(菜单)权限集合
     *
     * @param sysRoleResource 资源(菜单)权限DTO对象
     * @return 资源(菜单)权限DTO集合
     */
    @Override
    public PageInfo<?> querySysRoleResourceDTOList(
            Integer pageNum, Integer pageSize, SysRoleResource sysRoleResource, Long[] roleIds) {
        PageInfo<SysResource> pageResult = new PageInfo<>();
        // 初始化分页信息
        PageHelper.startPage(pageNum, pageSize);
        // 查询list集合
        List<SysRoleResourceDTO> sysRoleResourceDTOList = sysRoleResourceMapper.querySysRoleResourceDTOList(
                sysRoleResource, roleIds);
        //判断DTO是否为null
        if (sysRoleResourceDTOList != null && sysRoleResourceDTOList.size() > 0) {
            List<SysResource> result = sysRoleResourceDTOList.stream()
                    .map(e -> e.getSysResource()).collect(Collectors.toList());
            try {
                result = TreeUtilCommon.buildTree(
                        result, TreeUtilCommon.SYSRESOURCE, "id", "parentId", "childList");
            } catch (Exception e) {
                log.error("树形转换异常：%s", e);
                throw new IllegalArgumentException("树形转换异常：%s", e);
            }
            pageResult.setList(result);
        }
        return pageResult;
    }

    /**
     * 批量删除资源(菜单)权限
     *
     * @param resourceIds 资源Ids
     * @param roleId      权限Id
     * @return
     */
    @Override
    public int batchDeleteSysRoleResource(List<Long> resourceIds, Long roleId) {
        return sysRoleResourceMapper.batchDeleteSysRoleResource(resourceIds, roleId);
    }

    /**
     * 新增资源(菜单)权限
     *
     * @param sysRoleResource 资源(菜单)权限对象
     */
    @Override
    public void insertSysRoleResource(SysRoleResource sysRoleResource) {
        sysRoleResourceMapper.insertSysRoleResource(sysRoleResource);
    }

    /**
     * 修改资源(菜单)权限
     *
     * @param resourceIds 资源Ids
     * @param roleId      权限Id
     * @return
     */
    @Override
    public void updateSysRoleResource(Long[] resourceIds, Long roleId) {
        // 新增、删除list
        List<Long> insertList = new ArrayList<>();
        List<Long> deleteList = new ArrayList<>();
        List<Long> resourceIdList = new ArrayList<>();
        // 构造SysRoleResource对象
        SysRoleResource sysRoleResource = new SysRoleResource();
        // 获取登录用户
        SysUser sysUser = TokenUtil.getUserFromToken();
        sysRoleResource.setRoleId(roleId.intValue());
        sysRoleResource.setCreateBy(sysUser.getId());
        sysRoleResource.setUpdateBy(sysUser.getId());
        sysRoleResource.setState(Integer.valueOf(Constant.DataStatus.EFFECTIVE));

        // 查询角色下的权限
        List<SysRoleResource> sysRoleResourceList = sysRoleResourceMapper.querySysRoleResourceList(sysRoleResource);

        // 判断需要修改菜单权限的内容
        if (resourceIds == null || resourceIds.length == 0) {
            if (sysRoleResourceList != null) {
                for (SysRoleResource sysRoleResource1 : sysRoleResourceList) {
                    deleteList.add(sysRoleResource1.getResourceId().longValue());
                }
            }
        } else {
            if (sysRoleResourceList != null && sysRoleResourceList.size() > 0) {
                // 将list<Object>转换为list<Long>格式，方便contains比较
                for (SysRoleResource sysRoleResource1 : sysRoleResourceList) {
                    resourceIdList.add(sysRoleResource1.getResourceId().longValue());
                }
                // 区分需要删除和新增的菜单权限
                for (int i = 0; i < resourceIds.length; i++) {
                    if (!resourceIdList.contains(resourceIds[i])) {
                        insertList.add(resourceIds[i]);
                    }
                }
                for (int j = 0; j < resourceIdList.size(); j++) {
                    if (!Arrays.asList(resourceIds)
                            .contains(Long.valueOf(sysRoleResourceList.get(j).getResourceId()))) {
                        deleteList.add(sysRoleResourceList.get(j).getResourceId().longValue());
                    }
                }
            } else {
                // 说明以前没有添加过权限，那么传过来的数组需要都新增
                insertList = Arrays.asList(resourceIds);
            }
        }

        // 执行新增
        for (Long param : insertList) {
            sysRoleResource.setResourceId(param.intValue());
            insertSysRoleResource(sysRoleResource);
        }
        // 执行删除
        if (deleteList != null && deleteList.size() > 0) {
            batchDeleteSysRoleResource(deleteList, roleId);
        }
    }

}
