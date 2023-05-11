package io.github.ryanddu.vo.res;

import io.github.ryanddu.constant.enums.CommonCodeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 通用响应
 *
 * @author: ryan
 * @date: 2023/3/27 20:01
 **/
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class R<T> extends CommonResponse<T> {

	/**
	 * 上下文
	 */
	@Schema(description = "上下文")
	private String context;

	public R() {
		super();
	}

	public R(int code) {
		super(code);
	}

	public R(T data) {
		super(data);
	}

	public R(int code, String msg) {
		super(code, msg);
	}

	public R(int code, String msg, T data) {
		super(code, msg, data);
	}

	/**
	 * 成功
	 * @return 响应实体
	 */
	public static R ok() {
		return restResult(CommonCodeEnum.SUCCESS.getCode(), CommonCodeEnum.SUCCESS.getMsg(), null);
	}

	/**
	 *
	 * 成功
	 * @param data
	 * @param <T>
	 * @return
	 */
	public static <T> R<T> ok(T data) {
		return restResult(CommonCodeEnum.SUCCESS.getCode(), CommonCodeEnum.SUCCESS.getMsg(), data);
	}

	/**
	 * 失败
	 * @param <T>
	 * @return
	 */
	public static <T> R<T> failed() {
		return restResult(CommonCodeEnum.SERVER_ERROR.getCode(), CommonCodeEnum.SUCCESS.getMsg(), null);
	}

	/**
	 * 失败
	 * @param code
	 * @param <T>
	 * @return
	 */
	public static <T> R<T> failed(int code) {
		return restResult(code, null, null);
	}

	/**
	 * 失败
	 * @param msg
	 * @param <T>
	 * @return
	 */
	public static <T> R<T> failed(String msg) {
		return restResult(CommonCodeEnum.SERVER_ERROR.getCode(), msg, null);
	}

	/**
	 * 失败
	 * @param data
	 * @param <T>
	 * @return
	 */
	public static <T> R<T> failed(T data) {
		return restResult(CommonCodeEnum.SERVER_ERROR.getCode(), CommonCodeEnum.SERVER_ERROR.getMsg(), data);
	}

	/**
	 * 失败
	 * @param code 错误响应码
	 * @param msg 消息
	 * @return
	 */
	public static <T> R<T> failed(int code, String msg) {
		return restResult(code, msg, null);
	}

	public static <T> R<T> failed(int code, String msg, T data) {
		return restResult(code, msg, data);
	}

	public static <T> R<T> failed(R<T> r) {
		return restResult(r.getCode(), r.getMsg(), r.getData());
	}

	public static R result(boolean result) {
		if (result) {
			return ok(result);
		}
		else {
			return failed(result);
		}
	}

	public static R result(boolean result, String msg) {
		if (result) {
			return ok(result);
		}
		else {
			return failed(CommonCodeEnum.SERVER_ERROR.getCode(), msg, result);
		}
	}

	private static <T> R<T> restResult(int code, String msg, T data) {
		return new R<>(code, msg, data);
	}

	/**
	 * 返回是否成功
	 * @return
	 */
	public boolean success() {
		return CommonCodeEnum.SUCCESS.getCode() == code;
	}

	/**
	 * 返回是否失败
	 * @return
	 */
	public boolean fail() {
		return !success();
	}

}
