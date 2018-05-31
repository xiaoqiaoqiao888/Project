package com.camelot.pmt;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author daiyang
 * @date 2018/5/4
 */
@Slf4j
@SpringBootApplication
@EnableTransactionManagement
@EnableScheduling
@MapperScan("com.camelot.pmt.mapper")
public class PmtApplication {
    public static void main(String[] args) {
        SpringApplication.run(PmtApplication.class, args);
    }
}
