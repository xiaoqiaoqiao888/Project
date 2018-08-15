package com.camelot.pmt.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.exceptions.InvalidClaimException;
import com.camelot.pmt.model.SysUser;
import com.camelot.pmt.service.SysUserService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class TokenUtil {

    static final String CLAIM_KEY_USERNAME = "sub";
    static final String CLAIM_KEY_AUDIENCE = "audience";
    static final String CLAIM_KEY_CREATED = "created";
    static final String CLAIM_KEY_USERID = "userId";
    static final String CLAIM_KEY_REALNAME = "realName";
    static final String CLAIM_KEY_TEL = "tel";
    static final String CLAIM_KEY_EMAIL = "email";

    private static String secret;

    private static Long expiration;

    private SysUserService sysUserService;

    public String getSecret() {
        return secret;
    }

    @Value("${jwt.token.secret}")
    public void setSecret(String secret) {
        TokenUtil.secret = secret;
    }

    public Long getExpiration() {
        return expiration;
    }

    @Value("${jwt.token.expiration}")
    public void setExpiration(Long expiration) {
        TokenUtil.expiration = expiration;
    }

    public static Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    /**
     * 生成token
     *
     * @param username
     *            用户名
     */
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        Map<String, Object> header = new HashMap<String, Object>();
        header.put("typ", "JWT");
        header.put("alg", "HS512");
        SysUser sysUser = sysUserService.queryByUserName(username);
        claims.put(CLAIM_KEY_USERNAME, username);
        if (sysUser != null) {
            claims.put(CLAIM_KEY_USERID, sysUser.getId());
            if (StringUtils.isNoneEmpty(sysUser.getRealName())) {
                claims.put(CLAIM_KEY_REALNAME, sysUser.getRealName());
            }
            if (StringUtils.isNoneEmpty(sysUser.getTel())) {
                claims.put(CLAIM_KEY_TEL, sysUser.getTel());
            }
            if (StringUtils.isNoneEmpty(sysUser.getEmail())) {
                claims.put(CLAIM_KEY_EMAIL, sysUser.getEmail());
            }
        } else {
            throw new UnknownAccountException();
        }
        // TODO 存放角色ID
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(header, claims);
    }

    private String generateToken(Map<String, Object> header, Map<String, Object> claims) {
        return Jwts.builder().setHeader(header).setClaims(claims).setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, TokenUtil.secret).compact();
    }

    /**
     * 生成token时间 = 当前时间 + expiration（properties中配置的失效时间）
     */
    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    /**
     * 根据token获取用户
     */
    public static SysUser getUserFromToken() {
        HttpServletRequest httpRequest = RequestResponseContext.getRequest();
        SysUser sysUser = (SysUser) httpRequest.getAttribute("currentUser");
        if (Objects.isNull(sysUser)) {
            throw new InvalidClaimException("token获取当前登陆人错误");
        }
        return sysUser;
    }

    /**
     * 根据token获取用户名
     */
    public static String getUsernameFromToken(String token) {
        String userName = "";
        try {
            final Claims claims = getClaimsFromToken(token);
            userName = (claims.getSubject());
        } catch (Exception e) {
            userName = null;
        }
        return userName;
    }

    /**
     * 判断token失效时间是否到了
     */
    @SuppressWarnings("unused")
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    /**
     * 获取设置的token失效时间
     */
    public Date getExpirationDateFromToken(String token) {
        Date expiration;
        try {
            final Claims claims = getClaimsFromToken(token);
            expiration = claims.getExpiration();
        } catch (Exception e) {
            expiration = null;
        }
        return expiration;
    }

    // /**
    // * Token失效校验
    // * @param token token字符串
    // * @param loginInfo 用户信息
    // * @return
    // */
    // public Boolean validateToken(String token, LoginInfo loginInfo) {
    // final String username = getUsernameFromToken(token);
    // return (
    // username.equals(loginInfo.getUsername())
    // && !isTokenExpired(token));
    // }

    public String refreshToken(String token) {
        final Claims claims = TokenUtil.getClaimsFromToken(token);
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken((String) claims.get(CLAIM_KEY_USERNAME));
    }
}
