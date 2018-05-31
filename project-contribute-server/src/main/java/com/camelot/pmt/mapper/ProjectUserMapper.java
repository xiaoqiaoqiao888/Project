package com.camelot.pmt.mapper;

import com.camelot.pmt.model.CoreMember;
import com.camelot.pmt.model.ProjectUser;
import com.camelot.pmt.model.ProjectUserInfo;

import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface ProjectUserMapper {
    /**
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * @mbggenerated
     */
    int insert(ProjectUser record);

    /**
     * @mbggenerated
     */
    int insertSelective(ProjectUser record);

    /**
     * @param projectId
     * @mbggenerated
     */
    ProjectUser selectByUserId(@Param("userId") Integer userId, @Param("projectId") int projectId);

    /**
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(ProjectUser record);

    /**
     * @mbggenerated
     */
    int updateByPrimaryKey(ProjectUser record);

    /**
     * 添加核心成员
     *
     * @param coreMember
     */
    void addCoreMember(List<CoreMember> coreMember);

    List<ProjectUser> searchCoreMember(Integer projectId);

    /**
     * 根据项目成员状态查询成员
     * 
     * @param projectUserTypeCommon
     * @return
     */
    ProjectUser searchByType(int type);

    /**
     * 删除核心成员 修改出项目时间，修改时间，修改人， 数据状态为失效
     * 
     * @param puser
     */
    void deleteCoreUser(ProjectUser puser);

    /**
     * 核心人员的消耗成本
     */
    List<ProjectUser> selectProjectUserCost();

    Integer countUser(Integer projectId);

    ProjectUser isCoreMember(ProjectUser puser);

    List<ProjectUserInfo> searchUserOfProject(ProjectUserInfo info);

    void updateUserRecord(@Param("updateTime") Date updateTime, @Param("projectId") Integer projectId,
            @Param("userId") Integer userId);

}