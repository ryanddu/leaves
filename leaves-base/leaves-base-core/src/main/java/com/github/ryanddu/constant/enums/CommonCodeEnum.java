package com.github.ryanddu.constant.enums;

/**
 * 全局通用状态码枚举
 *
 * @author: ryan
 * @date: 2023/3/24 17:43
 **/
public enum CommonCodeEnum {

	/**
	 * 成功
	 */
	SUCCESS(0, "请求成功"),

	/**
	 * 缺少必要参数
	 */
	REQUIRED_PARAMETER_MISSING(1000,"缺少必要参数"),

	/**
	 * 参数类型不匹配
	 */
	PARAMETER_TYPE_DO_NOT_MATCH(1001,"参数类型不匹配"),

	/**
	 * 参数错误
	 */
	PARAMETER_ERROR(1002,"参数错误"),

	/**
	 * 服务器异常，无法识别的异常，尽可能对通过判断减少未定义异常抛出
	 */
	SERVER_ERROR(9999, "服务器异常"),

	/**
	 * 服务器繁忙，请稍后重试
	 */
	SERVER_BUSY(9998, "服务器繁忙"),

	/**
	 * Token
	 */
	TOKEN_INVALID(9996, "无效的Token"), TOKEN_EXPIRE(9995, "Token已过期"), NON_PERMISSION(9997, "无访问权限");

	/**
	 * 返回码
	 */
	private final int code;

	/**
	 * 返回消息
	 */
	private final String msg;

	CommonCodeEnum(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

}
