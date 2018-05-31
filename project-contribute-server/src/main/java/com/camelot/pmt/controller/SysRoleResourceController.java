package com.camelot.pmt.controller;

import com.camelot.pmt.model.SysRoleResource;
import com.camelot.pmt.service.SysRoleResourceService;
import com.camelot.pmt.utils.Constant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @author gxl
 * @ClassName: SysRoleResourceController
 * @Description: TODO(资源 ( 菜单)权限Controller层)
 */

@Api(description = "资源(菜单)权限")
@RestController
@RequestMapping("/sys/role-resource")
public class SysRoleResourceController {

    @Autowired
    private SysRoleResourceService sysRoleResourceService;

    /**
     * @return ResponseEntity 返回类型
     * @Description: TODO(查询资源(菜单)权限DTO)
     */
    @GetMapping("")
    @ApiOperation(value = "查询资源(菜单)权限DTO,包括关联对象的其他信息", notes = "查询资源(菜单)权限DTO,包括关联对象的其他信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码",
                    paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页数量",
                    paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "id", value = "资源(菜单)权限Id",
                    paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "resourceId", value = "资源Id",
                    paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "roleIds", value = "角色Ids", required = true,
                    paramType = "query", dataType = "Integer") })
    public ResponseEntity<?> queryDTOList(
            @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize,
            @ApiIgnore SysRoleResource sysRoleResource,
            Long[] roleIds) {
        if (sysRoleResource == null || roleIds == null || roleIds.length == 0) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(Constant.Manager.REQUEST_DATA_EXCEPTION);
        }
        return ResponseEntity.ok(
                sysRoleResourceService.querySysRoleResourceDTOList(pageNum, pageSize, sysRoleResource,roleIds));
    }

    /**
     * 更新资源(菜单)权限) 注：resourceIds没值代表清除本角色所有权限，如果有值则修改本角色权限为所传值
     *
     * @return ResponseEntity 返回类型
     */
    @PostMapping("")
    @ApiOperation(value = "更新资源(菜单)权限", notes = "更新资源(菜单)权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "resourceIds", value = "资源ids",
                    paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "roleId", value = "角色id", required = true,
                    paramType = "query", dataType = "Long")})
    public ResponseEntity<String> update(Long[] resourceIds, Long roleId) {
        if (roleId == null) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(Constant.Manager.REQUEST_DATA_EXCEPTION);
        }
        sysRoleResourceService.updateSysRoleResource(resourceIds, roleId);
        return ResponseEntity.ok(Constant.Manager.UPDATE_SUCCESS);
    }
}