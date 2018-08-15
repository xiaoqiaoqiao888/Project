package com.camelot.pmt.controller;

import com.camelot.pmt.model.LoginUserVO;
import com.camelot.pmt.model.SysRole;
import com.camelot.pmt.model.SysUser;
import com.camelot.pmt.service.LogUserLoginService;
import com.camelot.pmt.service.SysRoleService;
import com.camelot.pmt.service.SysUserService;
import com.camelot.pmt.utils.ComUtil;
import com.camelot.pmt.utils.Constant;
import com.camelot.pmt.utils.JWTUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(description = "登陆认证")
@RestController
@Slf4j
public class LoginController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private LogUserLoginService logUserLoginService;

    @Autowired
    private SysRoleService sysRoleService;

    @Value("${jwt.header}")
    private String header;

    @PostMapping("/login")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userNo", value = "员工号", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, paramType = "query", dataType = "String") })
    public ResponseEntity<Map<String, Object>> login(@ApiIgnore SysUser sysUser, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        Integer userNo = sysUser.getUserNo();
        String password = sysUser.getPassword();
        if (ComUtil.isEmpty(userNo) || ComUtil.isEmpty(password)) {
            map.put("message", Constant.LoginMessage.USERNO_OR_PASSWORD_EMPTY);
            return new ResponseEntity<Map<String, Object>>(map, HttpStatus.NOT_ACCEPTABLE);

        }
        sysUser = sysUserService.queryByUserNo(userNo);
        if (ComUtil.isEmpty(sysUser) || !BCrypt.checkpw(password, sysUser.getPassword())) {
            map.put("message", Constant.LoginMessage.USERNO_OR_PASSWORD_ERROR);
            return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);

        }

        // 认证成功调用记录
        try {
            logUserLoginService.saveLogUserLogin(request, sysUser);
        } catch (Exception e) {
            log.error(Constant.LoginMessage.SAVE_LOGIN_LOG);
        }
        List<SysRole> roles = sysRoleService.selectRoleByUserId(sysUser.getId());
        List<String> list = new ArrayList<>();
        for (SysRole sysRole : roles) {
            list.add(sysRole.getId().toString());
        }
        LoginUserVO loginUserVO = new LoginUserVO();
        loginUserVO.setRoleIds(list);
        BeanUtils.copyProperties(sysUser, loginUserVO);
        Map<String, Object> result = new HashMap<String, Object>();
        String token = JWTUtil.sign(loginUserVO, loginUserVO.getPassword());
        sysUser.setPassword("");
        result.put("user", loginUserVO);
        result.put("token", token);
        // 将token写出到cookie
        Cookie cookie = new Cookie(header, token);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(1800 * 4);
        cookie.setPath("/");
        response.addCookie(cookie);
        response.flushBuffer();

        return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
    }

    /**
     * 登出
     */
    // Optional<Cookie> cookie = Arrays.stream(request.getCookies())
    // .filter(ck -> "Authorization".equals(ck.getName()))
    // .limit(1).map(ck -> {
    // ck.setMaxAge(0);
    // ck.setHttpOnly(true);
    // ck.setPath("/");
    // return ck;
    // }).findFirst();
    @GetMapping(value = "/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SecurityUtils.getSubject().logout();
        Cookie cookie = new Cookie(header, "");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        response.flushBuffer();
        return new ResponseEntity<Object>("退出成功", HttpStatus.OK);
    }

}
