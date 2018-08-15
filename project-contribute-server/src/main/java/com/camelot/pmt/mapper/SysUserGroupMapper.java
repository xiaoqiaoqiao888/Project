package com.camelot.pmt.mapper;

import com.camelot.pmt.model.Stage;
import com.camelot.pmt.model.SysResourceDTO;
import com.camelot.pmt.model.SysUser;
import com.camelot.pmt.model.SysUserDTO;
import com.camelot.pmt.model.SysUserGroup;
import com.camelot.pmt.model.Task;
import com.camelot.pmt.model.Work;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SysUserGroupMapper {
    /**
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * @mbggenerated
     */
    int insert(SysUserGroup record);

    /**
     * @mbggenerated
     */
    int insertSelective(SysUserGroup record);

    /**
     * @mbggenerated
     */
    SysUserGroup selectByPrimaryKey(Integer id);

    /**
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(SysUserGroup record);

    /**
     * @mbggenerated
     */
    int updateByPrimaryKey(SysUserGroup record);

    int updateStateByPrimaryKey(SysUserGroup sysUserGroup);

    List<Map<String, Object>> selectGroupUserSelective(SysUserDTO sysUserDTO);

    List<SysUser> userListByGroupId(Integer id);

    int updateTaskList(Map<String, Object> trueMap);

    int updateWorkList(Map<String, Object> trueMap);

    int updateStageList(Map<String, Object> trueMap);

    int countGroupUser(Integer id);

    List<Map<String, Object>> queryTaskListByParamsOrder(Task task);

    List<Task> queryTaskListByWork(Work work);

    List<SysUser> userListNoRoleByGroupId(@Param(value = "groupId") Integer groupId,
            @Param(value = "roleId") Integer roleId);

    List<SysUser> userListNoProjectByGroupId(@Param(value = "groupId") Integer groupId,
            @Param(value = "projectId") Integer projectId, @Param(value = "realName") String realName);

    List<Stage> selectStageListByProjectId(SysResourceDTO sysResourceDTO);

    int countGroupUserByList(List<Integer> idList);
}