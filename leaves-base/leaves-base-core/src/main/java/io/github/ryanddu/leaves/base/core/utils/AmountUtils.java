package io.github.ryanddu.leaves.base.core.utils;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 金额计算工具类
 *
 * @author: ryan
 * @date: 2023/4/12 11:27
 **/
public class AmountUtils {

	/**
	 * 分转元，保留两位小数
	 * @param amount 金额，分为单位
	 * @return 元
	 */
	public static String centToYuan(Long amount) {
		if (amount == null) {
			return null;
		}
		return BigDecimal.valueOf(amount).divide(BigDecimal.valueOf(100), 2, RoundingMode.DOWN).toString();
	}

	/**
	 * 元转分
	 * @param amount 金额，分为单位
	 * @return 元
	 */
	public static Long yuanToCent(String amount) {
		if (StringUtils.isBlank(amount)) {
			return 0L;
		}

		return BigDecimal.valueOf(Double.parseDouble(amount)).multiply(BigDecimal.valueOf(100)).longValue();
	}

}
