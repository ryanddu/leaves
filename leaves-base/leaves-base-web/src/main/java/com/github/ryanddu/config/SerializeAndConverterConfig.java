package com.github.ryanddu.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 序列化和属性转换配置
 * @author: ryan
 * @date: 2023/3/29 10:01
 **/
@Configuration
public class SerializeAndConverterConfig {

	/**
	 * 默认日期时间格式
	 */
	public static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 默认日期格式
	 */
	public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

	/**
	 * 默认时间格式
	 */
	public static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";

	/**
	 * 默认时间格式 yyyy-MM
	 */
	public static final String DEFAULT_YYYY_MM_FORMAT = "yyyy-MM";

	/**
	 * 默认时间格式 yyyy-MM
	 */
	public static final String DEFAULT_YYYY_MM_DD_HH_MM_FORMAT = "yyyy-MM-dd HH:mm";



	/**
	 * LocalDate转换器，用于转换RequestParam和PathVariable参数
	 */
	@Bean
	public Converter<String, LocalDate> localDateConverter() {
		return new Converter<String, LocalDate>() {
			@Override
			public LocalDate convert(String source) {
				return LocalDate.parse(source, DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT));
			}
		};
	}

	/**
	 * LocalDateTime转换器，用于转换RequestParam和PathVariable参数
	 */
	@Bean
	public Converter<String, LocalDateTime> localDateTimeConverter() {
		return new Converter<String, LocalDateTime>() {
			@Override
			public LocalDateTime convert(String source) {
				return LocalDateTime.parse(source, DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT));
			}
		};
	}

	/**
	 * LocalTime转换器，用于转换RequestParam和PathVariable参数
	 */
	@Bean
	public Converter<String, LocalTime> localTimeConverter() {
		return new Converter<String, LocalTime>() {
			@Override
			public LocalTime convert(String source) {
				return LocalTime.parse(source, DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT));
			}
		};
	}

	/**
	 * Date转换器，用于转换RequestParam和PathVariable参数
	 */
	@Bean
	public Converter<String, Date> dateConverter() {
		return (source)-> {
			if (StringUtils.isBlank(source)) {
				return null;
			}
			source = source.trim();
			if(source.matches("^\\d{4}-\\d{1,2}$")){
				return parseDate(source, DEFAULT_YYYY_MM_FORMAT);
			}else if(source.matches("^\\d{4}-\\d{1,2}-\\d{1,2}$")){
				return parseDate(source, DEFAULT_DATE_FORMAT);
			}else if(source.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}$")){
				return parseDate(source, DEFAULT_YYYY_MM_DD_HH_MM_FORMAT);
			}else if(source.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$")){
				return parseDate(source, DEFAULT_DATE_TIME_FORMAT);
			}else {
				throw new IllegalArgumentException("Invalid false value '" + source + "'");
			}
		};
	}

	/**
	 * 格式化日期
	 * @param dateStr String 字符型日期
	 * @param format String 格式
	 * @return Date 日期
	 */
	private  Date parseDate(String dateStr, String format) {
		Date date;
		try {
			DateFormat dateFormat = new SimpleDateFormat(format);
			date = dateFormat.parse(dateStr);
		} catch (Exception e) {
			throw new IllegalArgumentException(e.getLocalizedMessage());
		}
		return date;
	}


	/**
	 * Json序列化和反序列化转换器，用于转换Post请求体中的json以及将我们的对象序列化为返回响应的json
	 */
	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		objectMapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);

		//LocalDateTime系列序列化和反序列化模块，继承自jsr310，我们在这里修改了日期格式
		JavaTimeModule javaTimeModule = new JavaTimeModule();
		javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)));
		javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)));
		javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT)));
		javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)));
		javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)));
		javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT)));

		//Date序列化和反序列化
		javaTimeModule.addSerializer(Date.class, new JsonSerializer<Date>() {
			@Override
			public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
				SimpleDateFormat formatter = new SimpleDateFormat(DEFAULT_DATE_TIME_FORMAT);
				String formattedDate = formatter.format(date);
				jsonGenerator.writeString(formattedDate);
			}
		});
		javaTimeModule.addDeserializer(Date.class, new JsonDeserializer<Date>() {
			@Override
			public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
				SimpleDateFormat format = new SimpleDateFormat(DEFAULT_DATE_TIME_FORMAT);
				String date = jsonParser.getText();
				try {
					return format.parse(date);
				} catch (ParseException e) {
					throw new RuntimeException(e);
				}
			}
		});

		// 处理Long类型数据超过17位，导致传入前端精度丢失的问题
		javaTimeModule.addSerializer(Long.class, ToStringSerializer.instance);

		objectMapper.registerModule(javaTimeModule);
		return objectMapper;
	}
}
