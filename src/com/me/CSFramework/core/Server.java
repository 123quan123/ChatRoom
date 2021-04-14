package com.me.CSFramework.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.me.model.UserModel;
import com.me.service.IUserSvice;
import com.me.util.AESUtil;
import com.me.util.ArgumentMaker;
import com.me.util.PropertiesParse;
import com.me.util.TimeDate;

public class Server implements Runnable, INetSpeaker {
	private static final int DEFAULT_PORT = 54188;
	private static final int MAX_CLIENT_COUNT = 50;
	
	private int startRegistryId;
	private UserModel serverModel;
	
	private ServerSocket serverSocket;
	private int port;
	private volatile boolean goon;
	private Map<String, ServerConversation> conversationList;
	private HashMap<String, String> onLineUserMap;
	private int maxClientCount;

	private IUserSvice userSvice;
	private Map<String, String> aesKeyMap;
	
	private List<INetLisener> listenerList;
	private List<Socket> socketPool;

	public Server(IUserSvice userService) {
		listenerList = new ArrayList<>();
		socketPool = new ArrayList<>();
		this.userSvice = userService;
		init();
	}
	
	public int getStartRegistryId() {
		int id = startRegistryId;
		startRegistryId += 1;
		return id;
	}
	
	public Map<String, String> getAesKeyMap() {
		return aesKeyMap;
	}

	public void setAesKeyMap(Map<String, String> aesKeyMap) {
		this.aesKeyMap = aesKeyMap;
	}

	public boolean isStartup() {
		return goon;
	}
	
	public UserModel getServerModel() {
		return serverModel;
	}
	
	public Map<String, ServerConversation> getConversationList() {
		return conversationList;
	}

