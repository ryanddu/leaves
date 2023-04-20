package io.github.ryanddu.utils;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.Md5Crypt;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * 加密工具
 *
 * @author: ryan
 * @date: 2023/3/27 9:44
 **/
public class EncryptUtils {

	/**
	 * 加密解密算法 执行一次加密，两次解密    
	 */
	public static Key DEFAULT_KEY = null;

	public static final String DEFAULT_SECRET_KEY1 = "?:P)(OL><KI*&UJMNHY^%TGBVFR$#EDCXSW@!QAZ";

	public static final String DEFAULT_SECRET_KEY2 = "1qaz2wsx3edc4rfv5tgb6yhn7ujm8ik,9ol.0p;/";

	public static final String DEFAULT_SECRET_KEY3 = "!QAZ@WSX#EDC$RFV%TGB^YHN&UJM*IK<(OL>)P:?";

	public static final String DEFAULT_SECRET_KEY4 = "1qaz@WSX3edc$RFV5tgb^YHN7ujm*IK<9ol.)P:?";

	public static final String DEFAULT_SECRET_KEY5 = "!QAZ2wsx#EDC4rfv%TGB6yhn&UJM8ik,(OL>0p;/";

	public static final String DEFAULT_SECRET_KEY6 = "1qaz2wsx3edc4rfv5tgb^YHN&UJM*IK<(OL>)P:?";

	public static final String DEFAULT_SECRET_KEY = DEFAULT_SECRET_KEY1;

	public static final String DES = "DES";

	public static final Base32 base32 = new Base32();

	static {
		DEFAULT_KEY = obtainKey(DEFAULT_SECRET_KEY);
	}

	/**
	 * 获得key
	 **/
	public static Key obtainKey(String key) {
		if (key == null) {
			return DEFAULT_KEY;
		}
		KeyGenerator generator = null;
		try {
			generator = KeyGenerator.getInstance(DES);
		}
		catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		generator.init(new SecureRandom(key.getBytes()));
		Key key1 = generator.generateKey();
		generator = null;
		return key1;
	}

	/**
	 * 加密<br>
	 * String明文输入,String密文输出
	 */
	public static String encode(String str) {
		return encode64(null, str);
	}

	/**
	 * 加密<br>
	 * String明文输入,String密文输出
	 */
	public static String encode64(String key, String str) {
		return Base64.encodeBase64URLSafeString(obtainEncode(key, str.getBytes()));
	}

	/**
	 * 加密<br>
	 * String明文输入,String密文输出
	 */
	public static String encode32(String key, String str) {
		return base32.encodeAsString(obtainEncode(key, str.getBytes())).replaceAll("=", "");
	}

	/**
	 * 加密<br>
	 * String明文输入,String密文输出
	 */
	public static String encode16(String key, String str) {
		return Hex.encodeHexString(obtainEncode(key, str.getBytes()));
	}

	/**
	 * 解密<br>
	 * 以String密文输入,String明文输出
	 */
	public static String decode(String str) {
		return decode64(null, str);
	}

	/**
	 * 解密<br>
	 * 以String密文输入,String明文输出
	 */
	public static String decode64(String key, String str) {
		return new String(obtainDecode(key, Base64.decodeBase64(str)));
	}

	/**
	 * 解密<br>
	 * 以String密文输入,String明文输出
	 */
	public static String decode32(String key, String str) {
		return new String(obtainDecode(key, base32.decode(str)));
	}

	/**
	 * 解密<br>
	 * 以String密文输入,String明文输出
	 */
	public static String decode16(String key, String str) {
		try {
			return new String(obtainDecode(key, Hex.decodeHex(str.toCharArray())));
		}
		catch (DecoderException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 加密<br>
	 * 以byte[]明文输入,byte[]密文输出
	 */
	private static byte[] obtainEncode(String key, byte[] str) {
		byte[] byteFina = null;
		Cipher cipher;
		try {
			Key key1 = obtainKey(key);
			cipher = Cipher.getInstance(DES);
			cipher.init(Cipher.ENCRYPT_MODE, key1);
			byteFina = cipher.doFinal(str);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			cipher = null;
		}
		return byteFina;
	}

	/**
	 * 解密<br>
	 * 以byte[]密文输入,以byte[]明文输出
	 */
	private static byte[] obtainDecode(String key, byte[] str) {
		Cipher cipher;
		byte[] byteFina = null;
		try {
			Key key1 = obtainKey(key);
			cipher = Cipher.getInstance(DES);
			cipher.init(Cipher.DECRYPT_MODE, key1);
			byteFina = cipher.doFinal(str);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			cipher = null;
		}
		return byteFina;
	}

	/**
	 * 加密
	 * @param val
	 * @return
	 */
	public static String encrypt(String val) {
		String m = encode64(DEFAULT_SECRET_KEY2, val);
		String m1 = encode32(DEFAULT_SECRET_KEY3, m);
		return m1;
	}

	/**
	 * 解密
	 * @param val
	 * @return
	 */
	public static String decrypt(String val) {
		String n = decode32(DEFAULT_SECRET_KEY3, val);
		String n1 = decode64(DEFAULT_SECRET_KEY2, n);
		return n1;
	}

	/**
	 * 将字符串进行 MD5 Hash 散列
	 * @param str 待散列字符串.
	 * @param salt 散列盐.
	 * @return 字符串散列值.
	 */
	public static String hashMD5(String str, String salt) {

		return Md5Crypt.md5Crypt(str.getBytes(), salt);

	}

	public static void main(String[] args) {
		// 加密
		String m = encrypt("12345djuapohdyp9a8TGSFP97AUTSGFSYFSF");
		System.out.println("加密结果：" + m);
		// 解密
		String n = decrypt(m);
		System.out.println("解密结果：" + n);

	}

}
