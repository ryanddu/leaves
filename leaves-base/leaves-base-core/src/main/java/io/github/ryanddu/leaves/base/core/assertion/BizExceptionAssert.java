package io.github.ryanddu.leaves.base.core.assertion;

import io.github.ryanddu.leaves.base.core.exception.BizException;

/**
 * BizException断言
 *
 * @author: ryan
 * @date: 2023/3/24 18:39
 **/
public class BizExceptionAssert implements Assert<BizException> {

	@Override
	public BizException newException() {
		return new BizException();
	}

	@Override
	public BizException newException(int code, String message) {
		return new BizException(code, message);
	}

}
