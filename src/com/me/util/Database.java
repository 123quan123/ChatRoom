package com.me.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Database {
private static Connection connection;
	
	public Database() {
	}
	
	public static void loadDatabaseConfig(String path) {
		PropertiesParse.loadProperties(path);
	}
	
	private static Connection getConnection() {
		if (connection == null) {
			try {
				Class.forName(PropertiesParse.value("driver"));
				connection = DriverManager.getConnection(
						PropertiesParse.value("url"), 
						PropertiesParse.value("user"), 
						PropertiesParse.value("password"));
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return connection;
	}
	
	public int executeUpdate(String SQLString) {
		Connection connection = getConnection();
		try {
			PreparedStatement state = connection.prepareStatement(SQLString);
			return state.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return -1;
	}
	
	public ResultSet executeQuery(String SQLString) {
		ResultSet rs = null;
		Connection connection = getConnection();
		try {
			PreparedStatement state = connection.prepareStatement(SQLString);
			return state.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return rs;
	}
}
