package com.github.ryanddu.vo.res;

import com.github.ryanddu.constant.enums.CommonCodeEnum;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Optional;

/**
 * 通用响应
 * @author: ryan
 * @date: 2023/3/27 20:01
 **/
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class R<T> extends CommonResponse<T> {


    public R() {
        super();
    }

    public R(T data) {
        super();
        this.data = data;
    }

    public R(T data, String msg) {
        super();
        this.data = data;
        this.msg = msg;
    }

    public R(Throwable e) {
        super();
        this.msg = e.getMessage();
        this.code = CommonCodeEnum.SERVER_ERROR.getCode();
    }


    public R(int errorCode, String msg) {
        errorCode = Optional.ofNullable(errorCode).orElse(CommonCodeEnum.SERVER_ERROR.getCode());
        this.code = errorCode;
        this.msg = msg;
    }

	/**
	 * 成功
	 * @return 响应实体
	 */
    public static R ok(){
        return restResult(null,CommonCodeEnum.SUCCESS.getCode(), CommonCodeEnum.SUCCESS.getMsg());
    }


    /**
     *
     * 成功
     * @param data
     * @param <T>
     * @return
     */
    public static <T> R<T> ok(T data) {
        return restResult(data, CommonCodeEnum.SUCCESS.getCode(),CommonCodeEnum.SUCCESS.getMsg());
    }

    /**
     * 失败
     * @param msg
     * @param <T>
     * @return
     */
    public static <T> R<T> failed(String msg) {
        return restResult(null, CommonCodeEnum.SERVER_ERROR.getCode(), msg);
    }

	/**
	 * 失败
	 * @param errorCode 错误响应码
	 * @param msg 消息
	 * @return
	 */
    public static <T> R<T> failed(int errorCode,String msg) {
        return restResult(null, errorCode,msg);
    }
    public static <T> R<T> failed(int errorCode,String msg,T data) {
        return restResult(data, errorCode,msg);
    }

    public static <T> R<T> failed(R errorR) {
        return restResult(null, errorR.getCode(),errorR.getMsg());
    }

    public static R result(boolean result) {
        if (result) {
            return ok(result);
        } else {
            return failed(CommonCodeEnum.SERVER_ERROR.getCode(),CommonCodeEnum.SERVER_ERROR.getMsg(),result);
        }
    }

    public static R result(boolean result, String msg) {
        if (result) {
            return ok(result);
        } else {
            return failed(CommonCodeEnum.SERVER_ERROR.getCode(),msg,result);
        }
    }


    private static <T> R<T> restResult(T data, int code, String msg) {
        R<T> apiResult = new R<>();
        apiResult.setCode(code);
        apiResult.setData(data);
        apiResult.setMsg(msg);
        return apiResult;
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
    public boolean fail(){
        return !success();
    }

}
