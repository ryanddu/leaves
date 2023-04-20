package io.github.ryanddu.assertion;

import io.github.ryanddu.exception.LeavesException;

/**
 * LeavesException断言
 *
 * @author: ryan
 * @date: 2023/3/24 18:39
 **/
public class LeavesExceptionAssert implements Assert<LeavesException> {

	@Override
	public LeavesException newException(String message) {
		return new LeavesException(message);
	}

	@Override
	public LeavesException newException(Throwable cause) {
		return new LeavesException(cause);
	}

	@Override
	public LeavesException newException(String message, Throwable cause) {
		return new LeavesException(message, cause);
	}

}
