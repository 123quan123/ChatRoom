package com.me.view;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.me.CSFramework.core.INetLisener;
import com.me.CSFramework.core.Server;
import com.me.service.UserService;
import com.me.util.Database;
import com.me.util.IView;
import com.me.util.PropertiesParse;
import com.me.util.ViewTool;

public class ServerView implements IView, INetLisener{
	private Server server;
	private boolean autoStartUp;
	private JFrame jfrmMainView;
	private JTextArea jtatSystem;
	private JTextField jtxtCommand;
	
	public ServerView() {
		initView();
		server = new Server(new UserService());
		server.addListener(this);
		PropertiesParse.loadProperties("/lib/net.cfg.properties");
		Database.loadDatabaseConfig("/lib/database.cfg.properties");
		String autoStartUp = PropertiesParse.value("auto_startup");
		if (autoStartUp != null && autoStartUp.equals("true")) {
			this.autoStartUp = true;
		}
	}

	@Override
	public void closeView() {
		if (server.isStartup()) {
			ViewTool.showMessage(jfrmMainView, "请先关闭服务器！");
			return;
		}
		jfrmMainView.dispose();
	}
	
	private void dealCommand(String command) {
		if (command.equalsIgnoreCase("startup")
				|| command.equalsIgnoreCase("st")
				|| command.equals("开")) {
			server.startup();
		} else if (command.equalsIgnoreCase("shutdown")
				|| command.equalsIgnoreCase("sd")
				|| command.equals("关")) {
			server.shutdown();
		} else if (command.equalsIgnoreCase("forcedown")
				|| command.equalsIgnoreCase("fd")
				|| command.equals("强关")) {
			server.forcedown();
		} else if (command.equalsIgnoreCase("exit")
				|| command.equalsIgnoreCase("x")
				|| command.equals("退")) {
			closeView();
		} else if (command.startsWith("kill")) {
			String[] comm = command.split("@");
			if (comm[1] != null && comm[1].length() == 5) {
				server.killClient(comm[1]);
			}
		} else if (command.startsWith("showClients")) {
			server.showClients();
		}
	}

	@Override
	public void init() {
		jfrmMainView = new JFrame("Server-chatroom");
		jfrmMainView.setMinimumSize(new Dimension(1900, 1000));
		
		jtatSystem = new JTextArea();
		jtatSystem.setFont(normalFont);
		jtatSystem.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		JScrollPane jsp = new JScrollPane(jtatSystem);
		jfrmMainView.add(jsp, BorderLayout.CENTER);
		JPanel jpnlCommand = new JPanel(new FlowLayout());
		jfrmMainView.add(jpnlCommand, BorderLayout.SOUTH);
		
		JLabel jlblCommand = new JLabel("命令");
		jlblCommand.setFont(normalFont);
		jpnlCommand.add(jlblCommand);
		
		jtxtCommand = new JTextField(50);
		jtxtCommand.setFont(normalFont);
		jpnlCommand.add(jtxtCommand);
	}

	@Override
	public void reinit() {
		jtatSystem.setFocusable(false);
		jtatSystem.setEditable(false);
	}

	@Override
	public void showView() {
		jfrmMainView.setVisible(true);
		System.out.println("autoStartUp" + autoStartUp);
		if (autoStartUp) {
			server.startup();
		}
	}

	@Override
	public void dealAction() {
		jfrmMainView.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				closeView();
			}
		});
		
		jtxtCommand.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String command = jtxtCommand.getText();
				dealCommand(command);
				jtxtCommand.setText("");
			}
		});
	}
	
	@Override
	public void dealMessage(String message) {
		jtatSystem.append(message + "\n");
		jtatSystem.setCaretPosition(jtatSystem.getText().length());		
	}

	public static void main(String[] args) {
		ServerView serverView = new ServerView();
		serverView.showView();
	}

}
