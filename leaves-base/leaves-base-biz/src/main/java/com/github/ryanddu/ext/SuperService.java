package com.github.ryanddu.ext;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.ryanddu.utils.BeanUtils;
import com.github.ryanddu.vo.req.BaseRequest;
import com.github.ryanddu.vo.req.PageRequest;
import com.github.ryanddu.vo.res.PageData;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;

/**
 * 通用service
 * @author: ryan
 * @date: 2023/3/27 21:00
 **/
public interface SuperService<T extends SuperEntity> extends IService<T> {

	/**
	 * 保存
	 * @param request
	 * @return
	 */
	default boolean save(BaseRequest request) {
		return save(request.convert(
				(Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1]));
	}

	/**
	 * 更新(根据主键)
	 * @param id
	 * @param request
	 */
	default boolean updateById(Long id, BaseRequest request) {
		T entity = request.convert(
				(Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1]);
		entity.setId(id);
		return updateById(entity);
	}

	/**
	 * 更新(根据主键)
	 * @param id
	 * @param request
	 */
	default boolean updateById(String id, BaseRequest request) {
		T entity = request.convert(
				(Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1]);
		entity.setId(id);
		return updateById(entity);
	}

	/**
	 * <p>
	 * 查询所有列表，并映射对象
	 * </p>
	 * @param destinationClass 映射目标对象
	 * @param <R>
	 */
	default <R> List<R> listWithConvert(Class<R> destinationClass) {
		return listWithConvert(Wrappers.emptyWrapper(), destinationClass);
	}

	/**
	 * <p>
	 * 条件查询列表，并映射对象
	 * </p>
	 * @param queryWrapper 查询条件
	 * @param destinationClass 映射目标对象
	 * @param <R>
	 */
	<R> List<R> listWithConvert(Wrapper<T> queryWrapper, Class<R> destinationClass);

	/**
	 * <p>
	 * 查询分页，并映射对象
	 * </p>
	 * @param pageRequest 分页对象
	 * @param destinationClass 映射目标对象
	 * @param <R>
	 */
	default <R> PageData<R> pageWithConvert(PageRequest pageRequest, Class<R> destinationClass) {
		return new PageData<>(pageWithConvert(pageRequest.buildPage(), Wrappers.emptyWrapper(), destinationClass));
	}

	/**
	 * <p>
	 * 带条件查询分页，并映射对象
	 * </p>
	 * @param pageRequest 分页对象
	 * @param queryWrapper 查询条件
	 * @param destinationClass 映射目标对象
	 * @param <R>
	 * @return
	 */
	default <R> PageData<R> pageWithConvert(PageRequest pageRequest, Wrapper<T> queryWrapper,
											Class<R> destinationClass) {
		return new PageData<>(pageWithConvert(pageRequest.buildPage(), queryWrapper, destinationClass));
	}

	/**
	 * 自定义SQL分页查询
	 * @param pageData 分页查询后的结果集
	 * @param destinationClass 转换的目标对象
	 * @param <R>
	 * @return
	 */
	default <R> PageData<R> pageWithCustomSQL(IPage<?> pageData, Class<R> destinationClass) {
		return new PageData(pageData.convert(e -> BeanUtils.map(e, destinationClass)));
	}

	/**
	 * <p>
	 * 条件查询分页，并映射对象
	 * </p>
	 * @param page 分页对象
	 * @param queryWrapper 查询条件
	 * @param destinationClass 映射目标对象
	 * @param <R>
	 */
	<R> IPage<R> pageWithConvert(IPage<T> page, Wrapper<T> queryWrapper, Class<R> destinationClass);

	/**
	 * <p>
	 * 查询（根据ID 批量查询）
	 * </p>
	 * @param idList 主键ID列表
	 * @param destinationClass 映射目标对象
	 * @param <R>
	 */
	<R> Collection<R> listByIdsWithConvert(Collection<? extends Serializable> idList, Class<R> destinationClass);
}