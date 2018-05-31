package com.camelot.pmt.controller;

import com.camelot.pmt.model.SysUser;
import com.camelot.pmt.service.DomeService;
import com.camelot.pmt.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by daiyang on 2018/5/4.
 */
@RestController
@RequestMapping("/demo")
public class DomeController {

    @Autowired
    private DomeService domoService;

    @GetMapping("/zhangao")
    public ResponseEntity<String> dome() {
        SysUser sysUser = TokenUtil.getUserFromToken();
        domoService.test();
        return ResponseEntity.ok("请求成功" + sysUser);
    }
}