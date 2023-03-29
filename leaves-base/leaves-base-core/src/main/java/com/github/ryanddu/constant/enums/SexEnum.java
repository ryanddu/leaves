package com.github.ryanddu.constant.enums;

/**
 * 性别枚举
 *
 * @author: ryan
 * @date: 2023/3/27 20:19
 **/
public enum SexEnum implements BaseEnum<Integer> {

	WOMAN(0, "女"), MAN(1, "男"), UNKNOWN(2, "未知性别"), UNSPOKEN(3, "未说明的性别");

	private int value;

	private String desc;

	SexEnum(int value, String desc) {
		this.value = value;
		this.desc = desc;
	}

	public static SexEnum fromCode(Integer code) {
		if (code == null) {
			return UNSPOKEN;
		}
		switch (code) {
			case 1:
				return MAN;
			case 0:
				return WOMAN;
			case 2:
				return UNKNOWN;
			case 3:
				return UNSPOKEN;
			default:
				return UNSPOKEN;
		}
	}

	@Override
	public Integer getValue() {
		return this.value;
	}

	@Override
	public String getDesc() {
		return this.desc;
	}

}
