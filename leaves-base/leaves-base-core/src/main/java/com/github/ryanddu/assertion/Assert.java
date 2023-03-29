package com.github.ryanddu.assertion;

import com.github.ryanddu.exception.LeavesException;

import java.util.Collection;
import java.util.Map;

/**
 * 异常断言基类
 *
 * @author: ryan
 * @date: 2023/3/24 18:14
 **/
public interface Assert<T extends LeavesException> {

	/**
	 * 通过异常消息创建异常
	 * @param message 异常消息
	 * @return 异常对象
	 */
	default T newException(String message) {
		throw new RuntimeException("未找到异常断言实现");
	}

	/**
	 * 通过其他异常创建异常
	 * @param cause 其他异常
	 * @return 异常对象
	 */
	default T newException(Throwable cause) {
		throw new RuntimeException("未找到异常断言实现");
	}

	/**
	 * 通过异常消息加其他异常创建异常
	 * @param message 异常消息
	 * @param cause 其他异常
	 * @return 异常对象
	 */
	default T newException(String message, Throwable cause) {
		throw new RuntimeException("未找到异常断言实现");
	}

	/**
	 * 通过异常code创建异常
	 * @param code 异常code
	 * @return 异常对象
	 */
	default T newException(Integer code) {
		throw new RuntimeException("未找到异常断言实现");
	}

	/**
	 * 通过异常code加异常参数创建异常
	 * @param code 异常code
	 * @param args 异常参数
	 * @return 异常对象
	 */
	default T newException(Integer code, Object... args) {
		throw new RuntimeException("未找到异常断言实现");
	}

	/**
	 * <p>
	 * 断言对象<code>obj</code>非空。如果对象<code>obj</code>为空，则抛出异常
	 * @param obj 待判断对象
	 */
	default void assertNotNull(Object obj, Integer code) {
		if (obj == null) {
			throw newException(code);
		}
	}

	/**
	 * <p>
	 * 断言对象<code>obj</code>非空。如果对象<code>obj</code>为空，则抛出异常
	 * <p>
	 * 异常信息<code>message</code>支持传递参数方式，避免在判断之前进行字符串拼接操作
	 * @param obj 待判断对象
	 * @param args message占位符对应的参数列表
	 */
	default void assertNotNull(Object obj, Integer code, Object... args) {
		if (obj == null) {
			throw newException(code, args);
		}
	}

	/**
	 * <p>
	 * 断言字符串<code>str</code>不为空串（长度为0）。如果字符串<code>str</code>为空串，则抛出异常
	 * @param str 待判断字符串
	 */
	default void assertNotEmpty(Integer code, String str) {
		if (null == str || "".equals(str.trim())) {
			throw newException(code);
		}
	}

	/**
	 * <p>
	 * 断言字符串<code>str</code>不为空串（长度为0）。如果字符串<code>str</code>为空串，则抛出异常
	 * <p>
	 * 异常信息<code>message</code>支持传递参数方式，避免在判断之前进行字符串拼接操作
	 * @param str 待判断字符串
	 * @param args message占位符对应的参数列表
	 */
	default void assertNotEmpty(String str, Integer code, Object... args) {
		if (str == null || "".equals(str.trim())) {
			throw newException(code, args);
		}
	}

	/**
	 * <p>
	 * 断言数组<code>arrays</code>大小不为0。如果数组<code>arrays</code>大小不为0，则抛出异常
	 * @param arrays 待判断数组
	 */
	default void assertNotEmpty(Object[] arrays, Integer code) {
		if (arrays == null || arrays.length == 0) {
			throw newException(code);
		}
	}

	/**
	 * <p>
	 * 断言数组<code>arrays</code>大小不为0。如果数组<code>arrays</code>大小不为0，则抛出异常
	 * <p>
	 * 异常信息<code>message</code>支持传递参数方式，避免在判断之前进行字符串拼接操作
	 * @param arrays 待判断数组
	 * @param args message占位符对应的参数列表
	 */
	default void assertNotEmpty(Object[] arrays, Integer code, Object... args) {
		if (arrays == null || arrays.length == 0) {
			throw newException(code);
		}
	}

	/**
	 * <p>
	 * 断言集合<code>c</code>大小不为0。如果集合<code>c</code>大小不为0，则抛出异常
	 * @param c 待判断数组
	 */
	default void assertNotEmpty(Collection<?> c, Integer code) {
		if (c == null || c.isEmpty()) {
			throw newException(code);
		}
	}

	/**
	 * <p>
	 * 断言集合<code>c</code>大小不为0。如果集合<code>c</code>大小不为0，则抛出异常
	 * @param c 待判断数组
	 * @param args message占位符对应的参数列表
	 */
	default void assertNotEmpty(Collection<?> c, Integer code, Object... args) {
		if (c == null || c.isEmpty()) {
			throw newException(code, args);
		}
	}

