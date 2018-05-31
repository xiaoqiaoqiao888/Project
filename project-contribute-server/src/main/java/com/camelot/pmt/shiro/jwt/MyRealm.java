package com.camelot.pmt.shiro.jwt;

import com.camelot.pmt.model.SysRole;
import com.camelot.pmt.model.SysUser;
import com.camelot.pmt.service.SysResourceService;
import com.camelot.pmt.service.SysRoleService;
import com.camelot.pmt.service.SysUserService;
import com.camelot.pmt.utils.ApplicationContextUtil;
import com.camelot.pmt.utils.JWTUtil;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
public class MyRealm extends AuthorizingRealm {

    private SysUserService sysUserService;
    // 角色
    private SysRoleService sysRoleService;
    // 菜单
    private SysResourceService sysResourceService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    /**
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        if (sysRoleService == null) {
            this.sysRoleService = ApplicationContextUtil.getBean(SysRoleService.class);
        }
        if (sysResourceService == null) {
            this.sysResourceService = ApplicationContextUtil.getBean(SysResourceService.class);
        }

        String username = JWTUtil.getUsername(principals.toString());
        SysUser user = sysUserService.queryByUserName(username);
        ArrayList<String> pers = new ArrayList<>();
        if (user != null) {
            List<SysRole> sysRoles = sysRoleService.selectRoleByUserId(user.getId());
            for (SysRole sysRole : sysRoles) {
                pers.add(sysRole.getId().toString());
            }

        }
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        Set<String> permission = new HashSet<>(pers);
        simpleAuthorizationInfo.addStringPermissions(permission);
        return simpleAuthorizationInfo;
    }

    /**
     * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可。
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws UnauthorizedException {
        if (sysUserService == null) {
            this.sysUserService = ApplicationContextUtil.getBean(SysUserService.class);
        }
        String token = (String) auth.getCredentials();

        // 解密获得userId，用于和数据库进行对比
        Integer userId = JWTUtil.getUserId(token);
        if (userId == null) {
            throw new UnauthorizedException("令牌错误");
        }

        SysUser userBean = sysUserService.selectByPrimaryKey(userId);
        if (userBean == null) {
            log.error("token查询user异常");
            throw new UnauthorizedException("用户名不存在!");
        }
        if (!JWTUtil.verify(token, userId, userBean.getPassword())) {
            throw new UnauthorizedException("用户名或密码错!");
        }
        return new SimpleAuthenticationInfo(token, token, this.getName());
    }
}
