package com.camelot.pmt.service.impl;

import com.camelot.pmt.model.SysUser;
import com.camelot.pmt.service.DomeService;
import com.camelot.pmt.utils.TokenUtil;
import org.springframework.stereotype.Service;

@Service
public class DomeServiceImpl implements DomeService {

    @Override
    public void test() {
        SysUser sysUser = TokenUtil.getUserFromToken();
        System.out.print(sysUser);
    }
}
