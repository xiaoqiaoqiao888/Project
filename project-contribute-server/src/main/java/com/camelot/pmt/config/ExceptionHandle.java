package com.camelot.pmt.config;

import com.auth0.jwt.exceptions.InvalidClaimException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

/**
 * Created by daiyang on 2018/5/8.
 */
@ControllerAdvice
public class ExceptionHandle {

    private static final Logger log = LoggerFactory.getLogger(ExceptionHandle.class);

    @ExceptionHandler({SQLException.class, Exception.class, JsonMappingException.class, ShiroException.class,
            UnauthorizedException.class})
    final ResponseEntity<Object> handleControllerApiException(HttpServletRequest request, Throwable ex) {
        HttpHeaders headers = new HttpHeaders();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        if (ex instanceof HttpMessageNotReadableException) {
            HttpMessageNotReadableException httpMessageNotReadableException = (HttpMessageNotReadableException) ex;
            log.error("HTTP信息异常：%s", httpMessageNotReadableException);
            return new ResponseEntity<>("HTTP信息异常：" + ex.getMessage(), headers, status);
        }

        if (ex instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException argumentNotValidException = (MethodArgumentNotValidException) ex;
            log.error("参数校验异常：%s", argumentNotValidException);
            return new ResponseEntity<>("参数校验异常：" + ex.getMessage(), headers, status);
        }

        if (ex instanceof SQLException) {
            SQLException sqlException = (SQLException) ex;
            log.error("SQL执行异常：%s", sqlException);
            return new ResponseEntity<>("SQL执行异常：" + sqlException.getMessage(), headers, status);
        }

        // 业务逻辑异常
        if (ex instanceof IllegalArgumentException) {
            IllegalArgumentException illegalArgumentException = (IllegalArgumentException) ex;
            log.error("业务异常： %s", illegalArgumentException);
            return new ResponseEntity<>("业务异常：" + illegalArgumentException.getMessage(), headers, status);
        }
        // 捕捉shiro的异常
        if (ex instanceof ShiroException) {
            ShiroException shiroException = (ShiroException) ex;
            log.error("认证异常： %s", shiroException);
            return new ResponseEntity<>("登录认证异常：" + shiroException.getMessage(), headers,
                    HttpStatus.UNAUTHORIZED);
        }
        // 授权异常 捕捉UnauthorizedException
        if (ex instanceof UnauthorizedException) {
            UnauthorizedException unauthorizedException = (UnauthorizedException) ex;
            log.error("非法授权异常： %s", unauthorizedException);
            return new ResponseEntity<>("非法授权异常：" + unauthorizedException.getMessage(), headers,
                    HttpStatus.NOT_ACCEPTABLE);
        }
        if (ex instanceof HttpRequestMethodNotSupportedException) {
            HttpRequestMethodNotSupportedException httpRequestMethodNotSupportedException
                    = (HttpRequestMethodNotSupportedException) ex;
            log.error("HTTP信息异常：%s", httpRequestMethodNotSupportedException);
            return new ResponseEntity<>("HTTP请求方式错误：" + ex.getMessage(), headers, status);
        }
        if (ex instanceof InvalidClaimException) {
            HttpRequestMethodNotSupportedException httpRequestMethodNotSupportedException
                    = (HttpRequestMethodNotSupportedException) ex;
            log.error("HTTP信息异常：%s", httpRequestMethodNotSupportedException);
            return new ResponseEntity<>("token已过期：" + ex.getMessage(), headers, status);
        }
        if (ex instanceof BindException) {
            return new ResponseEntity<>(((BindException) ex).getBindingResult()
                    .getFieldError().getDefaultMessage(), headers, status);
        }

        Exception exception = (Exception) ex;
        log.error("其他异常：%s", exception);
        return new ResponseEntity<>("其他异常", headers, HttpStatus.NOT_ACCEPTABLE);
    }
}
