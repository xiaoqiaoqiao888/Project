package com.camelot.pmt.mapper;

import com.camelot.pmt.model.LogWork;

import java.util.List;
import java.util.Map;

/**
 * Created by 18811 on 2018/5/21.
 */
public interface LogWorkMapper {

    int insertLogWork(LogWork logWork);

    List<LogWork> selectLogWork(Integer workId);

    int andLogWorkBydelete(Map<String, Object> map);
}
