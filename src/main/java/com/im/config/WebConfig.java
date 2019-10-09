package com.im.config;

import java.io.IOException;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

@Configuration
@EnableWebMvc
@ComponentScan(value="com.im.controller")

public class WebConfig extends WebMvcConfigurerAdapter {

	@Autowired
	private PropertyConfig propertyConfig;

	/**
	 * 配置视图解析器，注册为spring context的bean
	 * 
	 * @return
	 */
	@Bean
	public ViewResolver viewResolver() {
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		// 设置前缀
		resolver.setPrefix(propertyConfig.getWebViewPrefix());
		// 设置后缀
		resolver.setSuffix(propertyConfig.getWebViewSuffix());
		// 将视图解析为JstlView
		resolver.setViewClass(JstlView.class);

		return resolver;

	}

	/**
	 * 配置multipart解析器，主要服务于文件上传
	 * 
	 * @return
	 * @throws IOException
	 */
	@Bean
	public MultipartResolver multipartResolver() throws IOException {
		CommonsMultipartResolver resolver = new CommonsMultipartResolver();
		// 设置暂存路径
		resolver.setUploadTempDir(new FileSystemResource("/tmp/CloudServerApp/upload"));
		// 设置最大上传文件size=100MB
		resolver.setMaxUploadSize(104857600);
		// 设置最大内存容量，如果超过该内存容量就会写到临时文件路径，设为0表示立刻写到临时文件路径
		resolver.setMaxInMemorySize(0);
		resolver.setResolveLazily(true);
		// 默认编码utf-8
		resolver.setDefaultEncoding("utf-8");

		return resolver;
	}

	/**
	 * 设置统一错误处理要跳转的视图
	 * 
	 * @return
	 */
	public SimpleMappingExceptionResolver exceptionResolver() {
		SimpleMappingExceptionResolver resolver = new SimpleMappingExceptionResolver();
		Properties properties = new Properties();

		properties.getProperty("java.lang.Exception", "error");
		resolver.setExceptionMappings(properties);

		return resolver;
	}

	/**
	 * 配置静态资源的处理，要求diapatcherServlet将对静态资源的请求转发到servlet容器中默认的servlet上
	 */
	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

//	/**
//	 * 添加静态资源
//	 */
//	@Override
//	public void addResourceHandlers(ResourceHandlerRegistry registry) {
//		registry.addResourceHandler(propertyConfig.getWebStaticHandler()).
//		addResourceLocations(propertyConfig.getWebStaticResource()).
//		setCachePeriod(propertyConfig.getWebStaticCachedPeriod());
//	}

	/**
	 * 添加拦截器
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		super.addInterceptors(registry);
	}
	

}
