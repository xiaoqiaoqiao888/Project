package com.camelot.pmt.service;

import com.camelot.pmt.model.SysRoleResource;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author gxl
 * @ClassName: SysRoleResourceService
 * @Description: TODO(资源 ( 菜单)权限接口)
 * @date 2018年5月14日 上午10:20:56
 */
public interface SysRoleResourceService {

    /**
     * 查询资源(菜单)权限集合
     *
     * @param sysRoleResource
     *            资源(菜单)权限对象
     * @param pageNum
     * @param pageSize
     * @return 资源(菜单)权限集合
     */
    PageInfo<?> querySysRoleResourceList(Integer pageNum, Integer pageSize, SysRoleResource sysRoleResource);

    /**
     * 查询资源(菜单)权限集合
     *
     * @param sysRoleResource
     *            资源(菜单)权限DTO对象
     * @param pageNum
     * @param pageSize
     * @param roleIds
     * @return 资源(菜单)权限DTO对象
     */
    PageInfo<?> querySysRoleResourceDTOList(Integer pageNum, Integer pageSize, SysRoleResource sysRoleResource,
            Long[] roleIds);

    /**
     * 批量删除资源(菜单)权限
     *
     * @param resourceIds
     *            资源Ids
     * @param roleId
     *            权限Id
     * @return
     */
    int batchDeleteSysRoleResource(List<Long> resourceIds, Long roleId);

    /**
     * 新增系统登录日志
     *
     * @param sysRoleResource
     *            访问日志对象
     */
    void insertSysRoleResource(SysRoleResource sysRoleResource);

    /**
     * 修改资源(菜单)权限
     *
     * @param resourceIds
     *            资源Ids
     * @param roleId
     *            权限Id
     * @return
     */
    void updateSysRoleResource(Long[] resourceIds, Long roleId);

}
