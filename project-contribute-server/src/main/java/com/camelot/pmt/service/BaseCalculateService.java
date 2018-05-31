package com.camelot.pmt.service;

import com.camelot.pmt.model.SysUser;

import java.util.Date;
import java.util.List;

/**
 * @author Administrator
 * @date 2018/5/15 10:01
 */
public interface BaseCalculateService {

    /**
     * 获取人员成本到天
     *
     * @param users 用户列表（需要参数ID,COST,USERNAME,USERNO）
     * @param date  时间 获取这个月的人员成本到天
     * @return sysUser.list
     */
    List<SysUser> getPersonCost(List<SysUser> users, Date date);

    /**
     * 获取人员成本到小时或者天
     *
     * @param users 用户列表（需要参数ID,COST,USERNAME,USERNO）
     * @param date  时间 获取这个月的人员成本到天
     * @param isDay 到小时是false，到天为true
     * @return sysUser.list
     */
    List<SysUser> getPersonCost(List<SysUser> users, Date date, boolean isDay);

    /**
     * 获取实际工作时间（只算工作日）
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return int小时数
     */
    double getWorkHours(Date startDate, Date endDate);

    /**
     * 判断传入时间是否在工作日里
     *
     * @param nowDate 传入事件
     * @return true （在工作日里）; false (不在工作日内)
     */
    boolean isWorkDays(Date nowDate);
}
