package com.me.CSFramework.core;

import java.net.Socket;

public class ClientConversation extends Communication {
	private Client client;
	private String id;

	public ClientConversation(Client client, Socket socket) {
		super(socket);
		this.client = client;
	}
	
	void setId(String id) {
		this.id = id;
	}
	
	String getId() {
		return id;
	}
	
	void toOne(String resourceId, String targetId, String message) {
		send(new NetMessage()
				.setCommand(ENetCommand.TO_ONE)
				.setAction(resourceId + "@" + targetId)
				.setPara(message));
	}
	
	void toOther(String resourceId, String message) {
		send(new NetMessage()
				.setAction(resourceId)
				.setCommand(ENetCommand.TO_OTHER)
				.setPara(message));
	}

	void loginIn(String userModel) {
		System.out.println("login : " + new NetMessage()
				.setCommand(ENetCommand.LOGIN_IN)
				.setPara(userModel));
		send(new NetMessage()
				.setCommand(ENetCommand.LOGIN_IN)
				.setPara(userModel));
	}
	
	void registry(String userModel) {
		
		NetMessage para = new NetMessage()
				.setCommand(ENetCommand.REGISTRY)
				.setPara(userModel);
		System.out.println("registry : " + para);
		send(para);
	}

	void sendPicture(String resourceId, String targetId) {
		send(new NetMessage().setCommand(ENetCommand.SEND_PIC)
				.setAction(resourceId + "@" + targetId));	
	}

	public void sendPicInfo(String resourceId, String targetId, String decrypt) {
		send(new NetMessage().setCommand(ENetCommand.SEND_PIC_INFO)
				.setAction(resourceId + "@" + targetId)
				.setPara(decrypt));
	}
	
	void offline() {
		// 向服务器发送“下线”告知，并停止侦听线程
		send(new NetMessage()
				.setCommand(ENetCommand.OFFLINE));
		close();
	}

	@Override
	public void peerAbnormalDrop() {
		// 服务器异常掉线处理！
		client.getClientAction().serverAbnormalDrop();
	}

	public void dealForceDown(NetMessage message) {
		client.getClientAction().serverForcedown();
		close();
	}
	
	public void dealToOne(NetMessage message) {
		String action = message.getAction();
		String para = message.getPara();
		client.getClientAction().privateConversation(action, para);
	}
	
	public void dealToOther(NetMessage message) {
		//action 为sourceId
		String action = message.getAction();
		String para = message.getPara();
		client.getClientAction().publicConversation(action, para);
	}
	
	public void dealOutOfRoom(NetMessage message) {
		client.getClientAction().outOfRoom();
		close();
	}
	
	public void dealConnectSuccess(NetMessage message) {
		client.getClientAction().connectSuccess();
	}
	
	public void dealLoginIn(NetMessage message) {
		setId(message.getPara());
		client.getClientAction().connectSuccess();
	}
	
	/* 
	 * 
	LOGIN_IN_FAIL,
	LOGIN_IN_SUCCESS,

	REGISTRY_FAIL,
	REGISTRY_SUCCESS,
	 * */
	public void dealLoginInFail(NetMessage message) {
		client.getClientAction().loginInFail();
	}
	
	public void dealLoginInSuccess(NetMessage message) {
		client.getClientAction().loginInSuccess(message.getPara());
	}
	
	public void dealRegistryFail(NetMessage message) {
		client.getClientAction().RegistryFail();
	}
	
	public void dealRegistrySuccess(NetMessage message) {
		client.getClientAction().RegistrySuccess(message.getPara());
	}
	
	//SERVER_PUK
	public void dealServerPuk(NetMessage message) {
		client.getUserModel().setServerPuk(message.getPara());
	}

	public void dealSendPic(NetMessage message) {
		String[] split = message.getAction().split("@");
		client.getClientAction().receivePicHead(split[0], split[1]);
	}
	
	public void dealSendPicInfo(NetMessage message) {
		client.getClientAction().receivePicInfo(message.getAction()
				.split("@")[0], message.getPara());
	}

	//REFRESH_ONLINE_USER
	public void dealRefreshOnlineUser(NetMessage message) {
		client.getClientAction().refreshList(message.getPara());
	}
	//NOT_ONLINE
	public void dealNotOnline(NetMessage message) {
		client.getClientAction().notOnline(message.getAction());
	}
	@Override
	protected void dealNetMessage(NetMessage message) {
		System.out.println(message);
		DealNetMessage.dealCommand(this, message);
	}


}