	/**
	 * <p>
	 * 断言Map<code>map</code>大小不为0。如果Map<code>map</code>大小不为0，则抛出异常
	 * @param map 待判断Map
	 */
	default void assertNotEmpty(Map<?, ?> map, Integer code) {
		if (map == null || map.isEmpty()) {
			throw newException(code);
		}
	}

	/**
	 * <p>
	 * 断言Map<code>map</code>大小不为0。如果Map<code>map</code>大小不为0，则抛出异常
	 * @param map 待判断Map
	 * @param args message占位符对应的参数列表
	 */
	default void assertNotEmpty(Map<?, ?> map, Integer code, Object... args) {
		if (map == null || map.isEmpty()) {
			throw newException(code, args);
		}
	}

	/**
	 * <p>
	 * 断言布尔值<code>expression</code>为false。如果布尔值<code>expression</code>为true，则抛出异常
	 * @param expression 待判断布尔变量
	 */
	default void assertIsFalse(boolean expression, Integer code) {
		if (expression) {
			throw newException(code);
		}
	}

	/**
	 * <p>
	 * 断言布尔值<code>expression</code>为false。如果布尔值<code>expression</code>为true，则抛出异常
	 * @param expression 待判断布尔变量
	 * @param args message占位符对应的参数列表
	 */
	default void assertIsFalse(boolean expression, Integer code, Object... args) {
		if (expression) {
			throw newException(code, args);
		}
	}

	/**
	 * <p>
	 * 断言布尔值<code>expression</code>为true。如果布尔值<code>expression</code>为false，则抛出异常
	 * @param expression 待判断布尔变量
	 */
	default void assertIsTrue(boolean expression, Integer code) {
		if (!expression) {
			throw newException(code);
		}
	}

	/**
	 * <p>
	 * 断言布尔值<code>expression</code>为true。如果布尔值<code>expression</code>为false，则抛出异常
	 * @param expression 待判断布尔变量
	 * @param args message占位符对应的参数列表
	 */
	default void assertIsTrue(boolean expression, Integer code, Object... args) {
		if (!expression) {
			throw newException(code, args);
		}
	}

	/**
	 * <p>
	 * 断言对象<code>obj</code>为<code>null</code>。如果对象<code>obj</code>不为<code>null</code>，则抛出异常
	 * @param obj 待判断对象
	 */
	default void assertIsNull(Object obj, Integer code) {
		if (obj != null) {
			throw newException(code);
		}
	}

	/**
	 * <p>
	 * 断言对象<code>obj</code>为<code>null</code>。如果对象<code>obj</code>不为<code>null</code>，则抛出异常
	 * @param obj 待判断布尔变量
	 * @param args message占位符对应的参数列表
	 */
	default void assertIsNull(Object obj, Integer code, Object... args) {
		if (obj != null) {
			throw newException(code);
		}
	}

	/**
	 * <p>
	 * 直接抛出异常
	 */
	default void assertFail(Integer code) {
		throw newException(code);
	}

	/**
	 * <p>
	 * 直接抛出异常
	 * @param args message占位符对应的参数列表
	 */
	default void assertFail(Integer code, Object... args) {
		throw newException(code);
	}

	/**
	 * <p>
	 * 直接抛出异常，并包含原异常信息
	 * <p>
	 * 当捕获非运行时异常（非继承{@link RuntimeException}）时，并该异常进行业务描述时， 必须传递原始异常，作为新异常的cause
	 * @param t 原始异常
	 */
	default void assertFail(Throwable t) {
		throw newException(t);
	}

	/**
	 * <p>
	 * 断言对象<code>o1</code>与对象<code>o2</code>相等，此处的相等指（o1.equals(o2)为true）。 如果两对象不相等，则抛出异常
	 * @param o1 待判断对象，若<code>o1</code>为null，也当作不相等处理
	 * @param o2 待判断对象
	 */
	default void assertEquals(Object o1, Object o2, Integer code) {
		if (o1 == o2) {
			return;
		}
		if (o1 == null) {
			throw newException(code);
		}
		if (!o1.equals(o2)) {
			throw newException(code);
		}
	}

	/**
	 * <p>
	 * 断言对象<code>o1</code>与对象<code>o2</code>相等，此处的相等指（o1.equals(o2)为true）。 如果两对象不相等，则抛出异常
	 * @param o1 待判断对象，若<code>o1</code>为null，也当作不相等处理
	 * @param o2 待判断对象
	 * @param args message占位符对应的参数列表
	 */
	default void assertEquals(Object o1, Object o2, Integer code, Object... args) {
		if (o1 == o2) {
			return;
		}
		if (o1 == null) {
			throw newException(code, args);
		}
		if (!o1.equals(o2)) {
			throw newException(code, args);
		}
	}

}
