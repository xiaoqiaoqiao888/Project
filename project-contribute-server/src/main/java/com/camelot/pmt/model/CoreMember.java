package com.camelot.pmt.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * 项目添加核心成员参数接收
 *
 * @author guodx 2018年5月15日 上午9:47:39
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CoreMember extends ProjectUser {

    public static final int PROJECT_USER_STATE_EFFECTIVE = 1; // 有效

    public static final int PROJECT_USER_STATE_DISABLE = 0; // 无效

    public static final int PROJECT_USER_TYPE_CORE = 0; // 核心成员

    public static final int PROJECT_USER_TYPE_COMMON = 1; // 普通成员

    private Integer userId;

    @JsonFormat(pattern = "yyyy/MM/dd", timezone = "GMT+8")
    private Date inTime;

    @JsonFormat(pattern = "yyyy/MM/dd", timezone = "GMT+8")
    private Date outTime;
}