	public void startup() {
		if (goon == true) {
			// 服务器已启动，无需再次启动
			speakOut("服务器已启动，无需再次启动！");
			return;
		}
		try {
			speakOut(TimeDate.getCurrentTime(TimeDate.DATE_TIME) + " 服务器启动中……");
			conversationList = new HashMap<>();
			onLineUserMap = new HashMap<String, String>();
			serverSocket = new ServerSocket(port);
			speakOut(serverSocket.toString());

			goon = true;
			
			serverModel = userSvice.getServerModel(10000);
			
			getAesMap();
			new Thread(new ProcessClientConnect(), "客户端连接请求处理线程").start();
			new Thread(this, "服务器侦听线程").start();
			speakOut(TimeDate.getCurrentTime(TimeDate.DATE_TIME) + " 服务器启动成功！");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void getAesMap() {
		aesKeyMap = userSvice.getAess();
	}
	
	public void shutdown() {
		if (goon == false) {
			speakOut("服务器未启动！");
			return;
		}
		if (!conversationList.isEmpty()) {
			speakOut("尚有在线客户端，不能宕机！");
			return;
		}
		closeConversation();
		close();
		speakOut(TimeDate.getCurrentTime(TimeDate.DATE_TIME) + " 服务器已正常关闭！");
	}
	
	public void forcedown() {
		if (goon == false) {
			speakOut("服务器未启动！");
			return;
		}
		if (!conversationList.isEmpty()) {
			// 告知所有客户端，服务器强制宕机，并处理后事！
			for (ServerConversation conversation : conversationList.values()) {
				conversation.serverForcedown();
			}
		}
		speakOut(TimeDate.getCurrentTime(TimeDate.DATE_TIME) + " RMI服务器已关闭！");
		closeConversation();
		close();
		speakOut(TimeDate.getCurrentTime(TimeDate.DATE_TIME) + " 强制关闭服务器！");
	}
	
	void toOne(String resourceId, String targetId, String message) {
		// 完成向指定客户端转发消息
		ServerConversation conversation = conversationList.get(targetId);
		if (conversation == null) {
			//此时客户端未上线 可以通知 未做功能；
			conversationList.get(resourceId).notOnline(resourceId);
		}
		conversation.toOne(resourceId, message);
	}
	
	void toOther(String resourceId, String message) {
		//完成向其它所有在线客户端转发消息
		for (String id : conversationList.keySet()) {
			if (id.equals(resourceId)) {
				continue;
			}
			ServerConversation conversation = conversationList.get(id);
			try {
				String encrypt = AESUtil.encrypt(message, aesKeyMap.get(id));
				conversation.toOther(resourceId, encrypt);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	void sendPic(String resourceId, String targetId, String len) {
		// 完成向指定客户端转发消息
		ServerConversation conversation = conversationList.get(targetId);
		conversation.sendPic(resourceId, len);		
	}
	
	void sendPicInfo(String resourceId, String targetId, String encrypt) {
		// 完成向指定客户端转发消息
		ServerConversation conversation = conversationList.get(targetId);
		conversation.sendPicInfo(resourceId, targetId, encrypt);	
	}

	void confirmAcceptPic(String resourceId, String targetId) {
		ServerConversation conversation = conversationList.get(targetId);
		conversation.confirmAcceptPic(resourceId, targetId);
	}
	
	void pushOneToOnlineMap(String id, String name) {
		onLineUserMap.put(id, name);
		sendToAllClientRefresh(id);
	}
	
	void removeOneToOnlineMap(String id) {
		onLineUserMap.remove(id);
		sendToAllClientRefresh(id);
	}

	private void sendToAllClientRefresh(String downId) {
		ArgumentMaker argumentMaker = new ArgumentMaker();
		@SuppressWarnings("unchecked")
		Map<String, String> cloneMap = (Map<String, String>) onLineUserMap.clone();
		argumentMaker.setMap(cloneMap);
		if (onLineUserMap.size() >= 1) {
			for (String id : onLineUserMap.keySet()) {
				// 除去自己 
				String remove = null;
				remove = cloneMap.remove(id);
				ServerConversation conversation = conversationList.get(id);
				if (conversation == null) continue;
				String encrypt;
				try {
					encrypt = AESUtil.encrypt(argumentMaker.toJson(), aesKeyMap.get(id));
					conversation.sendOnLineMap(encrypt);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (remove != null) {
					cloneMap.put(id, remove);
				}
			}
		} else {
			speakOut(TimeDate.getCurrentTime() + "\n无人在线");
		}
	}

	UserModel loginIn(UserModel userModel) {
		//数据库操作
		UserModel model = userSvice.loginIn(userModel);
		
		if (model == null || model.getId() == null) {
			return null;
		}
		return model;
	}
	
	boolean registry(UserModel userModel) {
		return userSvice.registry(userModel);
	}
	
	
	public void killClient(String id) {
		ServerConversation conversation = conversationList.get(id);
		if (conversation == null) {
			speakOut("客户端[" + id + "]不存在！");
			return;
		}
		conversation.killConversation();
		synchronized (conversationList) {
			conversationList.remove(id);
		}
		speakOut("客户端[" + id + "]被强制下线！");
	}
	
	List<String> getOnlineClient() {
		List<String> result = new ArrayList<>();
		
		synchronized (conversationList) {
			for (String id : conversationList.keySet()) {
				result.add(id);
			}
		}
		
		return result;
	}

	public void showClients() {
		List<String> clientsIDs = getOnlineClient();
		speakOut("共在线"+ clientsIDs.size() + "人\n");
		for (int i = 0; i < clientsIDs.size(); i++) {
			speakOut("用户：" + clientsIDs.get(i) + "  ");
		}
		speakOut("\n");
	}
	
	void removeConversation(String id) {
		if (!conversationList.containsKey(id)) {
			return;
		}
		conversationList.remove(id);
	}
	
	void addConversation(String id, ServerConversation conversation) {
		if (conversationList.containsKey(id)) {
			return;
		}
		conversationList.put(id, conversation);
	}
	
	private void closeConversation() {
		conversationList.clear();
		conversationList = null;
	}
	
	private void init() {
		this.port = DEFAULT_PORT;
		this.maxClientCount = MAX_CLIENT_COUNT;
		readCfg("/lib/net.cfg.properties");
	}
	
	private void readCfg(String cfgPath) {
		InputStream is = Server.class.getResourceAsStream(cfgPath);
		if (is == null) {
			return;
		}
		PropertiesParse.loadProperties(is);
		String str = PropertiesParse.value("port");
		if (str.length() > 0) {
			this.port = Integer.valueOf(str);
		}
		
		str = PropertiesParse.value("max_client_count");
		if (str.length() > 0) {
			this.maxClientCount = Integer.valueOf(str);
		}
		
		str = PropertiesParse.value("startRegistryId");
		if (str.length() > 0) {
			this.startRegistryId = Integer.parseInt("10006");
			this.startRegistryId = Integer.valueOf(str.toString());
		}
	}
	
	public void initNetConfig(String configFilePath) {
		readCfg(configFilePath);
	}
	
	@Override
	public void run() {
		
		speakOut(TimeDate.getCurrentTime(TimeDate.DATE_TIME) + " 开始侦听客户端连接请求……");
		while (goon) {
			// 侦听客户端连接，并构成、维持一个会话线程
			try {
				Socket socket = serverSocket.accept();
				synchronized (socketPool) {
					socketPool.add(socket);
					speakOut(TimeDate.getCurrentTime(TimeDate.DATE_TIME) + "客户端[" 
					+ socket.getInetAddress() + "]请求连接……");
				}
			} catch (IOException e) {
				goon = false;
			}
		}
		close();
		
	}
	
	private void close() {
		goon = false;
		PropertiesParse.updateProperty("startRegistryId", String.valueOf(startRegistryId));
		try {
			if (serverSocket != null && !serverSocket.isClosed()) {
				serverSocket.close();
			}
		} catch (IOException e) {
		} finally {
			serverSocket = null;
			PropertiesParse.close();
		}
	}

    class ProcessClientConnect implements Runnable {

		@Override
		public void run() {
			Socket socket = null;
			while (goon) {
				synchronized (socketPool) {
					if (socketPool.isEmpty()) {
						continue;
					}
					socket = socketPool.remove(0);
				}
				String ip;
				// 应该用socket产生会话，即，ServerConversation对象！
				ServerConversation conversation = new ServerConversation(socket);
				if (conversationList.size() >= Server.this.maxClientCount) {
					conversation.send(new NetMessage()
							.setCommand(ENetCommand.OUT_OF_ROOM));
					conversation.close();
					speakOut(TimeDate.getCurrentTime(TimeDate.DATE_TIME) 
							+ "客户端[" + socket.getInetAddress() + "]因服务器满放弃连接！");
				} else {
					long curTime = System.currentTimeMillis();
					ip = conversation.getIp() + ":" + curTime;
					conversation.setServer(Server.this);
					conversation.send(new NetMessage()
							.setCommand(ENetCommand.CONNECT_SUCCESS));
					conversation.send(new NetMessage()
							.setCommand(ENetCommand.SERVER_PUK)
							.setPara(serverModel.getPublicKey()));
					speakOut(TimeDate.getCurrentTime(TimeDate.DATE_TIME) 
							+ "客户端[" + ip + "]连接成功！");
				}
				
			}
			
			socketPool.clear();
		}
		
	}

	@Override
	public void addListener(INetLisener listener) {
		if (!this.listenerList.contains(listener)) {
			this.listenerList.add(listener);
		}
	}

	@Override
	public void removeListener(INetLisener listener) {
		if (this.listenerList.contains(listener)) {
			this.listenerList.remove(listener);
		}
	}

	void speakOut(String message) {
		for (INetLisener listener : listenerList) {
			listener.dealMessage(TimeDate.getCurrentTime() + "\n " + message);
		}
	}
	
	public String getUserAesKey(String id) {
		if (aesKeyMap.containsKey(id)) {
			return aesKeyMap.get(id);
		}
		return null;
	}

	
}
