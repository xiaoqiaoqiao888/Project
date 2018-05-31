package com.camelot.pmt.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogSysGroup {
    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 组名称
     */
    private String groupName;

    /**
     * 组的描述
     */
    private String groupDesc;

    /**
     * 部门级别
     */
    private Integer groupLevel;

    /**
     * 父级ID
     */
    private Integer parentId;

    /**
     * 状态值 0-无效 1-有效
     */
    private Integer state;

    /**
     * 创建人
     */
    private Integer createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private Integer updateBy;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 日志描述
     */
    private String operateDesc;

    /**
     * 部门组id
     */
    private Integer sysGroupId;
}