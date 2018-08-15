package com.camelot.pmt.service.impl;

import com.camelot.pmt.mapper.LogUserLoginMapper;
import com.camelot.pmt.model.LogUserLogin;
import com.camelot.pmt.model.SysUser;
import com.camelot.pmt.service.LogUserLoginService;
import com.camelot.pmt.utils.Constant;
import com.camelot.pmt.utils.IpAddressUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * @author gxl
 * @ClassName: LogUserLoginServiceImpl
 * @Description: TODO(登录日志ServiceImpl)
 * @date 2018年5月10日 下午2:55:32
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class LogUserLoginServiceImpl implements LogUserLoginService {
    @Autowired
    private LogUserLoginMapper logUserLoginMapper;

    /**
     * 新增系统登录日志
     *
     * @param logUserLogin
     *            访问日志对象
     */
    @Override
    public void insertLogUserLogin(LogUserLogin logUserLogin) {
        logUserLoginMapper.insertLogUserLogin(logUserLogin);
    }

    /**
     * 查询用户登录日志集合
     *
     * @param logUserLogin
     *            访问日志对象
     * @return 登录记录集合
     */
    @Override
    public PageInfo<?> queryLogUserLoginList(Integer pageNum, Integer pageSize, LogUserLogin logUserLogin) {
        // 初始化分页信息
        PageHelper.startPage(pageNum, pageSize);
        // 查询list集合
        List<LogUserLogin> logUserLoginList = logUserLoginMapper.queryLogUserLoginList(logUserLogin);
        PageInfo<LogUserLogin> pageResult = new PageInfo<>(logUserLoginList);
        return pageResult;
    }

    /**
     * 批量删除用户登录日志
     *
     * @param ids
     *            需要删除的数据
     * @return
     */
    @Override
    public int batchDeleteLogUserLogin(Long[] ids) {
        return logUserLoginMapper.batchDeleteLogUserLogin(ids);
    }

    /**
     * 记录格式 [ip][用户名][操作]
     *
     * @param request
     * @return void 返回类型
     */
    @Override
    public void saveLogUserLogin(HttpServletRequest request, SysUser sysUser) {
        LogUserLogin logUserLogin = new LogUserLogin();
        logUserLogin.setLoginIp(IpAddressUtil.getIpAddress(request));
        logUserLogin.setLoginTime(new Date());
        logUserLogin.setLoginUser(sysUser.getUserName());
        logUserLogin.setState(Integer.valueOf(Constant.DataStatus.EFFECTIVE));
        logUserLogin.setCreatedBy(sysUser.getId());
        logUserLogin.setUpdatedBy(sysUser.getId());
        insertLogUserLogin(logUserLogin);
    }
}
