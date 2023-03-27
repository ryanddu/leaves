package com.github.ryanddu.constant;

import lombok.Getter;

/**
 * 平台类型枚举
 */
@Getter
public enum PlatformEnum {
    IOS("1","IOS"),
    ANDROID("2","安卓"),
    APPLETS("3","小程序"),
    PC("4","PC"),
    MOBILE_H5("5","移动端H5");

    private String code;
    private String msg;

    PlatformEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
