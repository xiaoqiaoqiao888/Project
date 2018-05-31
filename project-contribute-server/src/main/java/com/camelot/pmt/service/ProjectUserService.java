package com.camelot.pmt.service;

import com.camelot.pmt.model.CoreMember;
import com.camelot.pmt.model.ProjectUser;
import com.camelot.pmt.model.ProjectUserInfo;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

public interface ProjectUserService {

    void addCoreMember(List<CoreMember> coreMember);

    void deleteCoreUserByRecoredId(Integer recoredId);

    List<ProjectUser> searchCoreMember(Integer projectId);
    
    boolean inProject(int userId, int projectId);
    
    void deleteCoreUser(Integer projectId, Integer userId);
    
    /**
     * Query the number of members based on the project id
     * @param projectId
     * @return
     */
    Integer countUserByProjectId(Integer projectId);
    
    /**
     * query the sum of real task time by project id
     * @author guodx
     * @param projectId
     * @return
     */
    Integer sumRelTaskTime(Integer projectId);
    
    /**
     * query the sum of exp task time by project id
     * @author guodx
     * @param projectId
     * @return
     */
    Integer sumExpTaskTime(Integer projectId);
    
    Map<String, Integer> sumProjectUser(Integer projectId);

    /**
     * 项目查询成员
     * @param info
     * @return
     */
    PageInfo<ProjectUserInfo> search(ProjectUserInfo info);

}
