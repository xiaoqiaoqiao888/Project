package com.camelot.pmt.service;

import java.util.List;

import com.camelot.pmt.model.SysResource;
import com.camelot.pmt.model.SysResourceDTO;
import com.github.pagehelper.PageInfo;

public interface SysResourceService {

    /**
     * 添加菜单
     *
     * @param sysResource
     * @return
     */
    boolean insertSysResource(SysResource sysResource);

    boolean deleteById(Integer id);

    PageInfo<?> list(SysResourceDTO sysResourceVO);

    SysResource get(Integer id);

    boolean update(SysResource sysResource);

    List<SysResource> treeList();
}
