package com.me.util;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ViewTool {
	public static final String DeFAULT_TITLE = "提示";
	
	public static void showMessage(JFrame jframe, String message) {
		JOptionPane.showMessageDialog(jframe, message,
				"DeFAULT_TITLE", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static void showMessage(JFrame jfrm, String title, String message) {
		JOptionPane.showMessageDialog(jfrm, message, title, 
				JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static int choiceYesOrNo(JFrame jfrm, String message) {
		return JOptionPane.showConfirmDialog(jfrm, message,
				DeFAULT_TITLE, JOptionPane.YES_NO_OPTION);
		
	}
}
