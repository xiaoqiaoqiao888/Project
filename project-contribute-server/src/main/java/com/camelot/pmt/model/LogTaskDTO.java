package com.camelot.pmt.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 任务操作记录详情
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogTaskDTO {

    private Integer id;

    private Integer taskId;

    private Integer taskPersonId;
    
    private Integer state;

    private String operateDesc;

    @DateTimeFormat(pattern = "yyyy-MM-ddHH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

}