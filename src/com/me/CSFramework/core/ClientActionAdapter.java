package com.me.CSFramework.core;


public class ClientActionAdapter implements IClientAction {

	@Override
	public void serverAbnormalDrop() {
	}

	@Override
	public boolean confirmOffline() {
		return false;
	}

	@Override
	public void beforeOffline() {
		
	}

	@Override
	public void outOfRoom() {
		
	}


	@Override
	public void privateConversation(String resourceId, String message) {
		
	}

	@Override
	public void publicConversation(String resourceId, String message) {
		
	}

	@Override
	public void serverForcedown() {
		
	}

	@Override
	public void beGoneByServer() {

	}

	@Override
	public void afterOffline() {
		
	}

	@Override
	public void connectSuccess() {
		
	}

	@Override
	public void loginInFail() {
		
	}

	@Override
	public void loginInSuccess(String model) {
		
	}

	@Override
	public void RegistryFail() {
		
	}

	@Override
	public void RegistrySuccess(String id) {
		
	}

	@Override
	public void receivePicHead(String id, String len) {
		
	}

	@Override
	public void receivePicInfo(String string, String para) {
		
	}

	@Override
	public void refreshList(String onlineList) {
		
	}

	@Override
	public void notOnline(String id) {
		
	}

	@Override
	public void confirmAcceptPic(String id) {

	}


}
