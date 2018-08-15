package com.camelot.pmt.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author gxl
 * @ClassName: SysRoleResourceDTO
 * @Description: TODO(资源 ( 菜单)权限)
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysRoleResourceDTO implements Serializable {

    private static final long serialVersionUID = 8529564394952712306L;

    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 角色
     */
    private SysRole sysRole;

    /**
     * 资源ID
     */
    private SysResource sysResource;

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

}