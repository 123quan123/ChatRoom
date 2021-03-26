package com.me.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.google.gson.Gson;
import com.me.CSFramework.core.Client;
import com.me.CSFramework.core.ClientActionAdapter;
import com.me.CSFramework.core.INetLisener;
import com.me.model.UserModel;
import com.me.section.FileSection;
import com.me.section.FileWriter;
import com.me.section.IResourceAllocationStrtegy;
import com.me.section.PictureReceive;
import com.me.section.ResourceAllocationStrtegy;
import com.me.section.SectionInfo;
import com.me.section.UnReceivedFileInfo;
import com.me.util.AESUtil;
import com.me.util.ArgumentMaker;
import com.me.util.IView;
import com.me.util.ImageUtil;
import com.me.util.TimeDate;
import com.me.util.ViewTool;

public class ChatView<E> implements IView, INetLisener {
	public static final Font boldFont = new Font("黑体", Font.BOLD, 28);
	private Client client;
	
	private JFrame jfrmChat;
	
	private JTextArea jtatChatRecord; 

	private JTextField jtf;//输入消息的框
	private JFileChooser fileChooser;
	private String DEFAULT_CURRENT_DIRECTORY_PATH = "F:\\ChatRoom的图片文件夹";

	private Map<String, String> userMap;
	
	private JButton btnSendToOne;//发送消息的按钮
	private JButton btnSendToOther;//发送消息的按钮
	private JButton btnPicture;
	private JLabel nickLab ;  //显示昵称的标签
	
	private UserModel userModel;
	private Gson gson = ArgumentMaker.gson;
	private JScrollPane jspUser;
	private JList<UserModel> jList;

	public ChatView(Client client) {
		this.client = client;
		userMap = new HashMap<String, String>();
		this.userModel = client.getUserModel();
		client.setListener(this);
		initView();
	}
	
	public static void main(String[] args) {
		new ChatView(new Client()).showView();
	}
	
	@Override
	public void init() {
		jfrmChat = new JFrame("聊天系统-chatroom");
		jfrmChat.setMinimumSize(new Dimension(840, 560));
		jfrmChat.setLocationRelativeTo(null);
		jfrmChat.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		jfrmChat.setLayout(new BorderLayout());
		jList = new JList<UserModel>();
		jspUser = new JScrollPane(jList);
		
		jtatChatRecord = new JTextArea();
		jtatChatRecord.setEditable(false);
		jtatChatRecord.setEnabled(false);
		JScrollPane jspChat = new JScrollPane(jtatChatRecord);
		
		JPanel footer = new JPanel();
		nickLab = new JLabel(userModel.getName());
		jtf = new JTextField(50); //每行字数
		footer.add(nickLab);
		footer.add(jtf);
		
		btnSendToOne = new JButton("单发");
		btnSendToOther = new JButton("群发");
		btnPicture=new JButton("图片");
		footer.add(btnSendToOne);
		footer.add(btnSendToOther);
		footer.add(btnPicture);
		
		fileChooser = new JFileChooser();
		fileChooser = new JFileChooser(DEFAULT_CURRENT_DIRECTORY_PATH);
        fileChooser.setFileFilter(new FileNameExtensionFilter("24位位图(*.bmp)", "bmp"));
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		
		JPanel jp3=new JPanel();
		footer.add(jp3);
		//将上述2个画布添加到窗口frame上
		jfrmChat.add(jspChat,BorderLayout.CENTER);
		jfrmChat.add(footer,BorderLayout.SOUTH);
		jfrmChat.add(jspUser,BorderLayout.WEST);
		
		
		btnSendToOne.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (userMap.size() == 0) {
					 ViewTool.showMessage(jfrmChat, "没有用户在线"); 
					 return;
				}

				try {
					String msg = jtf.getText();
					String[] charArray = msg.split("@");
					String targetId = charArray[0];					
					String encrypt = AESUtil.encrypt(charArray[1], client.getUserModel().getAesKey());
					
					client.toOne(userModel.getId(), targetId, encrypt);

					jtatChatRecord.append(TimeDate.getCurrentTime() + "\n "
							+ "  我向" + targetId + "说  ： " + charArray[1] + "\n");
				} catch (NoSuchAlgorithmException e2) {
					
					return;
				} catch (ArrayIndexOutOfBoundsException e1) {
					ViewTool.showMessage(jfrmChat, "单发需要加 用户 ID @");
					return; 
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		
		btnSendToOther.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (userMap.size() == 0) {
					 ViewTool.showMessage(jfrmChat, "没有用户在线"); 
					 return;
				}

				try {
					String msg = jtf.getText();

					String encrypt = AESUtil.encrypt(msg, client.getUserModel().getAesKey());
					client.toOther(userModel.getId(), encrypt);

					jtatChatRecord.append(TimeDate.getCurrentTime() + "\n "
							+ "我向所有人说 : " + msg + "\n ");
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				
			}
		});
		
