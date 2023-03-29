package com.github.ryanddu.assertion;

import com.github.ryanddu.exception.CodeException;
import com.github.ryanddu.exception.LeavesException;

/**
 * CodeException断言
 *
 * @author: ryan
 * @date: 2023/3/24 18:39
 **/
public class CodeExceptionAssert implements Assert<LeavesException> {

	@Override
	public CodeException newException(Integer code) {
		return new CodeException(code);
	}

	@Override
	public CodeException newException(Integer code, Object... args) {
		return new CodeException(code, args);
	}

}
