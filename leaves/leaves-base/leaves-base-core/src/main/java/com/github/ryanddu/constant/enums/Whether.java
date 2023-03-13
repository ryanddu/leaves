package com.github.ryanddu.constant.enums;

/**
 * 锁定状态
 *
 * @author lm
 */
public enum Whether{

    YES(1, "是"),
    NO(0, "否");

    private int code;

    private String msg;

	Whether(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

    public static Whether fromCode(Integer code) {
        if (code == null) {
            return null;
        }
        switch (code) {
            case 0:
                return NO;
            case 1:
                return YES;
            default:
                return null;
        }
    }

}
