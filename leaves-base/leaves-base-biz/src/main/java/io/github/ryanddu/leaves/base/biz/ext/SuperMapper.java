package io.github.ryanddu.leaves.base.biz.ext;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 通用mapper
 *
 * @author: ryan
 * @date: 2023/4/4 21:11
 **/
public interface SuperMapper<T> extends BaseMapper<T> {

	// 这里可以写 mapper 层公共方法、 注意！！ 多泛型的时候请将泛型T放在第一位.

}
