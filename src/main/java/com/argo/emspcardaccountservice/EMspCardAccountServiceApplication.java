package com.argo.emspcardaccountservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.argo.emspcardaccountservice.mapper")
public class EMspCardAccountServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EMspCardAccountServiceApplication.class, args);
    }

}
