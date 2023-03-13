package com.github.ryanddu.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.*;


/**
 * Jackson 工具类
 * @author chenhuainian
 */
public abstract class JacksonUtils {

	private final static ObjectMapper objectMapper;

	static {
		objectMapper = initObjectMapper(new ObjectMapper());
	}

	/**
	 * 初始化 ObjectMapper
	 * @param objectMapper
	 * @return
	 */
	public static ObjectMapper initObjectMapper(ObjectMapper objectMapper) {
		if (Objects.isNull(objectMapper)) {
			objectMapper = new ObjectMapper();
		}

		// 设置Jackson特性
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		objectMapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
		return objectMapper;
	}

	/**
	 * 获取ObjectMapper
	 */
	private static ObjectMapper getObjectMapper() {
		return objectMapper;
	}


	/**
	 * Json转换为对象 转换失败返回null
	 * @param json
	 */
	public static Object parse(String json) {
		Object object = null;
		try {
			object = getObjectMapper().readValue(json, Object.class);
		}
		catch (Exception ignored) {
		}
		return object;
	}

	/**
	 * Json转换为对象 转换失败返回null
	 * @param json
	 * @param clazz
	 * @param <T>
	 */
	public static <T> T readValue(String json, Class<T> clazz) {
		T t = null;
		try {
			t = getObjectMapper().readValue(json, clazz);
		}
		catch (Exception ignored) {
		}
		return t;
	}

	/**
	 * Json转换为对象 转换失败返回null
	 * @param json
	 * @param valueTypeRef
	 * @param <T>
	 * @return
	 */
	public static <T> T readValue(String json, TypeReference valueTypeRef) {
		T t = null;
		try {
			t = (T) getObjectMapper().readValue(json, valueTypeRef);
		}
		catch (Exception ignored) {
		}
		return t;
	}

	private JacksonUtils() {

	}

	public static ObjectMapper getInstance() {
		return objectMapper;
	}

	/**
	 * javaBean、列表数组转换为json字符串
	 */
	public static String obj2json(Object obj) throws Exception {
		return objectMapper.writeValueAsString(obj);
	}

	/**
	 * javaBean、列表数组转换为json字符串,忽略空值
	 */
	public static String obj2jsonIgnoreNull(Object obj) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		return mapper.writeValueAsString(obj);
	}

	/**
	 * json 转JavaBean
	 */

	public static <T> T json2pojo(String jsonString, Class<T> clazz) throws Exception {
		objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
		return objectMapper.readValue(jsonString, clazz);
	}

	/**
	 * json字符串转换为map
	 */
	public static <T> Map<String, Object> json2map(String jsonString) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		return mapper.readValue(jsonString, Map.class);
	}

	/**
	 * json字符串转换为map
	 */
	public static <T> Map<String, T> json2map(String jsonString, Class<T> clazz) throws Exception {
		Map<String, Map<String, Object>> map = (Map<String, Map<String, Object>>) objectMapper.readValue(jsonString, new TypeReference<Map<String, T>>() {});
		Map<String, T> result = new HashMap<String, T>();
		for (Map.Entry<String, Map<String, Object>> entry : map.entrySet()) {
			result.put(entry.getKey(), map2pojo(entry.getValue(), clazz));
		}
		return result;
	}

	/**
	 * 深度转换json成map
	 *
	 * @param json
	 * @return
	 */
	public static Map<String, Object> json2mapDeeply(String json) throws Exception {
		return json2MapRecursion(json, objectMapper);
	}

	/**
	 * 把json解析成list，如果list内部的元素存在jsonString，继续解析
	 *
	 * @param json
	 * @param mapper 解析工具
	 * @return
	 * @throws Exception
	 */
	private static List<Object> json2ListRecursion(String json, ObjectMapper mapper) throws Exception {
		if (json == null) {
			return null;
		}

		List<Object> list = mapper.readValue(json, List.class);

		for (Object obj : list) {
			if (obj != null && obj instanceof String) {
				String str = (String) obj;
				if (str.startsWith("[")) {
					obj = json2ListRecursion(str, mapper);
				} else if (obj.toString().startsWith("{")) {
					obj = json2MapRecursion(str, mapper);
				}
			}
		}

		return list;
	}

	/**
	 * 把json解析成map，如果map内部的value存在jsonString，继续解析
	 *
	 * @param json
	 * @param mapper
	 * @return
	 * @throws Exception
	 */
	private static Map<String, Object> json2MapRecursion(String json, ObjectMapper mapper) throws Exception {
		if (json == null) {
			return null;
		}

		Map<String, Object> map = mapper.readValue(json, Map.class);

		for (Map.Entry<String, Object> entry : map.entrySet()) {
			Object obj = entry.getValue();
			if (obj != null && obj instanceof String) {
				String str = ((String) obj);

				if (str.startsWith("[")) {
					List<?> list = json2ListRecursion(str, mapper);
					map.put(entry.getKey(), list);
				} else if (str.startsWith("{")) {
					Map<String, Object> mapRecursion = json2MapRecursion(str, mapper);
					map.put(entry.getKey(), mapRecursion);
				}
			}
		}

		return map;
	}

	/**
	 * 与javaBean json数组字符串转换为列表
	 */
	public static <T> List<T> json2list(String jsonArrayStr, Class<T> clazz) throws Exception {

		JavaType javaType = getCollectionType(ArrayList.class, clazz);
		List<T> lst = (List<T>) objectMapper.readValue(jsonArrayStr, javaType);
		return lst;
	}


	/**
	 * 获取泛型的Collection Type
	 *
	 * @param collectionClass 泛型的Collection
	 * @param elementClasses  元素类
	 * @return JavaType Java类型
	 * @since 1.0
	 */
	public static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
		return objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
	}


	/**
	 * map  转JavaBean
	 */
	public static <T> T map2pojo(Map map, Class<T> clazz) {
		return objectMapper.convertValue(map, clazz);
	}

	/**
	 * map 转json
	 *
	 * @param map
	 * @return
	 */
	public static String mapToJson(Map map) {
		try {
			return objectMapper.writeValueAsString(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * map  转JavaBean
	 */
	public static <T> T obj2pojo(Object obj, Class<T> clazz) {
		return objectMapper.convertValue(obj, clazz);
	}

}
