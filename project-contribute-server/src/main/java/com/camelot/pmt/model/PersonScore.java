package com.camelot.pmt.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonScore {
    /**
     * ID
     */
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 项目ID
     */
    private Integer projectId;

    /**
     * 阶段ID
     */
    private Integer stageId;

    /**
     * 工程包ID
     */
    private Integer workId;

    /**
     * 任务ID
     */
    private Integer taskId;

    /**
     * 任务价值分
     */
    private Integer taskValue;

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
    @DateTimeFormat(pattern = "yyyy-MM-ddHH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 修改人
     */
    private Integer updateBy;

    /**
     * 修改时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-ddHH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

}