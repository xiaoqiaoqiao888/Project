package com.camelot.pmt.mapper;

import java.util.List;

import com.camelot.pmt.model.Stage;
import com.camelot.pmt.model.WorkCountDTO;

public interface StageMapper {
    /**
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * @mbggenerated
     */
    int insert(Stage record);

    /**
     * @mbggenerated
     */
    int insertSelective(Stage record);

    /**
     * @mbggenerated
     */
    Stage selectByPrimaryKey(Integer id);

    /**
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(Stage record);

    /**
     * @mbggenerated
     */
    int updateByPrimaryKeyWithBLOBs(Stage record);

    /**
     * @mbggenerated
     */
    int updateByPrimaryKey(Stage record);

    List<Stage> selectByStageName(Stage stage);

    Double selectSumStageByProjectId(Integer projectId);

    List<Stage> selectStateByProjectId(Stage stage);

    List<Stage> selectByProjectId(Integer projectId);

    List<Stage> selectStageUnderWay();

    List<Stage> selectAllStage(Integer projectId);

    int updateStageByProjectID(Stage stage);

    WorkCountDTO selectStageStatic(Integer projectId);

    List<Stage> stageStaticByStageState(Stage stage);

    Integer updateStageByProjectId(Stage stage);
}