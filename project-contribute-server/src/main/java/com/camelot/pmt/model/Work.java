package com.camelot.pmt.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class Work {
    /**
     * ID
     */
    private Integer id;

    /**
     * 项目ID
     */
    private Integer projectId;

    /**
     * 阶段ID
     */
    private Integer stageId;

    /**
     * 工程包名称
     */
    private String workName;

    /**
     * 工程包预算
     */
    private BigDecimal workBudget;

    /**
     * 价值分
     */
    private Integer workValue;

    /**
     * 工程包预计开始时间
     */
    private Date startTime;

    /**
     * 工程包预计结束时间
     */

    private Date endTime;

    /**
     * 工程包业务状态自定
     */
    private Integer workState;

    /**
     * 工程包类型
     */
    private Integer workType;

    /**
     * 状态值 0-无效 1-有效
     */
    private Integer state;

    /**
     * 创建人
     */
    private Integer createBy;

    /**
     * 修改人真实姓名
     */
    private String createUserName;

    /**
     * 创建时间
     */

    private Date createTime;

    /**
     * 修改人id
     */
    private Integer updateBy;

    /**
     * 修改人真实姓名
     */
    private String cherealname;

    /**
     * 修改时间
     */

    private Date updateTime;

    /**
     * 工程包描述
     */
    private String workDesc;

    /**
     * 提交时间
     */

    public Date endSubmitTime;
}