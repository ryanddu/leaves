
package io.github.ryanddu.leaves.base.web.advice;

import io.github.ryanddu.leaves.base.core.constant.enums.CommonCodeEnum;
import io.github.ryanddu.leaves.base.core.exception.BizException;
import io.github.ryanddu.leaves.base.core.exception.CodeException;
import io.github.ryanddu.leaves.base.core.utils.Kv;
import io.github.ryanddu.leaves.base.api.vo.res.R;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.skywalking.apm.toolkit.trace.TraceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.util.ClassUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.Set;

/**
 * 全局异常拦截
 *
 * @author: ryan
 * @date: 2023/4/11 17:12
 **/
@Order
@RestControllerAdvice
public class ExceptionHandlerAdvice implements EnvironmentAware {

	private final Logger logger = LoggerFactory.getLogger(ExceptionHandlerAdvice.class);

	private boolean hasTrace = ClassUtils.isPresent("org.apache.skywalking.apm.toolkit.trace.TraceContext",
			ExceptionHandlerAdvice.class.getClassLoader());

	private Environment environment;

	private final MessageSource messageSource;

	@Value("${leaves.show-error-detail:false}")
	private boolean showErrorDetail;

	public ExceptionHandlerAdvice(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	@ExceptionHandler({ HttpMediaTypeNotSupportedException.class })
	public R<Void> mediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpServletRequest request) {
		this.logger.warn("不支持的ContentType:[{}],[{}]", request.getRequestURI(), ex.getContentType());
		return this.wrapResult(R.failed(CommonCodeEnum.NOT_ACCEPTABLE.getCode()), null);
	}

	@ExceptionHandler({ HttpRequestMethodNotSupportedException.class })
	public R<Void> methodNotSupported(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
		this.logger.warn("不支持的method:[{}],[{}]", request.getRequestURI(), ex.getMethod());
		return this.wrapResult(R.failed(CommonCodeEnum.METHOD_NOT_ALLOWED.getCode()), null);
	}

	@ExceptionHandler({ NoHandlerFoundException.class })
	public R<Void> handleNoHandlerFoundException(NoHandlerFoundException ex) {
		this.logger.warn("无效的访问地址:[{}]", ex.getRequestURL());
		return this.wrapResult(R.failed(CommonCodeEnum.NOT_FOUND.getCode()), null);
	}

	@ExceptionHandler({ CodeException.class })
	public R<Void> handleCodeException(CodeException e, HttpServletRequest request) {
		R<Void> r = this.wrapResult(R.failed(e.getCode()), e.getArgs());
		this.logger.error("业务异常[{}][{},{}]", request.getRequestURI(), r.getCode(), r.getMsg());
		return r;
	}

	@ExceptionHandler({ BizException.class })
	public R<Void> handleBizException(BizException e, HttpServletRequest request) {
		R<Void> r = this.wrapResult(R.failed(e.getCode()), null);
		this.logger.error("业务异常[{}][{},{}]", request.getRequestURI(), r.getCode(), r.getMsg());
		return r;
	}

	@ExceptionHandler({ Exception.class })
	public R<Void> handleException(Exception e, HttpServletRequest request) {
		this.logger.error("系统错误:[{}]", request.getRequestURI(), e);
		if (this.showErrorDetail) {
			return this.wrapResult(R.failed(CommonCodeEnum.SERVER_ERROR_WITH_DETAIL.getCode()),
					new Object[] { ExceptionUtils.getStackTrace(e) });
		}
		else {
			return this.wrapResult(R.failed(CommonCodeEnum.SERVER_ERROR.getCode()), null);
		}
	}

	@ExceptionHandler({ MissingServletRequestParameterException.class })
	public R<Void> MissingServletRequestParameter(MissingServletRequestParameterException ex,
			HttpServletRequest request) {
		this.logger.warn("缺少必要参数:[{}],[{}]", request.getRequestURI(), ex.getParameterName());
		return this.wrapResult(R.failed(CommonCodeEnum.REQUIRED_PARAMETER_MISSING.getCode()),
				new Object[] { ex.getParameterName() });
	}

	@ExceptionHandler({ TypeMismatchException.class })
	public R<Void> requestTypeMismatch(TypeMismatchException ex, HttpServletRequest request) {
		this.logger.warn("参数类型不匹配:[{}],[{}],[{}]",
				new Object[] { request.getRequestURI(), ex.getPropertyName(), ex.getRequiredType() });
		return this.wrapResult(R.failed(CommonCodeEnum.PARAMETER_TYPE_DO_NOT_MATCH.getCode()),
				new Object[] { ex.getPropertyName() });
	}

	@ExceptionHandler({ BindException.class })
	public R<Void> handleBindException(BindException ex) {
		FieldError fieldError = ex.getBindingResult().getFieldError();
		this.logger.warn("参数错误:[{}],[{}]", fieldError.getField(), fieldError.getDefaultMessage());
		return this.wrapResult(R.failed(CommonCodeEnum.PARAMETER_ERROR.getCode()),
				new Object[] { fieldError.getDefaultMessage() });
	}

	@ExceptionHandler({ ValidationException.class })
	public R<Void> handleValidationException(ConstraintViolationException ex) {
		Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
		ConstraintViolation<?> next = constraintViolations.iterator().next();
		this.logger.warn("参数错误:[{}]", next.getMessage());
		return this.wrapResult(R.failed(CommonCodeEnum.PARAMETER_ERROR.getCode()), new Object[] { next.getMessage() });
	}

	private R<Void> wrapResult(R r, Object[] args) {
		this.wrapResultContext(r);
		String message = this.messageSource.getMessage(String.valueOf(r.getCode()), args,
				LocaleContextHolder.getLocale());
		r.setMsg(message);
		return r;
	}

	private R<Void> wrapResultContext(R r) {
		if (r.getContext() == null) {
			Kv kv = Kv.create();
			kv.set("app", this.environment.getProperty("spring.application.name"));
			if (this.hasTrace) {
				kv.set("traceId", TraceContext.traceId());
			}

			r.setContext(kv.toJson());
		}

		return r;
	}

}
