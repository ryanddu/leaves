package io.github.ryanddu.utils;

import java.math.BigDecimal;
import java.util.Random;
import java.util.UUID;

/**
 * 数据helper类
 *
 * @author: ryan
 * @date: 2023/3/27 9:43
 **/
public class DataHelper {

	/**
	 * 获取唯一ID
	 * @return
	 */
	public static String getUniqueKey() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	/**
	 * 获取两位小数
	 *
	 * @Title
	 * @Description:保存2位小数
	 * @params:DataHelper
	 * @Return:float
	 * @Author zbq
	 * @Date 2017年7月5日 上午9:25:17
	 */
	public static float getFloatValueTwo(float value) {
		float ft = value;
		int scale = 2;// 设置位数
		int roundingMode = 4;// 表示四舍五入，可以选择其他舍值方式，例如去尾，等等.
		BigDecimal bd = new BigDecimal((double) ft);
		bd = bd.setScale(scale, roundingMode);
		ft = bd.floatValue();
		return ft;
	}

	/**
	 * 获取纯数字的随机数
	 * @param len
	 * @return
	 */
	public static String getRandomNum(int len) {
		Random rd = new Random();
		StringBuilder resNum = new StringBuilder();
		do {
			int nextInt = rd.nextInt(9);
			resNum.append(nextInt);
		}
		while (len-- > 0);
		return resNum.toString();
	}

	/**
	 * 获取字母+数字的字符串
	 * @param len
	 * @return
	 */
	public static String getRandomStr(int len) {
		char[] letters = { '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
				'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'h' };
		Random rd = new Random();
		StringBuilder resNum = new StringBuilder();

		do {
			int nextInt = rd.nextInt(36);
			resNum.append(new String(letters, nextInt, 1).trim());
		}
		while (len-- > 1);
		return resNum.toString().toUpperCase();
	}

	/**
	 * 获取三位随机数
	 * @return
	 */
	public static int getRandomThreePoint() {
		return new Random().nextInt(1000);
	}

}
