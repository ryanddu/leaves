package io.github.ryanddu.exception;

import io.github.ryanddu.constant.enums.CommonCodeEnum;

/**
 * 非国际化异常基类
 *
 * @author: ryan
 * @date: 2023/3/24 17:11
 **/
public class BizException extends LeavesException {

	/**
	 * 异常状态码
	 */
	private int code;

	/**
	 * 异常消息
	 */
	private String msg;

	public BizException() {
		this.code = CommonCodeEnum.SERVER_ERROR.getCode();
		this.msg = CommonCodeEnum.SERVER_ERROR.getMsg();
	}

	public BizException(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
