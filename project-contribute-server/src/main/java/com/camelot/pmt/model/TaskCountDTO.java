package com.camelot.pmt.model;

import lombok.Data;

/**
 * @Author: lyh
 * @CreateDate: 2018/5/25 15:45
 * @Description:
 */
@Data
public class TaskCountDTO {
    /**
     * 超预算
     */
    private Integer overBudget;

    /**
     * 未超预算
     */
    private Integer onOverBudget;

    /**
     * 未开始
     */
    private Integer noStart;

    /**
     * 进行中
     */
    private Integer haveInIand;

    /**
     * 延期进行中
     */
    private Integer delayHaveInHand;

    /**
     * 已完成
     */
    private Integer completed;

    /**
     * 待验收
     */
    private Integer waitCheck;

    /**
     * 总条数
     */
    private Integer total;
}
