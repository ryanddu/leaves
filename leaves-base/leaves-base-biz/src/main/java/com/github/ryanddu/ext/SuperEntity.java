package com.github.ryanddu.ext;

import com.github.ryanddu.converter.Convert;

import java.io.Serializable;

/**
 * Entity基类
 * @author: ryan
 * @date: 2023/3/27 20:39
 **/
public interface SuperEntity extends Convert, Serializable {

	/**
	 * 适应于UUID算法
	 * @param id
	 */
	default void setId(String id){}

	/**
	 * 适应于IdWork算法
	 * @param id
	 */
	default void setId(Long id){}

}
