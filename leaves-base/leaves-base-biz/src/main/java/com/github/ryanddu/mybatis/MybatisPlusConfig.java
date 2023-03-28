package com.github.ryanddu.mybatis;

import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.SqlExplainInterceptor;
import com.github.ryanddu.constant.EnvConstant;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * Mybatis Plus配置类
 * @author: ryan
 * @date: 2023/3/28 10:28
 **/
@Configuration
@EnableTransactionManagement
@ConditionalOnBean({DataSource.class})
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
public class MybatisPlusConfig {

	/**
	 * 分页插件，自动识别数据库类型
	 */
	@Bean
	public PaginationInterceptor paginationInterceptor() {
		return new PaginationInterceptor();
	}

	/**
	 * 乐观锁插件
	 */
	@Bean
	public OptimisticLockerInterceptor optimisticLockerInterceptor() {
		return new OptimisticLockerInterceptor();
	}

	/**
	 * 执行分析插件
	 */
	@Bean
	@Profile({ EnvConstant.LOCAL, EnvConstant.DEV, EnvConstant.TEST })
	public SqlExplainInterceptor sqlExplainInterceptor() {
		return new SqlExplainInterceptor();
	}
}
