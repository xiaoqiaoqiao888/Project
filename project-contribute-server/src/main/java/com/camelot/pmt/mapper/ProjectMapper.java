package com.camelot.pmt.mapper;

import com.camelot.pmt.model.Project;
import com.camelot.pmt.model.ProjectDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ProjectMapper {
    /**
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * @mbggenerated
     */
    int insertSelective(Project record);

    /**
     * @mbggenerated
     */
    Project selectByPrimaryKey(Integer id);

    /**
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(Project record);

    // 项目开始
    int updateProjectTime(Project project);

    // 项目延期
    int updateProjectDelay(Project project);

    // 项目关闭
    int updateProjectEnd(Project project);

    // 项目完成
    int updateProjectFinsh(Project project);

    // 查询项目列表分页
    List<Map<String, Object>> selectProjectList(HashMap<String, Object> listMap);

    // 查询项目列表
    List<Project> selectProjectLists(Map<String, Object> listMap);

    // 某个项目中已完成的阶段数量
    int selectProStageFinsh(Integer id);

    // 某个项目中的所有阶段数量
    int selectProStageAll(Integer id);

    // 所有状态为进行中或者延期进行中的项目
    List<Project> selectProjectState();

    // 校验项目编号
    int selectProjectNo(String id);

    // 阶段统计
    List<ProjectDTO> selectStateCount();
}