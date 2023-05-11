package io.github.ryanddu.utils;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;

import java.util.Base64;

/**
 * AES加密工具
 *
 * @author: ryan
 * @date: 2023/4/19 20:10
 **/
public class AesUtils {

	public final static String DES = "DES";

	public final static String AES = "AES";

	/**
	 * 设置为CBC加密模式，默认情况下ECB比CBC更高效
	 */
	private final static String CBC = "/CBC/PKCS5Padding";

	public static void main(String[] args) {
		// 对称加密 密文
		/*
		 * String input = "1629431243245&endTime=1660967243244"; // DES加密算法，key的大小必须是8个字节
		 * String desKey = "12345678"; // AES加密算法，key的大小必须是16个字节 String aesKey =
		 * "1234567812345678"; String encryptDes = encryptBySymmetry(input, desKey,
		 * AesUtils.DES, true); System.out.println("DES加密:" + encryptDes); String des =
		 * decryptBySymmetry(encryptDes, desKey, AesUtils.DES, true);
		 * System.out.println("DES解密:" + des);
		 *
		 * System.out.println("\n==============================================\n");
		 *
		 * String encryptAes = encryptBySymmetry(input, aesKey, AesUtils.AES);
		 * System.out.println("AES加密:" + encryptAes); String aes =
		 * decryptBySymmetry(encryptAes, aesKey, AesUtils.AES);
		 * System.out.println("AES解密:" + aes); String token =
		 * "cwr1dcJtAOLYYZih30Jp3o/YpSCQo=";
		 *
		 * String aaa = AesUtils.decryptBySymmetry(token, "portal_e2a61c05e");
		 * System.out.println(aaa);
		 */
		String dencode = "1";
		String abs = Base64.getEncoder().encodeToString("123456789112348ghyugyugfyufgyut45645644".getBytes());
		abs = abs.substring(0, 16);
		String accessToken = AesUtils.encryptBySymmetry(dencode, abs);
		System.out.println(accessToken);
		String tokenStr = AesUtils.decryptBySymmetry(accessToken, abs);
		System.out.println(tokenStr);
	}

	/**
	 * 对称加密
	 * @param input : 密文
	 * @param key : 密钥
	 * @return
	 */

	public static String encryptBySymmetry(String input, String key) {
		SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, key.getBytes(CharsetUtil.CHARSET_UTF_8));
		// 加密
		// 加密为16进制表示
		String encryptHex = aes.encryptHex(input);
		return encryptHex;
	}

	/**
	 * 对称解密
	 * @param input : 密文
	 * @param key : 密钥
	 * @return: 原文
	 */
	public static String decryptBySymmetry(String input, String key) {
		SymmetricCrypto des = new SymmetricCrypto(SymmetricAlgorithm.AES, key.getBytes(CharsetUtil.CHARSET_UTF_8));
		String decryptStr = des.decryptStr(input);
		return decryptStr;
	}

}
