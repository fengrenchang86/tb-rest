package com.turtlebone.rest;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;


@SpringBootApplication(scanBasePackages = "com.turtlebone")
@EnableScheduling
@EnableAutoConfiguration
@EnableAspectJAutoProxy(proxyTargetClass=true)
@EnableDubboConfiguration
public class RestApp {
	public static void main(String[] args) {
		SpringApplication.run(RestApp.class);
	}
}
