package com.github.ryanddu.utils;

import cn.hutool.core.util.ReflectUtil;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.SynthesizingMethodParameter;
import org.springframework.web.method.HandlerMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

/**
 * 类工具类
 * @author chenhuainian
 */
public class ClassUtils extends org.springframework.util.ClassUtils {

    private final ParameterNameDiscoverer PARAMETERNAMEDISCOVERER = new DefaultParameterNameDiscoverer();

    /**
     * 获取方法参数信息
     *
     * @param constructor    构造器
     * @param parameterIndex 参数序号
     * @return {MethodParameter}
     */
    public MethodParameter getMethodParameter(Constructor<?> constructor, int parameterIndex) {
        MethodParameter methodParameter = new SynthesizingMethodParameter(constructor, parameterIndex);
        methodParameter.initParameterNameDiscovery(PARAMETERNAMEDISCOVERER);
        return methodParameter;
    }

    /**
     * 获取方法参数信息
     *
     * @param method         方法
     * @param parameterIndex 参数序号
     * @return {MethodParameter}
     */
    public MethodParameter getMethodParameter(Method method, int parameterIndex) {
        MethodParameter methodParameter = new SynthesizingMethodParameter(method, parameterIndex);
        methodParameter.initParameterNameDiscovery(PARAMETERNAMEDISCOVERER);
        return methodParameter;
    }

    /**
     * 获取Annotation
     *
     * @param method         Method
     * @param annotationType 注解类
     * @param <A>            泛型标记
     * @return {Annotation}
     */
    public <A extends Annotation> A getAnnotation(Method method, Class<A> annotationType) {
        Class<?> targetClass = method.getDeclaringClass();
        // The method may be on an interface, but we need attributes from the target class.
        // If the target class is null, the method will be unchanged.
        Method specificMethod = ClassUtils.getMostSpecificMethod(method, targetClass);
        // If we are dealing with method with generic parameters, find the original method.
        specificMethod = BridgeMethodResolver.findBridgedMethod(specificMethod);
        // 先找方法，再找方法上的类
        A annotation = AnnotatedElementUtils.findMergedAnnotation(specificMethod, annotationType);
        ;
        if (null != annotation) {
            return annotation;
        }
        // 获取类上面的Annotation，可能包含组合注解，故采用spring的工具类
        return AnnotatedElementUtils.findMergedAnnotation(specificMethod.getDeclaringClass(), annotationType);
    }

    /**
     * 获取Annotation
     *
     * @param handlerMethod  HandlerMethod
     * @param annotationType 注解类
     * @param <A>            泛型标记
     * @return {Annotation}
     */
    public <A extends Annotation> A getAnnotation(HandlerMethod handlerMethod, Class<A> annotationType) {
        // 先找方法，再找方法上的类
        A annotation = handlerMethod.getMethodAnnotation(annotationType);
        if (null != annotation) {
            return annotation;
        }
        // 获取类上面的Annotation，可能包含组合注解，故采用spring的工具类
        Class<?> beanType = handlerMethod.getBeanType();
        return AnnotatedElementUtils.findMergedAnnotation(beanType, annotationType);
    }

    /**
     * 给对象的字段中设置值
     * LocalDate 和 LocalDateTime 会转成 date
     *
     * @param object 对象
     * @param field  字段
     * @param value  值
     */
    public static void setFieldValue(Object object, Field field, Object value) {
        Class<?> fieldType = field.getType();
        if (fieldType.equals(LocalDate.class)) {
            ReflectUtil.setFieldValue(object, field, value);
        } else if (fieldType.equals(LocalDateTime.class)) {
            Date date = (Date) value;
            LocalDateTime localDateTime = DateUtils.defaultDateToLocalDateTime(date);
            ReflectUtil.setFieldValue(object, field, localDateTime);
        } else {
            ReflectUtil.setFieldValue(object, field, value);
        }
    }


    /**
     * 获取字段的值
     * LocalDate 和 LocalDateTime 会转成 date
     *
     * @param object 对象
     * @param field  字段
     * @return
     */
    public static Object getFieldValue(Object object, Field field) {
        Object fieldValue = ReflectUtil.getFieldValue(object, field);
        if (Objects.isNull(fieldValue)) {
            return null;
        }
        Class<?> fieldType = field.getType();
        if (fieldType.equals(LocalDate.class)) {
            LocalDate date = (LocalDate) fieldValue;
            return DateUtils.localDateToDate(date);
        } else if (fieldType.equals(LocalDateTime.class)) {
            LocalDateTime date = (LocalDateTime) fieldValue;
            return DateUtils.defaultLocalDateTimeToDate(date);
        } else {
            return fieldValue;
        }
    }

}
