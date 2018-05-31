package com.camelot.pmt.service.impl;

import com.camelot.pmt.mapper.SysDictTypeMapper;
import com.camelot.pmt.model.SysDictType;
import com.camelot.pmt.model.SysDictTypeDTO;
import com.camelot.pmt.service.SysDictTypeService;
import com.camelot.pmt.utils.Constant;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * @Author: lxk
 * @CreateDate: 2018/5/10 11:28
 * @Description: 数据字典类型service类
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysDictTypeServiceImpl implements SysDictTypeService {
    @Autowired
    SysDictTypeMapper sysDictTypeMapper;
    /**
     * 字典编码校验信息
     */
    private static final String CHECK_MESSAGE = "字典类型编码已存在！";

    /**
     * 添加字典类型
     *
     * @param sysDictType
     */
    @Override
    public boolean add(SysDictType sysDictType) throws IllegalArgumentException {
        // 查询此字典码是否已经存在
        SysDictType sysDictType1 = sysDictTypeMapper.selectByTypeCode(sysDictType.getTypeCode());
        if (Objects.nonNull(sysDictType1)) {
            // 此编码已经存在
            throw new IllegalArgumentException(CHECK_MESSAGE);
        }
        sysDictType.setState(Integer.valueOf(Constant.DataStatus.EFFECTIVE));
        sysDictTypeMapper.insert(sysDictType);
        //添加日志
        return true;
    }

    /**
     * 删除字典类型---逻辑删除
     *
     * @param id code
     */
    @Override
    public boolean delete(Integer id, String code) {
        // 修改此类型下所有字典值
        // 修改此编码下所有字典值类型
        int result = sysDictTypeMapper.updateStateByPrimaryKey(id);
        if (result != 0) {
            // 日志
            return true;
        }
        return false;
    }

    /**
     * 编辑字典类型
     *
     * @param sysDictType
     */
    @Override
    public boolean update(SysDictType sysDictType) {
        // 查询此字典码是否已经存在
        SysDictType sysDictType1 = sysDictTypeMapper.selectByTypeCode(sysDictType.getTypeCode());
        if (Objects.nonNull(sysDictType1)) {
            // 此编码已经存在
            throw new IllegalArgumentException(CHECK_MESSAGE);
        }
        sysDictTypeMapper.updateByPrimaryKeySelective(sysDictType);
        //添加日志
        return true;
    }

    /**
     * 查询字典类型
     *
     * @param id
     */
    @Override
    public SysDictType get(Integer id) {
        return sysDictTypeMapper.selectByPrimaryKey(id);
    }

    /**
     * 分页条件查询字典类型
     *
     * @param sysDictSysDictTypeDTO
     * @return
     */
    @Override
    public PageInfo<SysDictType> list(SysDictTypeDTO sysDictSysDictTypeDTO) {
        if (Objects.isNull(sysDictSysDictTypeDTO)) {
            throw new IllegalArgumentException(Constant.OperateDesc.REQUEST_DATA_EXCEPTION);
        }
        Integer pageNum = sysDictSysDictTypeDTO.getPageNum();
        Integer pageSize = sysDictSysDictTypeDTO.getPageSize();
        // 初始化分页信息
        PageHelper.startPage((Objects.isNull(pageNum) || pageNum == 0) ? 1 : pageNum,
                (Objects.isNull(pageSize) || pageSize == 0 ? 10 : pageSize));
        // 查询产品list
        List<SysDictType> sysDictTypeList = sysDictTypeMapper.selectSelective(sysDictSysDictTypeDTO);
        // pageHelper的收尾
        PageInfo<SysDictType> pageResult = new PageInfo<>(sysDictTypeList);
        pageResult.setList(sysDictTypeList);
        return pageResult;
    }
}