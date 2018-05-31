package com.camelot.pmt.service;

import com.camelot.pmt.model.LogStage;
import com.camelot.pmt.model.Stage;
import com.camelot.pmt.model.WorkCountDTO;
import com.camelot.pmt.model.WorkDTO;
import com.github.pagehelper.PageInfo;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;

/**
 * StageService class
 *
 * @author zhangzhan
 * @date 2018/5/14
 */
public interface StageService {
    /**
     * @author zhangzhan
     * @date 2018/5/15
     * @description 根据阶段名查询
     */
    List<Stage> selectByStageName(Stage stage);

    /**
     * @author zhangzhan
     * @date 2018/5/15
     * @description 添加阶段
     */

    Boolean insertStage(Stage stage) throws CloneNotSupportedException;

    /**
     * @author zhangzhan
     * @date 2018/5/15
     * @description 修改阶段
     */

    Boolean updateStage(Stage stage) throws CloneNotSupportedException;

    /**
     * @author zhangzhan
     * @date 2018/5/15
     * @description 根据阶段id查询阶段
     */

    Stage selectById(Stage stage);

    /**
     * @author zhangzhan
     * @date 2018/5/15
     * @description 阶段统计
     */

    Double statisticsStage(Integer projectId);

    /**
     * @author zhangzhan
     * @date 2018/5/15
     * @description 根据项目id查询阶段列表
     */

    PageInfo<Stage> stateList(Stage stage, Integer pageNum, Integer pageSize);

    /**
     * @author zhangzhan
     * @date 2018/5/15
     * @description 根据项目id查询待完成和已完成的数据
     */

    PageInfo<Stage> selectStateListByProjectId(Integer projectId, Integer pageNum, Integer pageSize);

    /**
     * @author zhangzhan
     * @date 2018/5/15
     * @description 定时修改阶段状态
     */

    boolean updateStageState(Date date);


    Boolean deleteByStageId(Stage stage) throws CloneNotSupportedException;

    List<Stage> selectAllStage(Integer projectId);

    boolean closeStageByProjectID(Stage stage)  throws CloneNotSupportedException;

    WorkCountDTO stageStatic(Integer projectId);

    PageInfo<Stage> stageStaticByStageState(Stage stage, Integer pageNum, Integer pageSize);

    boolean updateStageByProjectId(Stage stage);

    PageInfo<Stage> selectStageBudgetByProjectId(Stage stage, Integer pageNum, Integer pageSize);

    List<Stage> selectStageBudget(Integer projectId);
}
