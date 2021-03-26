package com.me.CSFramework.core;

import java.net.Socket;

import com.me.model.UserModel;
import com.me.util.AESUtil;
import com.me.util.ArgumentMaker;
import com.me.util.RSAUtil;

public class ServerConversation extends Communication {
	private String id;
	private String ip;
	
	private Server server;
		
	ServerConversation(Socket socket) {
		super(socket);
		ip = socket.getInetAddress().getHostAddress();
	}
	
	String getIp() {
		return ip;
	}
	
	String getId() {
		return id;
	}
	
	void setServer(Server server) {
		this.server = server;
	}
	
	void setId(String id) {
		this.id = id;
	}
	
	void toOne(String resourceId, String message) {
		send(new NetMessage()
				.setCommand(ENetCommand.TO_ONE)
				.setAction(resourceId)
				.setPara(message));
	}
	
	void toOther(String resourceId, String message) {
		send(new NetMessage()
				.setCommand(ENetCommand.TO_OTHER)
				.setAction(resourceId)
				.setPara(message));
	}
	
	void sendPic(String resourceId, String len) {
		send(new NetMessage().setAction(resourceId + "@" + len)
				.setCommand(ENetCommand.SEND_PIC));
	}

	void sendPicInfo(String resourceId, String targetId, String encrypt) {
		send(new NetMessage().setAction(resourceId)
				.setCommand(ENetCommand.SEND_PIC_INFO)
				.setPara(encrypt));
	}

	void sendOnLineMap(String jsonMapEncrypt) {
		send(new NetMessage().setCommand(ENetCommand.REFRESH_ONLINE_USER)
				.setPara(jsonMapEncrypt));
	}

	void killConversation() {
		send(new NetMessage()
				.setCommand(ENetCommand.GONE));
		close();
	}
	
	void serverForcedown() {
		send(new NetMessage()
				.setCommand(ENetCommand.FORCE_DOWN));
		close();
	}

	@Override
	public void peerAbnormalDrop() {
		server.removeConversation(id);
		server.removeOneToOnlineMap(id);
		server.speakOut("客户端[" + id + "]异常掉线！");
	}

	public void dealOffline(NetMessage message) {
		server.removeConversation(id);
		server.removeOneToOnlineMap(id);
		server.speakOut("客户端[" + id + "]下线！");
		close();
	}

	public void notOnline(String targetId) {
		send(new NetMessage().setCommand(ENetCommand.NOT_ONLINE)
				.setAction(targetId));
	}
	
	public void dealToOther(NetMessage message) {
		try {
			String decrypt = AESUtil.decrypt(message.getPara(), server.getUserAesKey(message.getAction()));
			server.toOther(message.getAction(), decrypt);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void dealToOne(NetMessage message) {
		String[] ids = message.getAction().split("@");
		try {
			String decrypt = AESUtil.decrypt(message.getPara(), server.getUserAesKey(ids[0]));
			String encrypt = AESUtil.encrypt(decrypt, server.getUserAesKey(ids[1]));
			server.toOne(ids[0], ids[1], encrypt);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void dealLoginIn(NetMessage message) {
		try {
			String prDecrypt = RSAUtil.prDecrypt(message.getPara(), server.getServerModel().getPrivateKey());
			ArgumentMaker argumentMaker = new ArgumentMaker(prDecrypt);
			UserModel reModel = argumentMaker.getValue("com/me/model", UserModel.class);
			UserModel model = server.loginIn(reModel);
			if (model != null) {
				send(new NetMessage()
						.setCommand(ENetCommand.LOGIN_IN_SUCCESS)
						.setPara(AESUtil.encrypt(argumentMaker
						.addArg("com/me/model", model).toJson(), model.getAesKey())));
				server.getConversationList().put(model.getId(), this);
				this.id = model.getId();
				server.getAesKeyMap().put(id, model.getAesKey());
				server.pushOneToOnlineMap(model.getId(), model.getName());
			} else {
				send(new NetMessage().setCommand(ENetCommand.LOGIN_IN_FAIL));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
	
	public void dealRegistry(NetMessage message) {
		try {
			String prDecrypt = RSAUtil.prDecrypt(message.getPara(), server.getServerModel().getPrivateKey());
			ArgumentMaker argumentMaker = new ArgumentMaker(prDecrypt);
			UserModel model = argumentMaker.getValue("com/me/model", UserModel.class);
			this.id = String.valueOf(server.getStartRegistryId());
			model.setId(id);
			boolean b = server.registry(model);
			
			if (b) {
				send(new NetMessage().setCommand(ENetCommand.REGISTRY_SUCCESS)
						.setPara(id)); 
			} else {
				send(new NetMessage().setCommand(ENetCommand.REGISTRY_FAIL));
			}
		} catch (Exception e) {
			System.out.println("注册解密失败");
			e.printStackTrace();
		}
	}

	public void dealSendPic(NetMessage message) {
		String[] ids = message.getAction().split("@");
		server.sendPic(ids[0], ids[1], ids[2]);
	}
	
	public void dealSendPicInfo(NetMessage message) {
		try {
			String[] ids = message.getAction().split("@");
			String decrypt = AESUtil.decrypt(message.getPara(), server.getUserAesKey(ids[0]));
			String encrypt = AESUtil.encrypt(decrypt, server.getUserAesKey(ids[1]));
			server.sendPicInfo(ids[0], ids[1], encrypt);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void dealNetMessage(NetMessage message) {
		System.out.println(message.toString());
		DealNetMessage.dealCommand(this, message);
	}

}
