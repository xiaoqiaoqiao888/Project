package com.camelot.pmt.controller;

import com.camelot.pmt.model.SysResource;
import com.camelot.pmt.model.SysResourceDTO;
import com.camelot.pmt.model.SysResourceVo;
import com.camelot.pmt.service.SysResourceService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author:myp 2018/5/11 11:35
 */
@RestController
@RequestMapping(value = "/SysResource")
@Api(value = "菜单管理接口", description = "菜单管理接口")
public class SysResourceController {

    // private static final Logger logger =
    // LoggerFactory.getLogger(SysResourceController.class);

    @Autowired
    private SysResourceService sysResourceService;

    /**
     * 添加菜单
     *
     * @param sysResource
     * @return
     */
    @PostMapping(value = "/insert")
    @ApiOperation(value = "添加菜单", notes = "添加菜单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parentId", value = "菜单父级id", paramType = "form", dataType = "string"),
            @ApiImplicitParam(name = "text", value = "资源名称", paramType = "form", dataType = "string"),
            @ApiImplicitParam(name = "href", value = "资源路径", paramType = "form", dataType = "string"),
            @ApiImplicitParam(name = "iconcls", value = "图标", paramType = "form", dataType = "string"),
            @ApiImplicitParam(name = "type", value = "资源类型", paramType = "form", dataType = "string"),
            @ApiImplicitParam(name = "permission", value = "权限标示", paramType = "form", dataType = "integer"),
            @ApiImplicitParam(name = "isShow", value = "显示标示", paramType = "form", dataType = "string"),
            @ApiImplicitParam(name = "state", value = "状态值 ", paramType = "form", dataType = "integer")})
    public ResponseEntity<String> insert(@RequestBody SysResource sysResource) {
        boolean flag = sysResourceService.insertSysResource(sysResource);
        if (flag) {
            return ResponseEntity.ok("菜单添加成功");
        }
        return ResponseEntity.ok("菜单添加失败");
    }

    @DeleteMapping(value = "/delete")
    @ApiOperation(value = "删除菜单", notes = "删除菜单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "菜单id", required = true, paramType = "query", dataType = "Integer")})
    public ResponseEntity<String> delete(Integer id) {
        boolean result = sysResourceService.deleteById(id);
        if (result) {
            return ResponseEntity.ok("删除菜单成功");
        }
        return ResponseEntity.ok("删除菜单失败");
    }

    /**
     * 编辑菜单数据
     *
     * @param sysResource 编辑菜单数据
     */
    @PostMapping("/update")
    @ApiOperation(value = "编辑菜单数据", notes = "编辑菜单数据")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "字典类型id", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "parentId", value = "菜单父级id", paramType = "form", dataType = "string"),
            @ApiImplicitParam(name = "text", value = "资源名称", paramType = "form", dataType = "string"),
            @ApiImplicitParam(name = "href", value = "资源路径", paramType = "form", dataType = "string"),
            @ApiImplicitParam(name = "iconcls", value = "图标", paramType = "form", dataType = "string"),
            @ApiImplicitParam(name = "type", value = "资源类型", paramType = "form", dataType = "string"),
            @ApiImplicitParam(name = "permission", value = "权限标示", paramType = "form", dataType = "integer"),
            @ApiImplicitParam(name = "sort_no", value = "排序", paramType = "form", dataType = "integer"),
            @ApiImplicitParam(name = "isShow", value = "显示标示", paramType = "form", dataType = "string"),
            @ApiImplicitParam(name = "state", value = "状态值 ", paramType = "form", dataType = "integer")})
    public ResponseEntity<String> update(@RequestBody SysResource sysResource) {
        boolean flag = sysResourceService.update(sysResource);
        if (flag) {
            return ResponseEntity.ok("修改成功");
        }
        return ResponseEntity.ok("修改失败");
    }

    /**
     * 查询单个菜单数据
     *
     * @param id 菜单数据id
     */
    @GetMapping("/sysResource")
    @ApiOperation(value = "查询单个菜单数据", notes = "查询单个菜单数据")
    @ApiImplicitParam(name = "id", value = "菜单id", required = true, paramType = "query", dataType = "int")
    public ResponseEntity<SysResource> get(Integer id) {
        SysResource sysResource = sysResourceService.get(id);
        return ResponseEntity.ok(sysResource);
    }

    /**
     * 分页条件查询菜单列表
     *
     * @param sysResourceVO
     */
    @PostMapping("/list")
    @ApiOperation(value = "分页条件查询菜单列表", notes = "分页条件查询菜单列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "页大小", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "text", value = "资源名称", paramType = "form", dataType = "string"),
            @ApiImplicitParam(name = "type", value = "资源类型", paramType = "form", dataType = "string")})
    public ResponseEntity<PageInfo<?>> list(@RequestBody SysResourceDTO sysResourceVO) {
        PageInfo<?> sysResourceList = sysResourceService.list(sysResourceVO);
        return ResponseEntity.ok(sysResourceList);
    }

    /**
     * 树形结构展示菜单信息
     */
    @GetMapping("/tree-list")
    @ApiOperation(value = "树形结构展示菜单信息", notes = "树形结构展示菜单信息")
    public ResponseEntity<List<SysResource>> treeList() {
        List<SysResource> sysResourceList = sysResourceService.treeList();
        return ResponseEntity.ok(sysResourceList);
    }

}
