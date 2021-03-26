package com.me.dao;

import java.util.Map;

import com.me.model.UserModel;

public interface IUserDao {
	
	public Boolean registry(UserModel userModel);
	
	public UserModel loginIn(String id, String password);
	
	public String getUserPublicKey(String id);

	public UserModel getServerModel(int id);

	public Map<String, String> getAess();
}
