package com.camelot.pmt.service.impl;

import com.camelot.pmt.mapper.SysResourceMapper;
import com.camelot.pmt.model.SysResource;
import com.camelot.pmt.model.SysResourceDTO;
import com.camelot.pmt.model.SysResourceVo;
import com.camelot.pmt.model.SysUser;
import com.camelot.pmt.service.SysResourceService;
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
import java.util.List;

/**
 * @Author:myp 2018/5/11 11:38
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysResourceServiceImpl implements SysResourceService {

    private static final Logger log = LoggerFactory.getLogger(SysResourceServiceImpl.class);

    @Autowired
    private SysResourceMapper sysResourceMapper;

    /**
     * 添加菜单
     *
     * @param sysResource
     */
    @Override
    public boolean insertSysResource(SysResource sysResource) {
        boolean flag = false;
        SysUser user = TokenUtil.getUserFromToken();
        sysResource.setCreateBy(user.getId());
        sysResource.setUpdateBy(user.getId());
        sysResource.setState(1);
        sysResource.setCreateTime(new Date());
        sysResource.setUpdateTime(new Date());
        int resultCount = sysResourceMapper.insert(sysResource);
        if (resultCount > 0) {
            flag = true;
        }
        return flag;
    }

    /**
     * 删除指定菜单
     *
     * @param id
     * @return
     */
    @Override
    public boolean deleteById(Integer id) {
        int result = sysResourceMapper.updateStateByPrimaryKey(id);
        if (result > 0) {
            return true;
        }
        return false;
    }

    /**
     * 编辑菜单
     *
     * @param sysResource
     */
    @Override
    public boolean update(SysResource sysResource) {
        SysUser user = TokenUtil.getUserFromToken();
        sysResource.setUpdateBy(user.getId());
        sysResource.setUpdateTime(new Date());
        int result = sysResourceMapper.updateByPrimaryKeySelective(sysResource);
        if (result == 1) {
            // 日志
            return true;
        }
        return false;
    }

    /**
     * 分页条件查询字典类型
     *
     * @param sysResourceVO
     * @return
     */
    @Override
    public PageInfo<SysResource> list(SysResourceDTO sysResourceVO) {
        Integer pageNum = sysResourceVO.getPageNum();
        Integer pageSize = sysResourceVO.getPageSize();
        // 初始化分页信息
        PageHelper.startPage(pageNum == 0 ? 1 : pageNum, pageSize == 0 ? 10 : pageSize);
        // 查询菜单list
        List<SysResource> sysResourceList = sysResourceMapper.selectSelective(sysResourceVO);
        // pageHelper的收尾
        PageInfo<SysResource> pageResult = new PageInfo<>(sysResourceList);
        pageResult.setList(sysResourceList);
        return pageResult;
    }

    /**
     * 查询菜单
     *
     * @param id
     */
    @Override
    public SysResource get(Integer id) {
        return sysResourceMapper.selectByPrimaryKey(id);
    }


    /**
     * 树形结构展示菜单
     *
     * @return
     */
    @Override
    public List<SysResource> treeList() {
        SysResource sysResource = new SysResource();
        List<SysResource> resources = new ArrayList<>();
        List<SysResource> sysResources = sysResourceMapper.treeList(sysResource);
        if (sysResources.size() > 0) {
            try {
                resources = TreeUtilCommon.buildTree(sysResources, TreeUtilCommon.SYSRESOURCE,
                        "id", "parentId", "childList");
                return resources;
            } catch (Exception e) {
                log.error("其他异常：%s", e);
                throw new IllegalArgumentException("树形转换异常");
            }
        }
        return resources;
    }

}
