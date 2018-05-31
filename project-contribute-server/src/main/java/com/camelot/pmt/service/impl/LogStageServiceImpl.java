package com.camelot.pmt.service.impl;

import com.camelot.pmt.mapper.LogStageMapper;
import com.camelot.pmt.model.LogStage;
import com.camelot.pmt.model.SysUser;
import com.camelot.pmt.service.LogStageService;
import com.camelot.pmt.service.SysUserService;
import com.camelot.pmt.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class LogStageServiceImpl implements LogStageService {

    @Autowired
    private LogStageMapper logStageMapper;
    @Autowired
    private SysUserService sysUserService;

    /**
     * 划分阶段时插入日志
     * @throws CloneNotSupportedException
     */

    @Override
    public void logOfAdd(LogStage logStage) throws CloneNotSupportedException {
        SysUser user = TokenUtil.getUserFromToken();
        int userId = user.getId();
        logStage.setCreateBy(userId);
        logStage.setUpdateBy(userId);
        logStage.setCreateTime(new Date());
        logStage.setUpdateTime(new Date());
        logStageMapper.insertSelective(logStage);
    }

    @Override
    public List<LogStage> selectStageLogByProjectId(Integer stageId) {
        List<LogStage> logStages = logStageMapper.selectStageLogByProjectId(stageId);
        for (LogStage logStage:
            logStages) {
            Integer updateBy = logStage.getUpdateBy();
            SysUser sysUsers = sysUserService.selectByPrimaryKey(updateBy);
            String realName = sysUsers.getRealName();
            logStage.setRealName(realName);
        }
        return logStages;
    }


}
