package com.github.ryanddu.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web Mvc 全局配置
 *
 * @author: ryan
 * @date: 2023/3/29 10:09
 **/
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	@Bean
	@ConditionalOnMissingBean(RequestContextListener.class)
	public RequestContextListener requestContextListener() {
		return new RequestContextListener();
	}

	/**
	 * 为了在统一异常处理能捕获到404,在application.yml中设置了throw-exception-if-no-handler-found=true和add-mappings=false,
	 * 这将会导致swagger不能使用，解决方案是添加如下代码
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**").addResourceLocations("classpath:/META-INF/resources/").setCachePeriod(0);
	}

}
