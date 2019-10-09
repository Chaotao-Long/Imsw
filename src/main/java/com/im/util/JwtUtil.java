package com.im.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.internal.com.fasterxml.jackson.databind.ObjectMapper;
import com.auth0.jwt.internal.org.apache.commons.codec.binary.Base64;

/**
 * @author riversky E-mail:riversky@126.com
 * @version 创建时间 ： 2018/1/22.
 */
public class JwtUtil {

	/**
	 * 密钥
	 */
	private static final String SECRET = generalKey().toString();
	/**
	 * 默认字段key:exp 存期限
	 */
	private static final String EXP = "exp";
	/**
	 * 默认字段key:payload 存数据
	 */
	private static final String PAYLOAD = "payload";

	/**
	 * 加密
	 * 
	 * @param object  加密数据
	 * @param maxTime 有效期（毫秒数）
	 * @param <T>
	 * @return
	 */
	public static <T> String encode(T object, long maxTime) {
		try {
			final JWTSigner signer = new JWTSigner(SECRET);
			final Map<String, Object> data = new HashMap<>(10);
			ObjectMapper objectMapper = new ObjectMapper();
			String jsonString = objectMapper.writeValueAsString(object);
			data.put(PAYLOAD, jsonString);
			data.put(EXP, System.currentTimeMillis() + maxTime);
			return signer.sign(data);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 数据解密
	 * 
	 * @param jwt    解密数据
	 * @param tClass 解密类型
	 * @param <T>
	 * @return
	 */
	public static <T> T decode(String jwt, Class<T> tClass) {
		final JWTVerifier jwtVerifier = new JWTVerifier(SECRET);
		try {
			final Map<String, Object> data = jwtVerifier.verify(jwt);
			// 判断数据是否超时或者符合标准
			if (data.containsKey(EXP) && data.containsKey(PAYLOAD)) {
				long exp = (long) data.get(EXP);
				long currentTimeMillis = System.currentTimeMillis();
				if (exp > currentTimeMillis) { // token未过期
					String json = (String) data.get(PAYLOAD);
					ObjectMapper objectMapper = new ObjectMapper();
					return objectMapper.readValue(json, tClass);
				}
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 由字符串生成加密key
	 * 
	 * @return
	 */
	public static SecretKey generalKey() {
		String stringKey = "7786df7fc3a34e26a61c034d5ec8245d";// 本地配置文件中加密的密文7786df7fc3a34e26a61c034d5ec8245d
		byte[] encodedKey = Base64.decodeBase64(stringKey.getBytes());// 本地的密码解码[B@152f6e2
		//System.out.println(encodedKey);// [B@152f6e2
		// System.out.println(Base64.encodeBase64URLSafeString(encodedKey));//7786df7fc3a34e26a61c034d5ec8245d
		SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");// 根据给定的字节数组使用AES加密算法构造一个密钥，使用
																					// encodedKey中的始于且包含 0 到前 leng
																					// 个字节这是当然是所有。（后面的文章中马上回推出讲解Java加密和解密的一些算法）
		return key;
	}

//	public static void main(String[] args) {
////        有效期10秒
////        加密：
//		User user = new User();
//		user.setId(1);
//		user.setUsername("Long");
//		user.setPassword("123");
//		String token = encode(user, 7200);
//		System.out.println("token：" + token);
////      解密
//		User user1 = decode(token, User.class);
//		System.out.println(user1);
//	}
}
