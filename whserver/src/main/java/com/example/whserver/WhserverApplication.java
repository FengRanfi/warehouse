package com.example.whserver;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//mapper接口扫描器,指明mapper接口所在包,然后就会自动为mapper接口创建代理对象并加入到IOC容器
@MapperScan(basePackages = "com.example.whserver.mapper")
@SpringBootApplication
public class WhserverApplication {


	public static void main(String[] args) {
		SpringApplication.run(WhserverApplication.class, args);
	}

}
