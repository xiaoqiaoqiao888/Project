package com.camelot.pmt.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by fjy on 2018/5/24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginUserVO extends SysUser {
    /**
     * 角色ID
     */
    private List<String> roleIds;

}
