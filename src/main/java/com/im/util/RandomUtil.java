package com.im.util;

import java.util.Random;

public class RandomUtil {

	// 设置群号长度为9位
	private static final int factor = 9;
//	
//	/**
//	 * 效率高
//	 * 随机生产 factor 位的数字，最大不超过 19位，因为long的最大值为19位
//	 * 
//	 * @param factor
//	 * @return String
//	 */
//	public static Long randomNum() {
//		return new Double((Math.random() + 1) * Math.pow(10, factor - 1)).longValue();
//	}
//	

	public static String characters = "0123456789";
	public static String randomStr() {
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < factor; i++) {
			// nextInt(10) = [0, 10)
			sb.append(characters.charAt(random.nextInt(10)));
		}
		return sb.toString();
	}
	

}
