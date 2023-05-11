package io.github.ryanddu.leaves.base.biz.mybatis;

import com.baomidou.mybatisplus.extension.plugins.inner.IllegalSQLInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import io.github.ryanddu.leaves.base.core.constant.EnvConstant;
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
 *
 * @author: ryan
 * @date: 2023/3/28 10:28
 **/
@Configuration
@EnableTransactionManagement
@ConditionalOnBean({ DataSource.class })
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
public class MybatisPlusConfig {

	/**
	 * 分页插件，自动识别数据库类型
	 */
	@Bean
	public PaginationInnerInterceptor paginationInnerInterceptor() {
		return new PaginationInnerInterceptor();
	}

	/**
	 * 乐观锁插件
	 */
	@Bean
	public OptimisticLockerInnerInterceptor optimisticLockerInnerInterceptor() {
		return new OptimisticLockerInnerInterceptor();
	}

	/**
	 * sql性能规范插件
	 */
	@Bean
	@Profile({ EnvConstant.LOCAL, EnvConstant.DEV, EnvConstant.TEST })
	public IllegalSQLInnerInterceptor illegalSQLInnerInterceptor() {
		return new IllegalSQLInnerInterceptor();
	}

}
