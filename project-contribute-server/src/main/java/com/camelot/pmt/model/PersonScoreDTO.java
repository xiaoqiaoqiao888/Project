package com.camelot.pmt.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonScoreDTO {

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 组名称
     */
    private String groupName;

    /**
     * 总任务价值分
     */
    private Integer totalSumTaskValue;

    /**
     * 排名
     */
    private Integer rank;

    /**
     * 价值分状态
     */
    private Integer state;

}