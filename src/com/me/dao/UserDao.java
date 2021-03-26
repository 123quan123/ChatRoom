package com.me.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.me.model.UserModel;
import com.me.util.Database;

public class UserDao implements IUserDao {
	
	private Database database;
	
	public UserDao() {
		database = new Database();
	}

	@Override
	public Boolean registry(UserModel userModel) {
		ResultSet rs = database.executeQuery("SELECT id, name FROM chatroom "
				+ "where id = '" + userModel.getId() + "'");
		
		try {
			if (rs.next()) {
				return false;
			} else {
				int i = database.executeUpdate("INSERT INTO chatroom(id, name, publicKey, privateKey, aesKey) "
						+ "VALUES ('" + userModel.getId()+ "','" + userModel.getName()
						+ "','" + userModel.getPublicKey()+ "','" + userModel.getPrivateKey()+ "','" + userModel.getAesKey() + "')");
				System.out.println("insert return " + i);
				
				return i == 1;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public UserModel loginIn(String id, String aesKey) {
		ResultSet rs = database.executeQuery("SELECT * FROM chatroom "
				+ "where id = '" + id + "' and aesKey = '" + aesKey + "'");
		try {
			if (rs.next()) {
				String name = rs.getString("name");
				aesKey = rs.getString("aesKey");
				
				return new UserModel(id, name, aesKey);
			} else {
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getUserPublicKey(String id) {
		ResultSet rs = database.executeQuery("SELECT publicKey FROM chatroom "
				+ "where id = '" + id + "'");
		try {
			if (rs.next()) {
				String publicKey = rs.getString("publicKey");
				System.out.println("database : " + publicKey);
				return publicKey;
			} else {
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public UserModel getServerModel(int id) {
		System.out.println("SELECT * FROM chatroom " + "where id = '" + id + "'");
		ResultSet rs = database.executeQuery("SELECT * FROM chatroom "
				+ "where id = '" + id + "'");
		try {
			if (rs.next()) {
				String name = rs.getString("name");
				String publicKey = rs.getString("publicKey");
				String privateKey = rs.getString("privateKey");
				
				String aesKey = rs.getString("aesKey");
				return new UserModel(aesKey, name, publicKey, privateKey);
			} else {
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Map<String, String> getAess() {
		System.out.println("SELECT id, aesKey FROM chatroom " + "where id != '" + 10000 + "'");
		ResultSet rs = database.executeQuery("SELECT id, aesKey FROM "
				+ "chatroom " + "where id != '" + 10000 + "'");
		try {
			Map<String, String> aesMap = new HashMap<String, String>();
			while (rs.next()) {
				String id = rs.getString("id");
				
				String aesKey = rs.getString("aesKey");
				System.out.println(id + " : " + aesKey);
				aesMap.put(id, aesKey);
			} 
			return aesMap;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
