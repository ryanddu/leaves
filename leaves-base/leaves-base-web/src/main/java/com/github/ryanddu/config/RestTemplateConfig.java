package com.github.ryanddu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.List;

/**
 * RestTemplate配置
 *
 * @author: ryan
 * @date: 2023/3/29 10:07
 **/
@Configuration
public class RestTemplateConfig {

	@Bean
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

}
