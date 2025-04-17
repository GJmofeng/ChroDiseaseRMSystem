package com.gxwz.syschronicdisease;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.gxwz.syschronicdisease.mapper")
public class SysChronicDiseaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(SysChronicDiseaseApplication.class, args);
    }

}
