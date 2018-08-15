package com.camelot.pmt.shiro.interceptor;

import com.camelot.pmt.utils.RequestResponseContext;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RequestInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object obj)
            throws Exception {
        RequestResponseContext.setResponse(httpServletResponse);
        RequestResponseContext.setRequest(httpServletRequest);
        return super.preHandle(httpServletRequest, httpServletResponse, obj);
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object obj,
            ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
            Object obj, Exception exception) throws Exception {

    }
}
