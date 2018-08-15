package com.camelot.pmt.mapper;

import com.camelot.pmt.model.LogUserLogin;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author gxl
 * @ClassName: LogUserLoginMapper
 * @Description: TODO(用户登录Mapper层)
 */

public interface LogUserLoginMapper {

    /**
     * 新增系统登录日志
     *
     * @param logUserLogin
     *            访问日志对象
     */
    void insertLogUserLogin(LogUserLogin logUserLogin);

    /**
     * 查询用户登录日志集合
     *
     * @param logUserLogin
     *            访问日志对象
     * @return 登录记录集合
     */
    List<LogUserLogin> queryLogUserLoginList(LogUserLogin logUserLogin);

    /**
     * 批量删除用户登录日志
     *
     * @param ids
     *            需要删除的数据
     * @return
     */
    int batchDeleteLogUserLogin(@Param("ids") Long[] ids);

}