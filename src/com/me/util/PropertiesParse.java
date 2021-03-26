package com.me.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class PropertiesParse {
	private static final Map<String, String> propertyMap = new HashMap<>();
	private static Properties properties = new Properties();
	private static InputStream is;
	private static FileWriter fw;

	private static final String path = "F:/教主笔记/JAVAEE/JavaEE/ChatRoom/src/";
	
	public PropertiesParse() {
	}
	
	public static void loadProperties(String Path) {
		is = PropertiesParse.class.getResourceAsStream(Path);
		System.out.println("properties test is " + is);
		loadProperties(is);

		try {
			System.out.println(path + Path);
			if (Path.endsWith("net.cfg.properties")) {
				fw = new FileWriter(path + Path);
			}
		} catch (IOException e) {
			System.out.println("fw" );
		}
	}
	
	public static void loadProperties(InputStream is) {
		try {
			properties.load(is);
			Set<Object> keySet = properties.keySet();
			Iterator<Object> iterator = keySet.iterator();
			while (iterator.hasNext()){
				String key = (String) iterator.next();
				String value = properties.getProperty(key);
				propertyMap.put(key, value);
				System.out.println( key + " : " + value);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Set<String> keySet() {
		return propertyMap.keySet();
	}
	
	public static String value(String key) {
		return propertyMap.get(key);
	}
	
	public static void optionProperty(String key, String value) {
		propertyMap.put(key, value);
	}
	
	public static void updateProperty(String key, String value) {
		if (value(key) == null) {
			System.out.println("dose not have this key : " + key);
		}
		optionProperty(key, value);
		store();
	}

	public static void addProperty(String key, String value) {
		if (value(key) != null) {
			System.out.println("already have this key : " + key);
		}
		optionProperty(key, value);
		store();
	}
	
	private static void store() {
		Set<String> keySet = propertyMap.keySet();
		Iterator<String> iterator = keySet.iterator();
		while (iterator.hasNext()){
			String key = (String) iterator.next();
			System.out.println(key);
			if (key.equalsIgnoreCase("user")
					|| key.equalsIgnoreCase("driver")
					|| key.equalsIgnoreCase("password")
					|| key.equalsIgnoreCase("url")) {
				properties.remove(key);
			}
			String value = propertyMap.get(key);
			properties.setProperty(key, value);
		}
		try {
			properties.store(fw, "用户号码变更");
		} catch (IOException e) {
			System.out.println("fw xinzen error");
		}
	}

	public static void close() {
		try {
			if (is != null) {
				is.close();
			}
			if (fw != null) {
				fw.close();
			}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
}
