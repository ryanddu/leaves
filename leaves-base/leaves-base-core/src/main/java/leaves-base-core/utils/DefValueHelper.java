package com.github.ryanddu.utils;

import cn.hutool.core.util.StrUtil;

import java.io.Serializable;

/**
 * 默认值
 * @author: ryan
 * @date: 2023/4/19 20:42
 **/
public final class DefValueHelper {
    private DefValueHelper() {
    }

    public static String getOrDef(String val, String def) {
        return StrUtil.isEmpty(val) ? def : val;
    }

    public static <T extends Serializable> T getOrDef(T val, T def) {
        return val == null ? def : val;
    }

}
