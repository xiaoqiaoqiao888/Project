package com.camelot.pmt.service.impl;

import com.camelot.pmt.mapper.LogProjectMapper;
import com.camelot.pmt.model.Project;
import com.camelot.pmt.model.SysUser;
import com.camelot.pmt.service.LogProjectService;
import com.camelot.pmt.utils.TokenUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogProjectServiceImpl implements LogProjectService {

    @Autowired
    private LogProjectMapper logProjectMapper;

    /**
     * 新增项目时插入日志
     * 
     * @throws CloneNotSupportedException
     */
    @Override
    public void logOfAdd(Project project) throws CloneNotSupportedException {
        SysUser user = TokenUtil.getUserFromToken();
        int userId = user.getId();
        project.setCreateBy(userId);
        project.setUpdateBy(userId);
        project.setId(null);
        logProjectMapper.logOfAdd(project);
    }

    /**
     * 查询项目日志
     */
    @Override
    public List<Project> queryAll(Integer projectId) {
        return logProjectMapper.queryAll(projectId);
    }

}
