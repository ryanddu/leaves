package io.github.ryanddu.exception;

/**
 * 国际化异常基类
 *
 * @author: ryan
 * @date: 2023/3/24 17:11
 **/
public class CodeException extends LeavesException {

	/**
	 * 异常状态码
	 */
	private int code;

	/**
	 * 异常消息参数
	 */
	private Object[] args;

	public CodeException(int code) {
		this.code = code;
	}

	public CodeException(int code, Object... args) {
		this.code = code;
		this.args = args;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}

	public String getMessage() {
		return this.code + ":" + this.args;
	}

}
