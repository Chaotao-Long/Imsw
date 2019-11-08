package com.im.util;

import java.util.HashMap;
import java.util.Map;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;

/**
 * @author riversky E-mail:riversky@126.com
 * @version 创建时间 ： 2018/1/22.
 */
public class JwtUtil {

	/**
	 * 密钥
	 */
	private static final String SECRET = "7786df7fc3a34e26a61c034d5ec8245d"; // generalKey().toString();
	/**
	 * "XX#$%()(#*!()!KL<><MQLMNQNQJQK sdfkjsdrow32234545fdf>?N<:{LWPW" 默认字段key:exp
	 * 存期限
	 */
	//private static final String EXP = "exp";
	/**
	 * 默认字段key:payload 存数据
	 */
	private static final String PAYLOAD = "payload";

	public static final long maxTime = 86400000;   //一天

	/**
	 * 数据解密：
	 * 用户名->签名
	 * 传入用户名
	 * 加上密钥，用map结构存储，用sign（）签名算法将map变为字符串
	 * 传出sign签名
	 * 
	 * @param object  加密数据
	 * @param maxTime 有效期（毫秒数）
	 * @param <T>
	 * @return
	 */
	public static String sign(String username) {
		try {
			final JWTSigner signer = new JWTSigner(SECRET); // 根据密钥获取签名工具
			final Map<String, Object> data = new HashMap<String, Object>(); // 初始化映射表map的数据结构来存储数据 //HashMap<>(10);
			data.put(PAYLOAD, username); // 将对象属性数据存入map中
			// data.put(EXP, System.currentTimeMillis() + maxTime); // 将过期时间存入map中，并封装数据
			return signer.sign(data); // 将封装好的数据用签名工具进行签名（加密）
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void main(String[] args) {
		String sign = sign("Mr.Tom");
		String sign3 = sign("Mr.Tom1");
		String sign2 = sign("Miss.Han");
		System.out.println(sign);
		System.out.println(sign2);
		if (sign.equals(sign3)) {
			System.out.println("true");
		} else {
			System.out.println("false");
		}

	}

	/**
	 * 数据解密：
	 * 签名->用户名
	 * 传入签名
	 * 加上密钥，通过jwtVerifier.verify将签名字符串变为map结构
	 * 取出map中的key对应的value，返回用户名
	 * 
	 * @param jwt    解密数据
	 * @param tClass 解密类型
	 * @param <T>
	 * @return
	 */
	public static String unsign(String sign) {
		final JWTVerifier jwtVerifier = new JWTVerifier(SECRET); // 获取由密钥生成的解密工具
		try {
			final Map<String, Object> data = jwtVerifier.verify(sign); // 解密，将token数据转化为map数据结构来存储，即可取出EXP和PAYLOAD
			// 判断数据是否超时或者符合标准
			if (data.containsKey(PAYLOAD)) { // 如果token包含的数据信息正确
				return (String) data.get(PAYLOAD);
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

//	/**
//	 * 加密
//	 * 
//	 * @param object  加密数据
//	 * @param maxTime 有效期（毫秒数）
//	 * @param <T>
//	 * @return
//	 */
//	public static <T> String encode(T object, long maxTime) {
//		try {
//			final JWTSigner signer = new JWTSigner(SECRET); // 根据密钥获取签名工具
//			final Map<String, Object> data = new HashMap<String, Object>(); // 初始化映射表map的数据结构来存储数据 //HashMap<>(10);
//			ObjectMapper objectMapper = new ObjectMapper();
//			String jsonString = objectMapper.writeValueAsString(object); // 将要存储的用户对象转化为json格式
//			data.put(PAYLOAD, jsonString); // 将对象属性数据存入map中
//			data.put(EXP, System.currentTimeMillis() + maxTime); // 将过期时间存入map中，并封装数据
//			return signer.sign(data); // 将封装好的数据用签名工具进行签名（加密）
//		} catch (IOException e) {
//			e.printStackTrace();
//			return null;
//		}
//	}

//	/**
//	 * 数据解密
//	 * 
//	 * @param jwt    解密数据
//	 * @param tClass 解密类型
//	 * @param <T>
//	 * @return
//	 */
//	public static <T> T decode(String jwt, Class<T> tClass) {
//		final JWTVerifier jwtVerifier = new JWTVerifier(SECRET); // 获取由密钥生成的解密工具
//		try {
//			final Map<String, Object> data = jwtVerifier.verify(jwt); // 解密，将token数据转化为map数据结构来存储，即可取出EXP和PAYLOAD
//			// 判断数据是否超时或者符合标准
//			if (data.containsKey(EXP) && data.containsKey(PAYLOAD)) { // 如果token包含的数据信息正确
//				long exp = (long) data.get(EXP);
//				long currentTimeMillis = System.currentTimeMillis();
//				if (exp > currentTimeMillis) { // token未过期
//					String json = (String) data.get(PAYLOAD); // 将对象数据用json格式化
//					ObjectMapper objectMapper = new ObjectMapper();
//					return objectMapper.readValue(json, tClass); // 将对象数据及其类传入并解密
//				}
//			}
//			return null;
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//	}
//
//	/**
//	 * 由字符串生成加密key
//	 * 
//	 * @return
//	 */
//	public static SecretKey generalKey() {
//		String stringKey = "7786df7fc3a34e26a61c034d5ec8245d";// 本地配置文件中加密的密文7786df7fc3a34e26a61c034d5ec8245d
//		byte[] encodedKey = Base64.decodeBase64(stringKey.getBytes());// 本地的密码解码[B@152f6e2
//		//System.out.println(encodedKey);// [B@152f6e2
//		// System.out.println(Base64.encodeBase64URLSafeString(encodedKey));//7786df7fc3a34e26a61c034d5ec8245d
//		SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");// 根据给定的字节数组使用AES加密算法构造一个密钥，使用
//																					// encodedKey中的始于且包含 0 到前 length
//																					// 个字节这是当然是所有。（后面的文章中马上回推出讲解Java加密和解密的一些算法）
//		return key;
//	}
//
//	public static void main(String[] args) {
////        有效期10秒
////        加密：
//		User user = new User();
//		user.setId(1);
//		user.setUsername("Long");
//		user.setPassword("123");
//		String sign = encode(user, 7200);
//		System.out.println("sign：" + sign);
////      解密
//		User user1 = decode(sign, User.class);
//		System.out.println(user1);
//	}

}
