package com.camelot.pmt.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author gxl
 * @ClassName: SysRoleResource
 * @Description: TODO(资源 ( 菜单)权限)
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysRoleResource implements Serializable {

    private static final long serialVersionUID = 8529564394952754806L;

    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 角色ID
     */
    private Integer roleId;

    /**
     * 资源ID
     */
    private Integer resourceId;

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