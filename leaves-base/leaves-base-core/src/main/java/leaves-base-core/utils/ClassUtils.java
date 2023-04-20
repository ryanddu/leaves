package io.github.ryanddu.utils;

import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.io.Resources;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.SynthesizingMethodParameter;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.web.method.HandlerMethod;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.util.StringUtils.tokenizeToStringArray;

/**
 * 类工具类
 *
 * @author: ryan
 * @date: 2023/3/27 9:43
 **/
@Slf4j
public class ClassUtils extends org.springframework.util.ClassUtils {

	private final ParameterNameDiscoverer PARAMETERNAMEDISCOVERER = new DefaultParameterNameDiscoverer();

	private static final ResourcePatternResolver RESOURCE_PATTERN_RESOLVER = new PathMatchingResourcePatternResolver();
	private static final MetadataReaderFactory METADATA_READER_FACTORY = new CachingMetadataReaderFactory();

	/**
	 * 获取方法参数信息
	 * @param constructor 构造器
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
	 * @param method 方法
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
	 * @param method Method
	 * @param annotationType 注解类
	 * @param <A> 泛型标记
	 * @return {Annotation}
	 */
	public <A extends Annotation> A getAnnotation(Method method, Class<A> annotationType) {
		Class<?> targetClass = method.getDeclaringClass();
		// The method may be on an interface, but we need attributes from the target
		// class.
		// If the target class is null, the method will be unchanged.
		Method specificMethod = ClassUtils.getMostSpecificMethod(method, targetClass);
		// If we are dealing with method with generic parameters, find the original
		// method.
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
	 * @param handlerMethod HandlerMethod
	 * @param annotationType 注解类
	 * @param <A> 泛型标记
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
	 * 给对象的字段中设置值 LocalDate 和 LocalDateTime 会转成 date
	 * @param object 对象
	 * @param field 字段
	 * @param value 值
	 */
	public static void setFieldValue(Object object, Field field, Object value) {
		Class<?> fieldType = field.getType();
		if (fieldType.equals(LocalDate.class)) {
			ReflectUtil.setFieldValue(object, field, value);
		}
		else if (fieldType.equals(LocalDateTime.class)) {
			Date date = (Date) value;
			LocalDateTime localDateTime = DateUtils.defaultDateToLocalDateTime(date);
			ReflectUtil.setFieldValue(object, field, localDateTime);
		}
		else {
			ReflectUtil.setFieldValue(object, field, value);
		}
	}

	/**
	 * 获取字段的值 LocalDate 和 LocalDateTime 会转成 date
	 * @param object 对象
	 * @param field 字段
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
		}
		else if (fieldType.equals(LocalDateTime.class)) {
			LocalDateTime date = (LocalDateTime) fieldValue;
			return DateUtils.defaultLocalDateTimeToDate(date);
		}
		else {
			return fieldValue;
		}
	}

	private static Set<Class<?>> scanClasses(String packagePatterns, Class<?> assignableType) throws IOException {
		Set<Class<?>> classes = new HashSet<>();
		String[] packagePatternArray = tokenizeToStringArray(packagePatterns,
				ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);
		for (String packagePattern : packagePatternArray) {
			Resource[] resources = RESOURCE_PATTERN_RESOLVER.getResources(ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
					+ org.springframework.util.ClassUtils.convertClassNameToResourcePath(packagePattern) + "/**/*.class");
			for (Resource resource : resources) {
				try {
					ClassMetadata classMetadata = METADATA_READER_FACTORY.getMetadataReader(resource).getClassMetadata();
					Class<?> clazz = Resources.classForName(classMetadata.getClassName());
					if (assignableType == null || assignableType.isAssignableFrom(clazz)) {
						classes.add(clazz);
					}
				} catch (Throwable e) {
					log.warn("Cannot load the '{}'. Cause by ", resource, e);
				}
			}
		}
		return classes;
	}

	/**
	 * 扫面包路径下满足class过滤器条件的所有class文件，<br>
	 * 如果包路径为 com.abs + A.class 但是输入 abs会产生classNotFoundException<br>
	 * 因为className 应该为 com.abs.A 现在却成为abs.A,此工具类对该异常进行忽略处理,有可能是一个不完善的地方，以后需要进行修改<br>
	 *
	 * @param packageName 包路径 com.abs | com.abs.* | com.abs,com.xxx;com.ddd
	 * @param classFilter class过滤器，过滤掉不需要的class
	 * @return 类集合
	 */
	@SneakyThrows
	public static Set<Class<?>> scanPackage(String packageName, Predicate<Class<?>> classFilter) {
		if (StringUtils.isBlank(packageName)) {
			return Collections.emptySet();
		}
		Set<Class<?>> classes;

		if (packageName.contains(StringPool.STAR) && !packageName.contains(StringPool.COMMA)
				&& !packageName.contains(StringPool.SEMICOLON)) {
			classes = scanClasses(packageName, null);
		} else {
			classes = new HashSet<>();
			String[] packageNameArray = tokenizeToStringArray(packageName, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);
			Assert.notNull(packageNameArray, "not find packageName:" + packageName);
			Stream.of(packageNameArray).forEach(typePackage -> {
				try {
					Set<Class<?>> scanTypePackage = scanClasses(typePackage, null);
					classes.addAll(scanTypePackage);
				} catch (IOException e) {
					throw new MybatisPlusException("Cannot scan class in '[" + typePackage + "]' package", e);
				}
			});
		}
		return classes.stream().filter(classFilter).collect(Collectors.toSet());
	}

}
