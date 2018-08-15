package com.camelot.pmt.service;

import com.camelot.pmt.model.Project;

import java.util.List;

public interface LogProjectService {

    void logOfAdd(Project project) throws CloneNotSupportedException;

    /**
     * 查询所有项目日志
     * 
     * @param projectId
     * @return
     */
    List<Project> queryAll(Integer projectId);

}
