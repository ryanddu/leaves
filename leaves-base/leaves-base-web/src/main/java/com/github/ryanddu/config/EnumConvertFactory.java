package com.github.ryanddu.config;

import com.baomidou.mybatisplus.core.enums.IEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
/**
 * 枚举转换工厂
 * @author: ryan
 * @date: 2023/3/27 9:48
 **/
@Component
public class EnumConvertFactory implements ConverterFactory<String, IEnum<?>> {

    public static <T extends IEnum<?>> T getEnum(Class<T> targetType, String source) {
        for (T constant : targetType.getEnumConstants()) {
            if (source.equals(String.valueOf(constant.getValue()))) {
                return constant;
            }
        }
        return null;
    }

    @NotNull
    @Override
    public <T extends IEnum<?>> Converter<String, T> getConverter(@NotNull Class<T> targetType) {
        return new StringToEnum<>(targetType);
    }

    public static class StringToEnum<T extends IEnum<?>> implements Converter<String, T> {

        private final Class<T> targetType;

        public StringToEnum(Class<T> targetType) {
            this.targetType = targetType;
        }

        @Override
        public T convert(@NotNull String source) {
            if (StringUtils.isBlank(source)) {
                return null;
            }
            return EnumConvertFactory.getEnum(this.targetType, source);
        }
    }
}
