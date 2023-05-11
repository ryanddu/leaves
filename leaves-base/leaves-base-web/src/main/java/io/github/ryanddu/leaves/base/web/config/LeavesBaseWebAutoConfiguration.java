package io.github.ryanddu.leaves.base.web.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextListener;

import java.nio.charset.Charset;
import java.util.List;

/**
 * leaves-base-web自动配置类
 *
 * @author: ryan
 * @date: 2023/3/30 16:05
 **/
@Configuration
public class LeavesBaseWebAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		List<HttpMessageConverter<?>> list = restTemplate.getMessageConverters();
		// 中文乱码:遍历编码规则，如果遇到字符串编码规则，那么则替换成utf-8
		for (HttpMessageConverter<?> httpMessageConverter : list) {
			if (httpMessageConverter instanceof StringHttpMessageConverter) {
				((StringHttpMessageConverter) httpMessageConverter).setDefaultCharset(Charset.forName("utf-8"));
				break;
			}
		}
		return restTemplate;
	}

	@Bean
	@ConditionalOnMissingBean
	public RequestContextListener requestContextListener() {
		return new RequestContextListener();
	}

	@Bean
	@ConditionalOnMissingBean
	public WebMvcConfig webMvcConfig() {
		return new WebMvcConfig();
	}

}
