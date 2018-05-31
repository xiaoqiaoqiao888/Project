package com.camelot.pmt.mapper;

import com.camelot.pmt.model.Project;

import java.util.List;

public interface LogProjectMapper {

    void logOfAdd(Project project);

    List<Project> queryAll(Integer projectId);


}
