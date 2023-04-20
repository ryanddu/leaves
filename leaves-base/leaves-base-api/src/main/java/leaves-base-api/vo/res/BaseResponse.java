package io.github.ryanddu.vo.res;

import io.github.ryanddu.constant.enums.CommonCodeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 响应基类
 *
 * @author: ryan
 * @date: 2023/3/27 9:50
 **/
@Data
public class BaseResponse {

	/**
	 * 响应码
	 */
	@NotBlank(message = "响应码不能为空")
	@Schema(description = "响应码")
	protected int code;

	/**
	 * 响应消息
	 */
	@Schema(description = "响应消息")
	protected String msg;

	public BaseResponse() {
		// 默认创建成功的回应
		this(CommonCodeEnum.SUCCESS.getCode(), CommonCodeEnum.SUCCESS.getMsg());
	}

	public BaseResponse(Integer code) {
		this(code, null);
	}

	public BaseResponse(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

}
