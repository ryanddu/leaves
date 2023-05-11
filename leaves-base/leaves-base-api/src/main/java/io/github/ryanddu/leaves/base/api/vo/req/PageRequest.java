package io.github.ryanddu.leaves.base.api.vo.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 分页请求基类
 *
 * @author: ryan
 * @date: 2023/3/27 9:49
 **/
@Data
@Schema(description = "分页参数")
public class PageRequest extends BaseRequest {

	private static final long serialVersionUID = -7742620578397707781L;

	/**
	 * 默认页码，第1页
	 */
	private static final long DEFAULT_PAGE_NO = 1;

	/**
	 * 默认分页大小，默认10条记录
	 */
	private static final long DEFAULT_PAGE_SIZE = 10;

	/**
	 * 页码
	 */
	@NotNull(message = "页码不能为空")
	@Schema(description = "页码")
	protected long pageNo = DEFAULT_PAGE_NO;

	/**
	 * 分页大小
	 */
	@NotNull(message = "分页大小不能为空")
	@Schema(description = "分页大小")
	protected long pageSize = DEFAULT_PAGE_SIZE;

	/**
	 * 构建 mybatis-plus 分页对象
	 * @return mybatis-plus 分页对象
	 */
	public Page buildPage() {
		return new Page(pageNo, pageSize);
	}

}
