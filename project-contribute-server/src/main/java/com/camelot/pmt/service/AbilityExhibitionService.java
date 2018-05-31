package com.camelot.pmt.service;

import com.github.pagehelper.PageInfo;

import java.util.Map;

/**
 * @description: 职能能力展示服务 接口类
 * @author: Gnerv LiGen
 * @date: 2018-05-16
 **/
public interface AbilityExhibitionService {

    /**
     * 根据职能组id 做人员职能能力展示
     *
     * @param page
     * @param rows
     * @param groupId
     * @param realName
     * @param cycle
     * @return
     */
    PageInfo<Map<String, Object>> selectAbilityExhibition(
            Integer page, Integer rows, Integer groupId, String realName, Integer cycle);

    /**
     * 根据人员id 查询价值分明细
     *
     * @param page
     * @param rows
     * @param sysUserId
     * @return
     */
    PageInfo<Map<String, Object>> selectValuePointsDetailsBySysUserId(Integer page, Integer rows, Integer sysUserId);

    /**
     * 根据任务id 查询任务详情
     *
     * @param taskId
     * @return
     */
    Map<String, Object> selectTaskDetailsByTaskId(Integer taskId);

    /**
     * 根据用户id 追溯任务信息
     * @param userId
     * @param cycle
     * @return
     */
    Map<String, Object>  selectTaskTracing(Integer userId, Integer cycle);
}
