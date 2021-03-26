package com.me.CSFramework.core;

import java.lang.reflect.Method;

public class DealNetMessage {
	public DealNetMessage() {
	}
	
	private static String getMethodName(String command) {
		StringBuffer result = new StringBuffer("deal");
		
		String words[] = command.split("_");
		int wordCount = words.length;
		for (int i = 0; i < wordCount; i++) {
			result.append(words[i].substring(0, 1).toUpperCase());
			result.append(words[i].substring(1).toLowerCase());
		}
		return result.toString();
	}
	
	public static void dealCommand(Object object, NetMessage message) {
		Class<?> klass = object.getClass();
		String methodName = getMethodName(message.getCommand().name());
		try {
			Method method = klass.getMethod(methodName, NetMessage.class);
			method.invoke(object, message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
