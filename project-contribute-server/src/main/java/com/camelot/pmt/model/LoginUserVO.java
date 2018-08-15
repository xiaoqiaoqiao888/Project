package com.camelot.pmt.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by fjy on 2018/5/24
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginUserVO extends SysUser {
    /**
     * 角色ID
     */
    private List<String> roleIds;

}
