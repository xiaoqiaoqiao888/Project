package com.camelot.pmt.service;

import com.camelot.pmt.model.SysDictType;
import com.camelot.pmt.model.SysDictTypeDTO;
import com.github.pagehelper.PageInfo;

/**
 * @Author: lxk
 * @CreateDate: 2018/5/11 9:33
 * @Description: 数据字典类型service接口
 */
public interface SysDictTypeService {
    boolean add(SysDictType sysDictType);

    boolean delete(Integer id, String code);

    boolean update(SysDictType sysDictType);

    SysDictType get(Integer id);

    PageInfo<?> list(SysDictTypeDTO sysDictTypeVO);
}
