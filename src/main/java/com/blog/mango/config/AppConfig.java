package com.blog.mango.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

/**
 * bean 자바 환경설정 방식
 */
@Configuration
@ComponentScan("com.blog.mango")
public class AppConfig {
	@Value("${datasource.driverClassName:}")
	private String driverClassName;
	
	@Value("${datasource.url:}")
	private String url;
	
	@Value("${datasource.username:}")
	private String username;
	
	@Value("${datasource.password:}")
	private String password;
	
//	@Bean
//	DataSource dataSource() {
//		SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
//		dataSource.setDriverClass(org.mariadb.jdbc.Driver.class);
//		dataSource.setUrl(url);
//		dataSource.setUsername(username);
//		dataSource.setPassword(password);
//		System.out.println("url: "+ url);
//		System.out.println("username: "+ username);
//		System.out.println("password: "+ password);
//		return dataSource;
//	}
}
