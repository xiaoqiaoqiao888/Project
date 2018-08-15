package com.camelot.pmt.mapper;

import com.camelot.pmt.model.SysRoleResource;
import com.camelot.pmt.model.SysRoleResourceDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author gxl
 * @ClassName: SysRoleResourceMapper
 * @Description: TODO(资源 ( 菜单)权限Mapper)
 * @date 2018年5月14日 上午10:20:56
 */
public interface SysRoleResourceMapper {

    /**
     * 查询资源(菜单)权限集合
     *
     * @param sysRoleResource
     *            资源(菜单)权限对象
     * @return 资源(菜单)权限集合
     */
    List<SysRoleResource> querySysRoleResourceList(SysRoleResource sysRoleResource);

    /**
     * 查询资源(菜单)权限集合
     *
     * @param roleIds
     * @param sysRoleResource
     *            资源(菜单)权限对象
     * @return 资源(菜单)权限DTO集合
     */
    List<SysRoleResourceDTO> querySysRoleResourceDTOList(@Param("sysRoleResource") SysRoleResource sysRoleResource,
            @Param("roleIds") Long[] roleIds);

    /**
     * 新增资源(菜单)权限集合
     *
     * @param sysRoleResource
     *            资源(菜单)权限对象
     * @return int
     */
    int insertSysRoleResource(SysRoleResource sysRoleResource);

    /**
     * 批量删除资源(菜单)权限
     *
     * @param resourceIds
     *            资源Ids
     * @param roleId
     *            权限Id
     * @return int
     */
    int batchDeleteSysRoleResource(@Param("resourceIds") List<Long> resourceIds, @Param("roleId") Long roleId);
}