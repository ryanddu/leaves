package io.github.ryanddu.leaves.base.core.assertion;

import io.github.ryanddu.leaves.base.core.exception.CodeException;
import io.github.ryanddu.leaves.base.core.exception.LeavesException;

/**
 * CodeException断言
 *
 * @author: ryan
 * @date: 2023/3/24 18:39
 **/
public class CodeExceptionAssert implements Assert<LeavesException> {

	@Override
	public CodeException newException(int code) {
		return new CodeException(code);
	}

	@Override
	public CodeException newException(int code, Object... args) {
		return new CodeException(code, args);
	}

}
