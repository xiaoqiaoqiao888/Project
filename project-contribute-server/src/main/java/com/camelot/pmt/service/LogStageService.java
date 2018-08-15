package com.camelot.pmt.service;

import com.camelot.pmt.model.LogStage;

import java.util.List;

public interface LogStageService {

    void logOfAdd(LogStage logStage) throws CloneNotSupportedException;

    List<LogStage> selectStageLogByProjectId(Integer stageId);
}
