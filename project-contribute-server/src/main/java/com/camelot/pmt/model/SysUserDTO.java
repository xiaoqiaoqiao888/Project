package com.camelot.pmt.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysUserDTO {

    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 员工号
     */
    private Integer userNo;

    /**
     * 电子邮件
     */
    private String email;

    /**
     * 电话
     */
    private String tel;

    /**
     * 组名称
     */
    private String groupName;

    /**
     * 组名称
     */
    private String parentGroupName;

    /**
     * 父id
     */
    private Integer parentId;

    /**
     * 员工状态值
     */
    private Integer state;

    /**
     * 员工姓名
     */
    private String realName;

    /**
     * 组id
     */
    private Integer groupId;

    /**
     * 页码
     */
    private Integer pageNum;

    /**
     * 页大小
     */
    private Integer pageSize;
}