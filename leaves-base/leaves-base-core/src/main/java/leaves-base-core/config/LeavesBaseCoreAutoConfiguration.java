package com.github.ryanddu.config;

import com.github.ryanddu.utils.SpringContextHolder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * leaves-base-core自动配置类
 *
 * @author: ryan
 * @date: 2023/3/30 16:39
 **/
@Configuration
public class LeavesBaseCoreAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public SpringContextHolder springContextHolder() {
		return new SpringContextHolder();
	}

}
