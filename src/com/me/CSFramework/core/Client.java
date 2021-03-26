package com.me.CSFramework.core;

import java.io.InputStream;
import java.net.Socket;

import com.me.model.UserModel;
import com.me.util.PropertiesParse;
import com.me.util.TimeDate;

public class Client {
	private static final int DEFAULT_PORT = 54188;
	private static final String DEFAULT_IP = "169.254.100.38";

	private String ip;
	private int port;
	
	private UserModel userModel;
	
	private Socket socket;
	private IClientAction clientAction;
	private ClientConversation conversation;
	private INetLisener listener;
	
	public Client() {
		userModel = new UserModel();
		init();
		setClientAction(new ClientActionAdapter());
	}
	
	public void setListener(INetLisener lisener) {
		this.listener = lisener;
	}
	
	public UserModel getUserModel() {
		return userModel;
	}

	public boolean connectToServer() {
		if (socket != null) {
			return false;
		}
		try {
			socket = new Socket(ip, port);
			conversation = new ClientConversation(this, socket);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public void offline() {
		if (clientAction.confirmOffline() == false) {
			return;
		}
		clientAction.beforeOffline();
		conversation.offline();
		clientAction.afterOffline();
	}
	
	public void toOne(String resourceId, String targetId, String message) {
		// 通过调用conversation层所提供的相关功能实现
		conversation.toOne(resourceId, targetId, message);
	}
	
	public void toOther(String resourceId, String message) {
		// 通过调用conversation层所提供的相关功能实现
		conversation.toOther(resourceId, message);
	}

	public void loginIn(String mess) {
		conversation.loginIn(mess);
	}
	
	public void registry(String  userModel) {
		conversation.registry(userModel);
	}
	
	public void sendPicture(String resourceId, String targetId) {
		conversation.sendPicture(resourceId, targetId);
	}
	
	public void sendPicInfo(String resourceId, String targetId, String decrypt) {
		conversation.sendPicInfo(resourceId, targetId, decrypt);
	}

	void setId(String id) {
		conversation.setId(id);
	}
	
	public String getId() {
		return conversation.getId();
	}
	
	public Client setIp(String ip) {
		this.ip = ip;
		return this;
	}

	public Client setPort(int port) {
		this.port = port;
		return this;
	}
	
	private void init() {
		this.port = DEFAULT_PORT;
		this.ip = DEFAULT_IP;
		readCfg("/net.cfg.properties");
	}
	
	private void readCfg(String cfgPath) {
		InputStream is = Server.class.getResourceAsStream(cfgPath);
		if (is == null) {
			return;
		}
		PropertiesParse.loadProperties(is);
		String str = PropertiesParse.value("port");
		if (str.length() > 0) {
			port = Integer.valueOf(str);
		}
		
		String ip = PropertiesParse.value("ip");
		if (ip.length() > 0) {
			this.ip = ip;
		}
	}
	
	public void initNetConfig(String configFilePath) {
		readCfg(configFilePath);
	}
	
	public Client setClientAction(IClientAction clientAction) {
		this.clientAction = clientAction;
		return this;
	}

	IClientAction getClientAction() {
		return clientAction;
	}
	
	void speakOut(String message) {
		listener.dealMessage(TimeDate.getCurrentTime() + "\n " + message);
	}

}
