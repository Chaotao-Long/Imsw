package com.im.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * 配置文件application.properties的java对象
 * 
 * @author Kevin-
 *
 */

@Configuration
@PropertySource("classpath:application.properties")
public class PropertyConfig {

	@Value("${spring.datasource.url}")
	private String url;
	@Value("${spring.datasource.driver}")
	private String driver;
	@Value("${spring.datasource.user}")
	private String user;
	@Value("${spring.datasource.password}")
	private String password;
	@Value("${spring.datasource.initialSize}")
	private int initialSize;
	@Value("${spring.datasource.maxActive}")
	private int maxActive;
	@Value("${spring.datasource.maxIdle}")
	private int maxIdle;
	@Value("${spring.datasource.minIdle}")
	private int minIdle;
	@Value("${spring.datasource.maxWait}")
	private long maxWait;
	@Value("${spring.web.view.prefix}")
	private String webViewPrefix;
	@Value("${spring.web.view.suffix}")
	private String webViewSuffix;
	@Value("${spring.web.static.handler}")
	private String webStaticHandler;
	@Value("${spring.web.static.resource}")
	private String webStaticResource;
	@Value("${spring.web.static.cache.period}")
	private Integer webStaticCachedPeriod;
	@Value("${mybatis.type.alias.package}")
	private String aliasPackage;
	@Value("${mybatis.mapper.locations}")
	private String mapperLocations;

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	public String getUrl() {
		return url;
	}

	public String getDriver() {
		return driver;
	}

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}

	public int getInitialSize() {
		return initialSize;
	}

	public int getMaxActive() {
		return maxActive;
	}

	public int getMaxIdle() {
		return maxIdle;
	}

	public int getMinIdle() {
		return minIdle;
	}

	public long getMaxWait() {
		return maxWait;
	}

	public String getWebViewPrefix() {
		return webViewPrefix;
	}

	public String getWebViewSuffix() {
		return webViewSuffix;
	}

	public String getWebStaticHandler() {
		return webStaticHandler;
	}

	public String getWebStaticResource() {
		return webStaticResource;
	}

	public Integer getWebStaticCachedPeriod() {
		return webStaticCachedPeriod;
	}

	public String getAliasPackage() {
		return aliasPackage;
	}
	
	public String getMapperLocations() {
		return mapperLocations;
	}

}
