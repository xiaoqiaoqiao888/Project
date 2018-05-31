package com.camelot.pmt.model;

import lombok.Data;

/**
 * @FileName: WorkCountDTO
 * @Description: 功能包清单统计
 * @author: xueyj
 * @create: 2018-05-21 16:47
 */
@Data
public class WorkCountDTO {

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
