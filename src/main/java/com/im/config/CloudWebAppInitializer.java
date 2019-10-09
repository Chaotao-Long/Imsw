package com.im.config;

import javax.servlet.Filter;

import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * 配置spring的dispatcherServlet，此类替代了web.xml的配置语句【还缺少log4j的配置】
 * 
 * @author Kevin-
 *
 */

public class CloudWebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	/**
	 * 将DispatcherServlet映射到“/”上
	 */
	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}

	/**
	 * 指定ContextLoaderListener的配置文件,配置其应用上下文context
	 */
	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[] { AppConfig.class };
		
	}

	/**
	 * 指定DispatherServlet的配置文件，配置其应用上下文
	 */
	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class[] { WebConfig.class };
	}

	/**
	 * 添加过滤器：编码过滤器
	 */
	@Override
	protected Filter[] getServletFilters() {
		CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
		encodingFilter.setEncoding("UTF-8");
		encodingFilter.setForceEncoding(true);
		return new Filter[] { encodingFilter };
	}
	
}
