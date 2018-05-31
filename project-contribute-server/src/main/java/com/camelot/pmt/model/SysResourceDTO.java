package com.camelot.pmt.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysResourceDTO {

    /**
     * id
     */
    private Integer id;

    /**
     * 父ID
     */
    private Integer parentId;

    /**
     * 资源名称
     */
    private String text;

    /**
     * 资源路径
     */
    private String href;

    /**
     * 图标
     */
    private String iconcls;

    /**
     * 资源类型
     */
    private Integer type;

    /**
     * 权限标示
     */
    private String permission;

    /**
     * 排序
     */
    private Integer sortNo;

    /**
     * 显示标示 0-不显示 1-显示
     */
    private Integer isShow;

    /**
     * 状态值 0-无效 1-有效
     */
    private Integer state;

    /**
     * 状态码 1-任务 2-工程包 3-阶段
     */
    private Integer status;

    /**
     * 项目id
     */
    private Integer projectId;

    /**
     * 任务id
     */
    private Integer taskID;

    /**
     * 工程包id
     */
    private Integer workId;

    /**
     * 阶段id
     */
    private Integer stageId;

    /**
     * 确认验收的id
     */
    private Integer trueId;

    /**
     * 确认批量验收的id
     */
    private String trueIdStr;

    /**
     * 确认批量验收的id（List）
     */
    private String trueIdList;

    /**
     * 页码
     */
    private Integer pageNum;

    /**
     * 页大小
     */
    private Integer pageSize;

}