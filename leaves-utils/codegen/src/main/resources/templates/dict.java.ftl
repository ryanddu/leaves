package com.hashland.gpay.common.base.constant.enums;

import com.hashland.gpay.common.core.constant.enums.DictionaryEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
* <p>功能描述</p>
*
*
* @since 2019-08-02
*/
@Getter
@ToString
@AllArgsConstructor
public enum GenderDictEnum implements DictionaryEnum<Integer> {

    /**
    * 男
    */
    MALE(1, "男"),

    /**
    * 女
    */
    FEMALE(2, "女")
    ;

    private Integer value;
    private String desc;

    /**
    * value -> {@link GenderDictEnum}, 允许返回空
    *
    * @param value
    * @return
    */
    public static GenderDictEnum parseOfNullable(Integer value) {
    if (value != null) {
    for (GenderDictEnum e : values()) {
    if (e.value.equals(value)) {
    return e;
    }
    }
    }
    return null;
    }

    /**
    * value -> name, 允许返回空
    *
    * @param value
    * @return
    */
    public static String parseOfNameNullable(Integer value) {
    GenderDictEnum enable = parseOfNullable(value);
    return enable == null ? null : enable.getDesc();
    }
}