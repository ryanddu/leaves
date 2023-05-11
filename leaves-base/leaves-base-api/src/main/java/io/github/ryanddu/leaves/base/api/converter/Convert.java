package io.github.ryanddu.leaves.base.api.converter;

import io.github.ryanddu.leaves.base.core.utils.BeanUtils;

/**
 * 让Entity具有数据复制功能
 *
 * @author: ryan
 * @date: 2023/3/27 9:48
 **/
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
