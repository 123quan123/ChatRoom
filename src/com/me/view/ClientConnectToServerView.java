package com.me.view;

//10001 : U4OHuo3QC6TrU19B
//10002 : 71OKGWAl4pWJ47J3
//10003 : K8OkEn9AKjywIUb9
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.me.CSFramework.core.Client;
import com.me.CSFramework.core.ClientActionAdapter;
import com.me.util.IView;
import com.me.util.ViewTool;

public class ClientConnectToServerView implements IView {
	private JFrame jfrmConnectView;
	private JLabel jlblConnectContext;
	private int count;
	
	private Client client;

	public ClientConnectToServerView() {
		this.count = 1;
		client = new Client();
		initView();
	}
	
	@Override
	public void init() {
		jfrmConnectView = new JFrame("连接服务器");
		jfrmConnectView.setSize(400, 200);
		jfrmConnectView.setLayout(new BorderLayout());
		
		JLabel jlblConnectTopic = new JLabel("连接服务器", 0);
		jlblConnectTopic.setFont(topicFont);
		jlblConnectTopic.setForeground(topicColor);
		jfrmConnectView.add(jlblConnectTopic, "North");
		
		jlblConnectContext = new JLabel("", 0);
		jlblConnectContext.setForeground(new Color(3, 59, 8));
		jlblConnectContext.setFont(normalFont);
		jfrmConnectView.add(jlblConnectContext, "Center");
	}

	@Override
	public void reinit() {
		jfrmConnectView.setLocationRelativeTo(null);
		jfrmConnectView.setResizable(false);
		jfrmConnectView.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}

	@Override
	public void dealAction() {
		jfrmConnectView.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				closeView();
			}
		});
	}

	@Override
	public void showView() {
		client.setClientAction(new ConnectServerAction());
		jfrmConnectView.setVisible(true);
		jlblConnectContext.setText("第" + this.count++ + "次连接服务器中……");
		while (!client.connectToServer()) {
			int choice = ViewTool.choiceYesOrNo(jfrmConnectView, 
					"连接服务器失败，是否继续连接？");
			if (choice == JOptionPane.YES_OPTION) {
				jlblConnectContext.setText("第" + this.count++ + "次连接服务器中……");
				continue;
			} else {
				closeView();
				break;
			}
		}
	}

	@Override
	public void closeView() {
		jfrmConnectView.dispose();
	}
	
	class ConnectServerAction extends ClientActionAdapter {

		public ConnectServerAction() {
		}

		@Override
		public void outOfRoom() {
			ViewTool.showMessage(jfrmConnectView, "服务器已满，请稍后尝试连接！");
			closeView();
		}

		@Override
		public void connectSuccess() {
			LoginView loginView = new LoginView(client);
			loginView.initView();
			loginView.showView();
			closeView();
		}
		
	}
	public static void main(String[] args) {
		new ClientConnectToServerView().showView();
	}

}
