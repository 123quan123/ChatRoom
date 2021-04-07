package com.me.CSFramework.core;


public interface IClientAction {
	void loginInFail();
	void loginInSuccess(String model);
	void RegistryFail();
	void RegistrySuccess(String id);
	
	void serverAbnormalDrop();
	boolean confirmOffline();
	void beforeOffline();
	void outOfRoom();
	
	void privateConversation(String resourceId, String message);
	void publicConversation(String resourceId, String message);
	
	void serverForcedown();
	void beGoneByServer();
	void afterOffline();
	void connectSuccess();

	void receivePicHead(String id, String len);
	void receivePicInfo(String string, String para);
	void refreshList(String onlineList);
	void notOnline(String id);

	void confirmAcceptPic(String id);
}
