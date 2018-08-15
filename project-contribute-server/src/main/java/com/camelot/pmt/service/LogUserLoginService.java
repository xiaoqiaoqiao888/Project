package com.camelot.pmt.service;

import com.camelot.pmt.model.LogUserLogin;
import com.camelot.pmt.model.SysUser;
import com.github.pagehelper.PageInfo;

import javax.servlet.http.HttpServletRequest;

/**
 * @author gxl
 * @ClassName: LogUserLoginService
 * @Description: TODO(登录日志接口)
 * @date 2018年5月10日 下午2:54:56
 */
public interface LogUserLoginService {

    /**
     * 新增用户登录日志
     *
     * @param logUserLogin
     *            访问日志对象
     */
    void insertLogUserLogin(LogUserLogin logUserLogin);

    /**
     * 查询系统登录日志集合
     *
     * @param logUserLogin
     *            访问日志对象
     * @param pageNum
     * @param pageSize
     * @return 登录记录集合
     */
    PageInfo<?> queryLogUserLoginList(Integer pageNum, Integer pageSize, LogUserLogin logUserLogin);

    /**
     * 批量删除用户登录日志
     *
     * @param ids
     *            需要删除的数据
     * @return
     */
    int batchDeleteLogUserLogin(Long[] ids);

    /**
     * 新增用户登录日志调用接口
     *
     * @param request
     * @param sysUser
     * @return
     */
    void saveLogUserLogin(HttpServletRequest request, SysUser sysUser);
}
