package com.me.CSFramework.core;

public class NetMessage {
	private ENetCommand command;
	
	private String action;
	
	private String para; // para应该是发送的消息 应该从这边入手
	
	NetMessage() {
	}
	
	NetMessage(String message) {
		int commandIndex = message.indexOf(':');
		String command = message.substring(0, commandIndex);
		this.command = ENetCommand.valueOf(command);
		message = message.substring(commandIndex + 1);
		commandIndex = message.indexOf(':');
		String action = message.substring(0, commandIndex);
		this.action = action == null ? " " : action;
		this.para = message.substring(commandIndex + 1);
	}

	ENetCommand getCommand() {
		return command;
	}

	NetMessage setCommand(ENetCommand command) {
		this.command = command;
		return this;
	}

	String getAction() {
		return action;
	}

	NetMessage setAction(String action) {
		this.action = action;
		return this;
	}

	String getPara() {
		return para;
	}

	NetMessage setPara(String para) {
		this.para = para;
		return this;
	}
	
	/**
	 * 将对象转化为标准格式的字符串，以便计算机理解<br>
	 * 标准格式的例子<br>
	 * TALK_TO_ONE:[空格]targetId=XXX,textContent=YYY
	 */
	@Override
	public String toString() {
		StringBuffer result = new StringBuffer(command.name());
		result.append(':').append(action == null ? " " : action)
			.append(":").append(para);
		
		return result.toString();
	}
	
	
	
}
