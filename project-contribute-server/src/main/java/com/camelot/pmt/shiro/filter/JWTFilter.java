package com.camelot.pmt.shiro.filter;

import com.camelot.pmt.model.SysUser;
import com.camelot.pmt.service.SysUserService;
import com.camelot.pmt.shiro.jwt.JWTToken;
import com.camelot.pmt.utils.ApplicationContextUtil;
import com.camelot.pmt.utils.ComUtil;
import com.camelot.pmt.utils.Constant;
import com.camelot.pmt.utils.JWTUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 代码的执行流程preHandle->isAccessAllowed->isLoginAttempt->executeLogin
 */
@Slf4j
public class JWTFilter extends BasicHttpAuthenticationFilter {


    private SysUserService sysUserService;

    private static final String TOKEN = "Authorization";

    /**
     * 判断用户是否想要登入。
     * 检测header里面是否包含Authorization字段即可
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        HttpServletRequest req = (HttpServletRequest) request;
        String authorization = getToken(req, response);
        return authorization != null;
    }

    /**
     *
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) {
        String authorization = getToken(request, response);
        JWTToken token = new JWTToken(authorization);
        // 提交给realm进行登入，如果错误他会抛出异常并被捕获
        getSubject(request, response).login(token);
        // 如果没有抛出异常则代表登入成功，返回true
        setUserBean(request, response, token);
        return true;
    }

    /**
     * 这里我们详细说明下为什么最终返回的都是true，即允许访问
     * 例如我们提供一个地址 GET /article
     * 登入用户和游客看到的内容是不同的
     * 如果在这里返回了false，请求会被直接拦截，用户看不到任何东西
     * 所以我们在这里返回true，Controller中可以通过 subject.isAuthenticated() 来判断用户是否登入
     * 如果有些资源只有登入用户才能访问，我们只需要在方法上面加上 @RequiresAuthentication 注解即可
     * 但是这样做有一个缺点，就是不能够对GET,POST等请求进行分别过滤鉴权(因为我们重写了官方的方法)，但实际上对应用影响不大
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if (isLoginAttempt(request, response)) {
            try {
                executeLogin(request, response);
            } catch (Exception e) {
                log.error("认证异常");
                response401(request, response,Constant.LoginMessage.UN_AUTHENTICATED);
            }
        }
        return true;
    }

    private void setUserBean(ServletRequest request, ServletResponse response, JWTToken token) {
        if (this.sysUserService == null) {
            this.sysUserService = ApplicationContextUtil.getBean(SysUserService.class);
        }
        Integer userId = JWTUtil.getUserId(token.getPrincipal().toString());
        SysUser userBean = sysUserService.selectByPrimaryKey(userId);
        request.setAttribute("currentUser", userBean);
    }

    /**
     * 对跨域提供支持
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletRequest.getHeader("Origin");
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers",
                httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
//        for (String urlMethod : Constant.METHOD_URL_SET) {
//            String[] split = urlMethod.split(":");
//            String url = httpServletRequest.getRequestURI();
//            String[] splitUrl = url.split("/");
//            String str = "/" + splitUrl[splitUrl.length - 1];
//            if (split[0].equals("/" + splitUrl[splitUrl.length - 1])
//                    && split[1].equals(httpServletRequest.getMethod())) {
//                return true;
//            }
//        }

        String authorization = getToken(request, response);
        if (ComUtil.isEmpty(authorization)) {
            response401(request, response, Constant.LoginMessage.UN_AUTHENTICATED);
            return false;
        }
        return super.preHandle(request, response);
    }


    private String getToken(ServletRequest request, ServletResponse response) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String authorization = httpServletRequest.getHeader(TOKEN);
        if (ComUtil.isEmpty(authorization)) {
            // 获取不到再从Parameter中拿
            authorization = httpServletRequest.getParameter(TOKEN);
            // 还是获取不到再从Cookie中拿
            if (ComUtil.isEmpty(authorization)) {
                Cookie[] cookies = httpServletRequest.getCookies();
                if (cookies != null) {
                    for (Cookie cookie : cookies) {
                        if (TOKEN.equals(cookie.getName())) {
                            authorization = cookie.getValue();
                            break;
                        }
                    }
                }
            }
        }

        return authorization;
    }


    /**
     * 将非法请求跳转到 /401
     */
    private void response401(ServletRequest req, ServletResponse resp, String message) {
        HttpServletResponse httpServletResponse = (HttpServletResponse) resp;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("msg", message);
        try {
            httpServletResponse.reset();
            httpServletResponse.setCharacterEncoding("UTF-8");
            httpServletResponse.setContentType("application/json;charset=UTF-8");
            httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
            ObjectMapper objectMapper = new ObjectMapper();
            httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpServletResponse.getWriter().write(objectMapper.writeValueAsString(map));
            httpServletResponse.getWriter().close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                httpServletResponse.getWriter().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
