package com.camelot.pmt.controller;

import com.camelot.pmt.model.SysDictType;
import com.camelot.pmt.model.SysDictTypeDTO;
import com.camelot.pmt.model.SysUser;
import com.camelot.pmt.service.SysDictTypeService;
import com.camelot.pmt.utils.Constant;
import com.camelot.pmt.utils.TokenUtil;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * @Author: lxk
 * @CreateDate: 2018/5/9 16:49
 * @Description: 系统数据字典类型controller
 */
@RestController
@RequestMapping("/sys-dict-type")
@Api(value = "数据字典类型", description = "数据字典类型")
public class SysDictTypeController {

    @Autowired
    SysDictTypeService sysDictTypeService;

    final String paramCheck = "请求参数异常，字典码或字典名不能为空！";

    /**
     * 添加字典类型
     *
     * @param sysDictType
     *            字典类型实体
     */
    @PostMapping(value = "")
    @ApiOperation(value = "添加字典类型", notes = "添加字典类型")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "typeCode", value = "字典类型编码", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "typeName", value = "字典类型名称", paramType = "query", dataType = "String") })
    public ResponseEntity<String> save(@RequestBody SysDictType sysDictType) {
        SysUser sysUser = TokenUtil.getUserFromToken();
        if (Objects.isNull(sysUser)) {
            throw new IllegalArgumentException(Constant.OperateDesc.OPERATOR_NOT_EXIST);
        }
        boolean checkFlag = (Objects.isNull(sysDictType)) || Objects.isNull(sysDictType.getTypeCode())
                || (StringUtils.isEmpty(sysDictType.getTypeCode())) || Objects.isNull(sysDictType.getTypeName())
                || (StringUtils.isEmpty(sysDictType.getTypeName()));
        if (checkFlag) {
            throw new IllegalArgumentException(paramCheck);
        }
        sysDictType.setCreateBy(sysUser.getId());
        sysDictType.setUpdateBy(sysUser.getId());
        boolean flag = sysDictTypeService.add(sysDictType);
        if (flag) {
            return ResponseEntity.ok(Constant.OperateDesc.ADD + Constant.OperateDesc.SUCCESS);
        }
        return ResponseEntity.ok(Constant.OperateDesc.ADD + Constant.OperateDesc.FAIL);
    }

    /**
     * 删除字典类型
     *
     * @param id
     *            code 字典类型id 编码
     */
    @DeleteMapping(value = "")
    @ApiOperation(value = "删除字典类型", notes = "删除字典类型")
    @ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "字典类型id", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "code", value = "字典类型编码", paramType = "query", dataType = "String") })
    public ResponseEntity<String> delete(Integer id, String code) {
        boolean flag = sysDictTypeService.delete(id, code);
        if (flag) {
            return ResponseEntity.ok(Constant.OperateDesc.DELETE + Constant.OperateDesc.SUCCESS);
        }
        return ResponseEntity.ok(Constant.OperateDesc.DELETE + Constant.OperateDesc.FAIL);
    }

    /**
     * 编辑字典类型
     *
     * @param sysDictType
     *            字典类型实体
     */
    @PostMapping("/update")
    @ApiOperation(value = "编辑字典类型", notes = "编辑字典类型")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "字典类型id", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "typeCode", value = "字典类型编码", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "typeName", value = "字典类型名称", paramType = "query", dataType = "String") })
    public ResponseEntity<String> update(@RequestBody SysDictType sysDictType) {
        SysUser sysUser = TokenUtil.getUserFromToken();
        if (Objects.isNull(sysUser)) {
            throw new IllegalArgumentException(Constant.OperateDesc.OPERATOR_NOT_EXIST);
        }
        boolean checkFlag = (Objects.isNull(sysDictType)) || Objects.isNull(sysDictType.getTypeCode())
                || (StringUtils.isEmpty(sysDictType.getTypeCode())) || Objects.isNull(sysDictType.getTypeName())
                || (StringUtils.isEmpty(sysDictType.getTypeName()));
        if (checkFlag) {
            throw new IllegalArgumentException(paramCheck);
        }
        sysDictType.setUpdateBy(sysUser.getId());
        boolean flag = sysDictTypeService.update(sysDictType);
        if (flag) {
            return ResponseEntity.ok(Constant.OperateDesc.UPDATE + Constant.OperateDesc.SUCCESS);
        }
        return ResponseEntity.ok(Constant.OperateDesc.UPDATE + Constant.OperateDesc.FAIL);
    }

    /**
     * 查询单个字典类型
     *
     * @param id
     *            字典类型id
     */
    @GetMapping("/dict-type")
    @ApiOperation(value = "查询单个字典类型", notes = "查询单个字典类型")
    @ApiImplicitParam(name = "id", value = "字典类型id", paramType = "query", dataType = "int")
    public ResponseEntity<SysDictType> get(Integer id) {
        SysDictType sysDictType = sysDictTypeService.get(id);
        return ResponseEntity.ok(sysDictType);
    }

    /**
     * 分页条件查询字典类型编码
     *
     * @param sysDictTypeVO
     *            字典业务实体
     */
    @PostMapping("/list")
    @ApiOperation(value = "分页条件查询字典类型列表", notes = "分页条件查询字典类型列表")
    @ApiImplicitParams({ @ApiImplicitParam(name = "pageNum", value = "页码", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "页大小", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "typeCode", value = "字典类型编码", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "typeName", value = "字典类型名称", paramType = "query", dataType = "String") })
    public ResponseEntity<PageInfo<?>> list(@RequestBody SysDictTypeDTO sysDictTypeVO) {
        PageInfo<?> sysDictTypeList = sysDictTypeService.list(sysDictTypeVO);
        return ResponseEntity.ok(sysDictTypeList);
    }

}
