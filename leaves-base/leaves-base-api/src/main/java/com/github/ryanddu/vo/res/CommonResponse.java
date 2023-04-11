package com.github.ryanddu.vo.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 通用响应基类
 *
 * @author: ryan
 * @date: 2023/3/27 9:52
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class CommonResponse<T> extends BaseResponse {

	/**
	 * 响应数据
	 */
	@Schema(description = "响应数据")
	protected T data;

	public CommonResponse() {
		super();
	}

	public CommonResponse(T data) {
		super();
		this.data = data;
	}

}
