package io.github.ryanddu.leaves.base.core.exception;

/**
 * 异常基类
 *
 * @author: ryan
 * @date: 2023/3/24 17:07
 **/
public class LeavesException extends RuntimeException {

	public LeavesException() {
	}

	public LeavesException(String message) {
		super(message);
	}

	public LeavesException(Throwable cause) {
		super(cause);
	}

	public LeavesException(String message, Throwable cause) {
		super(message, cause);
	}

}
