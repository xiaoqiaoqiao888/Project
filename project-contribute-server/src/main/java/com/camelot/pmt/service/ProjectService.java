package com.camelot.pmt.service;

import com.camelot.pmt.model.CoreMember;
import com.camelot.pmt.model.Project;
import com.camelot.pmt.model.ProjectDTO;
import com.github.pagehelper.PageInfo;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ProjectService {

    /**
     * 分页查询项目
     *
     * @param pageNum
     * @param pageSize
     * @param projectState
     * @return
     */
    PageInfo<?> list(Integer pageNum, Integer pageSize, Integer projectState, String projectName);

    /**
     * 项目列表，条件查询
     *
     * @param map
     * @return
     */
    List<Project> selectProjectLists(Map<String, Object> map);

    /**
     * 新增项目
     *
     * @param project
     * @return boolean
     * @throws CloneNotSupportedException
     */
    int add(Project project) throws CloneNotSupportedException;

    /**
     * 修改项目
     *
     * @param project
     * @return boolean
     * @throws CloneNotSupportedException
     */
    boolean update(Project project) throws CloneNotSupportedException;

    /**
     * 根据项目id查询项目详情
     *
     * @param id
     * @return
     */
    Project selectByPrimaryKey(Integer id);

    /**
     * 项目详情添加核心成员，批量添加
     *
     * @param coreMember
     */
    void addCoreMember(List<CoreMember> coreMember);

    /**
     * 项目开始
     *
     * @param project
     * @return
     */
    boolean updateProjectTime(Project project) throws CloneNotSupportedException;

    /**
     * 项目延期
     *
     * @param project
     * @return
     */
    void updateProjectDelay(Date date);

    /**
     * 项目关闭
     *
     * @param project
     * @return
     */
    boolean updateProjectEnd(Project project) throws CloneNotSupportedException;

    /**
     * 项目完成
     *
     * @param project
     * @return
     */
    void updateProjectFinsh();

    /**
     * 校验项目编号
     */
    int selectProjectNo(String id);

    /**
     * 阶段统计
     * 
     * @return
     */
    List<ProjectDTO> selectStateCount();

}
