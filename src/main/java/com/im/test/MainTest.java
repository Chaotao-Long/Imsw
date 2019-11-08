//package com.im.test;
//
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//import org.springframework.web.socket.TextMessage;
//
//public class MainTest {
//
//	public static void main(String[] args) {
//	
//		TextMessage message = new TextMessage(); 
//		name(message.getPayload());
//
////}
//	
//	public static TextMessage name(TextMessage message) {
//		
//		String oldMessageString = message.toString();
//		System.out.println("oldMessageString："+oldMessageString);
//		//可以在中括号内加上任何想要替换的字符
//		String regex="#anyone#";
//		String empty = "";//这里是将特殊字符换为empty字符串,""代表直接去掉
//		Pattern p = Pattern.compile(regex);
//		Matcher m = p.matcher(oldMessageString);//这里把想要替换的字符串传进来
//		String newMessageString = m.replaceAll(empty).trim(); //将替换后的字符串存在变量newString中
//		TextMessage newMessage = new TextMessage(newMessageString);
//		System.out.println("newMessageString："+newMessageString);
//		return newMessage;
//	}
//}
//	
//	
//	
//	
