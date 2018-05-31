package com.camelot.pmt.service;

import com.camelot.pmt.model.Project;
import com.camelot.pmt.model.Stage;
import com.camelot.pmt.model.SysResourceDTO;
import com.camelot.pmt.model.Task;
import com.camelot.pmt.model.Work;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * 　　* @author muyuanpei
 * 　　* @date 2018/5/14
 */
public interface CheckManagerService {


    int updateStateList(SysResourceDTO sysResourceVO) throws CloneNotSupportedException;

    PageInfo<Map<String, Object>> selectTaskListByPageInfo(Task task, Integer pageNum, Integer pageSize);

    List<Work> getWorkListByStageId(Stage stage);

    List<Task> getTaskListByWorkId(Work work);

    int updateByPrimaryKeySelective(Task record);

    int addTaskLog(Map<String, Object> map);

    int addWorkLog(Map<String, Object> map);

    int addStageLog(Map<String, Object> map);

    Boolean checkProject(SysResourceDTO sysResourceVO);

    //项目完成
    int updateProjectFinsh(Integer pid);
}
