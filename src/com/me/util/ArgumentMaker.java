package com.me.util;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class ArgumentMaker {
	//gson使用时可保证多线程安全
	public static final Gson gson = new GsonBuilder().create();
	private static final Type type = new TypeToken<Map<String, String>>() {}.getType();
	private Map<String, String> paraMap;
	
	public ArgumentMaker() {
		paraMap = new HashMap<String, String>();
	}
	
	public ArgumentMaker(String str) {
		paraMap = gson.fromJson(str, type);
	}
	
	public ArgumentMaker addArg(String name, Object value) {
		paraMap.put(name, gson.toJson(value));
		
		return this;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getValue(String name, Type type) {
		String valueString = paraMap.get(name);
		if (valueString == null) {
			return null;
		}
		
		return (T) gson.fromJson(valueString, type);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getValue(String name, Class<?> type) {
		String valueString = paraMap.get(name);
		if (valueString == null) {
			return null;
		}
		
		return (T) gson.fromJson(valueString, type);
	}
	
	public Map<String, String> getMap() {
		return paraMap;
	}
	
	public void setMap(Map<String, String> map) {
		this.paraMap = map;
	}
	
	public String toJson() {
		return gson.toJson(paraMap);
	}
}
