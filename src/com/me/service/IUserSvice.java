package com.me.service;

import java.util.Map;

import com.me.model.UserModel;

public interface IUserSvice {

	
	public Boolean registry(UserModel userModel);
	
	public UserModel loginIn(UserModel userModel);
	
	public String getUserPublicKey(UserModel userModel);

	public UserModel getServerModel(int id);

	public Map<String, String> getAess();
}
