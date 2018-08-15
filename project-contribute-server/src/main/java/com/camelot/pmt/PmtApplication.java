package com.camelot.pmt;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 
 * @author DELL
 *
 */
@SpringBootApplication
@EnableTransactionManagement
@EnableScheduling
@MapperScan("com.camelot.pmt.mapper")
public class PmtApplication {
    public static void main(String[] args) {
        SpringApplication.run(PmtApplication.class, args);
    }
}
