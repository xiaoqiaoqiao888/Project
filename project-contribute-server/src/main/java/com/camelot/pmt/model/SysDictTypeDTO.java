package com.camelot.pmt.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: lxk
 * @CreateDate: 2018/5/10 10:38
 * @Description: 数据字典类型VO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysDictTypeDTO {
    /**
     * 类型code
     */
    private String typeCode;

    /**
     * 类型名称
     */
    private String typeName;

    /**
     * 状态值 0-无效 1-有效
     */
    private Integer state;

    /**
     * 页码
     */
    private Integer pageNum;

    /**
     * 页大小
     */
    private Integer pageSize;
}
