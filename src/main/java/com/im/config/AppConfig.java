package com.im.config;

import java.io.IOException;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.im.util.SpringUtil;



/**
 * 配置数据源，创建Spring和Mybaits整合的相关对象，创建声明式事务切面
 * 
 * @author Kevin-
 *
 */

@Configuration // 表明此类是配置类
@ComponentScan(value="com.im") // 扫描自定义组件(repository service component controller)

@PropertySource("classpath:application.properties") // 读取application.properties
@MapperScan("com.im.dao") // 扫描Mybatis的Mapper接口
@EnableTransactionManagement // 开启事务管理
@EnableCaching //开启缓存
@EnableScheduling

public class AppConfig {

	/**
	 * 设置数据源
	 * 
	 * @param propertyConfig
	 * @return
	 */
	@Bean
	public DataSource dataSource(PropertyConfig propertyConfig) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(propertyConfig.getDriver());
		dataSource.setUsername(propertyConfig.getUser());
		dataSource.setPassword(propertyConfig.getPassword());
		dataSource.setUrl(propertyConfig.getUrl());
		dataSource.setInitialSize(propertyConfig.getInitialSize());
		dataSource.setMaxActive(propertyConfig.getMaxActive());
		dataSource.setMaxIdle(propertyConfig.getMaxIdle());
		dataSource.setMinIdle(propertyConfig.getMinIdle());
		dataSource.setMaxWait(propertyConfig.getMaxWait());

		return dataSource;
	}

	/**
	 * 配置mybaits的sqlSessionFactoryBean，用于spring与mybaits的整合
	 * 
	 * @param dataSource
	 * @param propertyConfig
	 * @return
	 * @throws IOException
	 */
	@Bean
	public SqlSessionFactoryBean sqlSessionFactoryBean(DataSource dataSource, PropertyConfig propertyConfig)
			throws IOException {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();

		sqlSessionFactoryBean.setDataSource(dataSource);
		sqlSessionFactoryBean.setTypeAliasesPackage(propertyConfig.getAliasPackage());

		PathMatchingResourcePatternResolver classPathResource = new PathMatchingResourcePatternResolver();
		sqlSessionFactoryBean.setMapperLocations(classPathResource.getResources(propertyConfig.getMapperLocations()));

		return sqlSessionFactoryBean;

	}

	/**
	 * 配置spring的声明式事务
	 * 
	 * @param dataSource
	 * @return
	 */
	@Bean
	public PlatformTransactionManager transactionManager(DataSource dataSource) {
		DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager(dataSource);
		return dataSourceTransactionManager;
	}

	/**
	 * 
	 * @return
	 */
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
		return configurer;
	}
	
	/**
	 * 配置缓存管理器
	 * 
	 * @param 
	 * @return
	 */
	@Bean
	public CacheManager cacheManager() { //声明缓存管理器
		return new ConcurrentMapCacheManager();
	}
	
	/**注册Spring Util
     * 这里为了和上一个冲突，所以方面名为：springUtil2
     * 实际中使用springUtil
     */
    @Bean
    public SpringUtil springUtil2(){
        return new SpringUtil();
        }


}
    

