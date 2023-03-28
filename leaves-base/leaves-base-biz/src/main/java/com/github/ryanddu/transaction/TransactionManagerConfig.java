
package com.github.ryanddu.transaction;

import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import javax.annotation.Resource;
import java.util.Properties;

/**
 * 事务管理配置类
 * @author: ryan
 * @date: 2023/3/28 10:40
 **/
@Configuration
public class TransactionManagerConfig {

    public static final String transactionExecution = "execution(* com.github.ryanddu..service..*(..))";
    @Resource
    private PlatformTransactionManager transactionManager;

    @Bean
    public DefaultPointcutAdvisor defaultPointcutAdvisor() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(transactionExecution);
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor();
        advisor.setPointcut(pointcut);
        Properties attributes = new Properties();
        attributes.setProperty("get*", "PROPAGATION_SUPPORTS,-Exception,readOnly");
        attributes.setProperty("select*", "PROPAGATION_SUPPORTS,-Exception,readOnly");
        attributes.setProperty("list*", "PROPAGATION_SUPPORTS,-Exception,readOnly");
        attributes.setProperty("page*", "PROPAGATION_SUPPORTS,-Exception,readOnly");
        attributes.setProperty("find*", "PROPAGATION_SUPPORTS,-Exception,readOnly");
        attributes.setProperty("has*", "PROPAGATION_SUPPORTS,-Exception,readOnly");
        attributes.setProperty("locate*", "PROPAGATION_REQUIRED,-Exception,readOnly");
        attributes.setProperty("register*", "PROPAGATION_REQUIRED,-Exception");
        attributes.setProperty("save*", "PROPAGATION_REQUIRED,-Exception");
        attributes.setProperty("insert*", "PROPAGATION_REQUIRED,-Exception");
        attributes.setProperty("update*", "PROPAGATION_REQUIRED,-Exception");
        attributes.setProperty("batch*", "PROPAGATION_REQUIRED,-Exception");
        attributes.setProperty("do*", "PROPAGATION_REQUIRED,-Exception");
        attributes.setProperty("lock*", "PROPAGATION_REQUIRED,-Exception");
        attributes.setProperty("add*", "PROPAGATION_REQUIRED,-Exception");
        attributes.setProperty("del*", "PROPAGATION_REQUIRED,-Exception");
        attributes.setProperty("modify*", "PROPAGATION_REQUIRED,-Exception");
        attributes.setProperty("auth*", "PROPAGATION_REQUIRED,-Exception");
        TransactionInterceptor txAdvice = new TransactionInterceptor(transactionManager, attributes);
        advisor.setAdvice(txAdvice);
        return advisor;
    }


}