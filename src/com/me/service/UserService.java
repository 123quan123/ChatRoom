package com.me.service;

import java.util.Map;

import com.me.dao.IUserDao;
import com.me.dao.UserDao;
import com.me.model.UserModel;

public class UserService implements IUserSvice {

	private IUserDao userDao;
	
	public UserService() {
		userDao = new UserDao();
	}
	
	@Override
	public Boolean registry(UserModel userModel) {
		return userDao.registry(userModel);
	}

	@Override
	public UserModel loginIn(UserModel userModel) {
		return userDao.loginIn(userModel.getId(), userModel.getAesKey());
	}

	@Override
	public String getUserPublicKey(UserModel userModel) {
		return userDao.getUserPublicKey(userModel.getId());
	}

	@Override
	public UserModel getServerModel(int id) {
		return userDao.getServerModel(id);
	}

	@Override
	public Map<String, String> getAess() {
		return userDao.getAess();
	}

}
