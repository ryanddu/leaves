package com.github.ryanddu.ext;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.ryanddu.utils.BeanUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * SuperService 实现类（ 泛型：M 是 mapper 对象，T 是实体 ， PK 是主键泛型 ）
 *
 * @author: ryan
 * @date: 2023/3/28 10:23
 **/
public class SuperServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> {

	/**
	 * <p>
	 * 查询列表，并映射对象
	 * </p>
	 * @param queryWrapper 查询条件
	 * @param destinationClass 映射目标对象
	 * @param <R>
	 */
	public <R> List<R> listWithConvert(Wrapper<T> queryWrapper, Class<R> destinationClass) {
		return BeanUtils.mapAsList(list(queryWrapper), destinationClass);
	}

	/**
	 * <p>
	 * 查询分页（带条件），并映射对象
	 * </p>
	 * @param page 分页对象
	 * @param queryWrapper 查询条件
	 * @param destinationClass 映射目标对象
	 * @param <R>
	 */
	public <R> IPage<R> pageWithConvert(IPage<T> page, Wrapper<T> queryWrapper, Class<R> destinationClass) {
		return page(page, queryWrapper).convert(e -> BeanUtils.map(e, destinationClass));
	}

	/**
	 * <p>
	 * 查询（根据ID 批量查询）
	 * </p>
	 * @param idList 主键ID列表
	 * @param destinationClass 映射目标对象
	 * @param <R>
	 */
	public <R> Collection<R> listByIdsWithConvert(Collection<? extends Serializable> idList,
			Class<R> destinationClass) {

		return BeanUtils.mapAsList(Stream.of(idList).collect(Collectors.toList()), destinationClass);
	}

	/**
	 * <p>
	 * 批量更新
	 * </p>
	 * @param entityList 实体列表
	 * @param destinationClass 映射目标对象
	 * @param <R>
	 */
	public <R> Boolean updateBatchByIdWithConvert(Collection<R> entityList, Class<T> destinationClass) {
		List<T> ts = BeanUtils.mapAsList(Stream.of(entityList).collect(Collectors.toList()), destinationClass);
		return updateBatchById(ts);
	}

}
