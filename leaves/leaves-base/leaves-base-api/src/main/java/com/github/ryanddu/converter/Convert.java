package com.github.ryanddu.converter;


import com.github.ryanddu.utils.BeanUtils;

/**
 * 让Entity具有数据复制功能
 * @author chenhuainian
 */
public interface Convert {

	/**
	 * 获取自动转换后的JavaBean对象
	 * @param targetClazz
	 * @return
	 */
	default <T> T convert(Class<T> targetClazz) {
		return BeanUtils.map(this, targetClazz);
	}

}