		btnPicture.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (userMap.size() == 0) {
					 ViewTool.showMessage(jfrmChat, "没有用户在线"); 
					 return;
				}

				try {
					String targetId = jtf.getText().trim();
					byte[] imgStr = selectFile();
					client.sendPicture(userModel.getId(), targetId + "@" + imgStr.length);
					FileSection fileSection = new FileSection((long) imgStr.length);
					fileSection.setValue(imgStr);
					IResourceAllocationStrtegy allocationStrtegy = new ResourceAllocationStrtegy();
					allocationStrtegy.allocationSection(fileSection);
					
					for (SectionInfo sectionInfo : fileSection.getSectionInfoList()) {
						String decrypt = AESUtil.encrypt(gson .toJson(sectionInfo)
								, userModel.getAesKey());
						client.sendPicInfo(userModel.getId(), targetId, decrypt);
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		
		jList.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					UserModel model = jList.getSelectedValue();		
					if (model == null) return;
					jtf.setText(model.getId() + "@");
				}
			}
		});
	}
	
	private byte[] selectFile() {
		fileChooser.showOpenDialog(jfrmChat);
		File selectedFile = fileChooser.getSelectedFile();
		try {
			String fileName = selectedFile.getName();
	        String suffixName = fileName.substring(fileName.lastIndexOf(".") + 1);
	        if (!"bmp".equals(suffixName)) {
	            System.out.println("非BMP类型文件");
	        }
		} catch (Exception e) {
			ViewTool.showMessage(jfrmChat, "打开文件失败");
		}
		
        return prepareImageMessage(selectedFile.toString());
	}
	
	public byte[] prepareImageMessage(String path) {
		return ImageUtil.imgArray(path);
	}
	
	@Override
	public void closeView() {
		client.offline();
	}
	
	public void exitView() {
		System.out.println("jfrmChat.dispose();");
		jfrmChat.dispose();
	}

	@Override
	public void reinit() {
		
	}

	@Override
	public void showView() {
		client.setClientAction(new SendMessageAction());
//		jfrmChat.pack();// 自适应调整大小
		jfrmChat.setLocationRelativeTo(null);//居中
		jfrmChat.setDefaultCloseOperation(3);//真正关闭
		jfrmChat.setVisible(true); //显示出来
	}
	
	class SendMessageAction extends ClientActionAdapter {
		private PictureReceive pictureReceive;
		
		@Override
		public void serverAbnormalDrop() {
			ViewTool.showMessage(jfrmChat, "服务器异常宕机，服务停止！");
			exitView();
		}

		@Override
		public boolean confirmOffline() {
			int choice = ViewTool.choiceYesOrNo(jfrmChat, "是否确认下线？");
			return choice == JOptionPane.YES_OPTION;
		}

		@Override
		public void serverForcedown() {
			ViewTool.showMessage(jfrmChat, "服务器强制宕机，服务停止！");
			exitView();
		}

		@Override
		public void beGoneByServer() {
			ViewTool.showMessage(jfrmChat, "服务器强制本机下线，服务停止！");
			exitView();
		}

		@Override
		public void afterOffline() {
			exitView();
		}

		@Override
		public void privateConversation(String resourceId, String message) {
			try {

				String decrypt = AESUtil.decrypt(message, client.getUserModel().getAesKey());
				
				jtatChatRecord.append(TimeDate.getCurrentTime() + "\n " + resourceId + " : " + decrypt + "\n");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void publicConversation(String resourceId, String message) {
			try {

				String decrypt = AESUtil.decrypt(message, client.getUserModel().getAesKey());
				
				jtatChatRecord.append(TimeDate.getCurrentTime() + "\n " + resourceId + " : " + decrypt + "\n");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		@SuppressWarnings("deprecation")
		@Override
		public void receivePicHead(String id, String len) {
			int yesOrNo = ViewTool.choiceYesOrNo(jfrmChat, "是否接收来自" + id + "的图片?");
			
			if (yesOrNo == JOptionPane.YES_OPTION) {
				if (pictureReceive != null) {
					ViewTool.showMessage(jfrmChat, "正在接受其他图片，接受失败");
				} else {
					long picSize = Long.valueOf(len);
					UnReceivedFileInfo unReceivedFileInfo = new UnReceivedFileInfo((int) picSize);
					pictureReceive = new PictureReceive(unReceivedFileInfo);
				}
			} else {
				if (pictureReceive != null && pictureReceive.getFileWriter() != null) {
					FileWriter fileWriter = pictureReceive.getFileWriter();
					if (fileWriter.getThread() != null && fileWriter.getThread().isAlive()) {
						fileWriter.getThread().stop();
					}
				}
			}
		}

		@Override
		public void receivePicInfo(String resourceId, String picSectionInfo) {
			try {
				String decrypt = AESUtil.decrypt(picSectionInfo, client.getUserModel().getAesKey());
				SectionInfo sectionInfo = gson.fromJson(decrypt, SectionInfo.class); 
				pictureReceive.getSectionList().add(sectionInfo);
				pictureReceive.getUnReceivedFileInfo().afterReceiveSection(sectionInfo);
				boolean complete = pictureReceive.getUnReceivedFileInfo().isComplete();
				if (complete) {
					String path = String.valueOf(System.currentTimeMillis());
					File file = pictureReceive.setPath(path);
					FileWriter fileWriter = new FileWriter(file, pictureReceive.getSectionList());
					fileWriter.start();
					
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							while(!fileWriter.isOk()) {
							}
							ViewTool.showMessage(jfrmChat, "图片存放在" + file.toString());
							pictureReceive = null;
						}
					}).start();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void refreshList(String onlineList) {
			try {
				String JsonMap = AESUtil.decrypt(onlineList, userModel.getAesKey());
				ArgumentMaker argumentMaker = new ArgumentMaker(JsonMap);
				Map<String, String> onlineMap = argumentMaker.getMap();
				userMap = onlineMap;
				UserModel[] list = new UserModel[onlineMap.size()];
				Set<String> set = onlineMap.keySet();
		        Iterator<String> it = set.iterator();
		        int i = 0;
		        while(it.hasNext()){
		            String id = it.next();
		            String name = onlineMap.get(id);
		            list[i++] = new UserModel(id, name, "");
		        }

				jList.setListData(list);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void notOnline(String id) {
			ViewTool.showMessage(jfrmChat, id + "不在线");
		}
		
		
	}
	
	@Override
	public void dealAction() {
		jfrmChat.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				closeView();
			}
		});
	}

	@Override
	public void dealMessage(String message) {
		jtatChatRecord.append(TimeDate.getCurrentTime() + "\n " + message);
	}

}
