package com.github.ryanddu.constant.enums;

/**
 * 全局通用是否状态枚举
 *
 * @author: ryan
 * @date: 2023/3/24 17:43
 **/
public enum Whether implements BaseEnum<Integer> {

	YES(1, "是"), NO(0, "否");

	private final int value;

	private final String desc;

	Whether(int value, String desc) {
		this.value = value;
		this.desc = desc;
	}

	@Override
	public Integer getValue() {
		return this.value;
	}

	@Override
	public String getDesc() {
		return this.desc;
	}

	public static Whether fromCode(Integer value) {
		if (value == null) {
			return null;
		}
		for (Whether whether : Whether.values()) {
			if (whether.getValue().equals(value)) {
				return whether;
			}
		}
		return null;
	}

}
