package com.github.ryanddu.constant.enums;

import com.baomidou.mybatisplus.annotation.IEnum;

import java.io.Serializable;

/**
 * 枚举基类
 *
 * @author: ryan
 * @date: 2023/3/27 20:33
 **/
public interface BaseEnum<T extends Serializable> extends IEnum<T> {

	String getDesc();

	/**
	 * 判断字典代码是否相同
	 * @param value
	 * @return
	 */
	default boolean equalsCode(T value) {
		return getValue().equals(value);
	}

}