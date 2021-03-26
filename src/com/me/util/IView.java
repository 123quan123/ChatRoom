package com.me.util;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;

public interface IView {
	Font topicFont = new Font("微软雅黑", Font.BOLD, 30);
	Font normalFont = new Font("宋体", Font.PLAIN, 16);
	Font btnFont = new Font("宋体", Font.PLAIN, 14);
	Font smallFont = new Font("宋体", Font.PLAIN, 12);
	
	Color topicColor = new Color(15, 3, 227);
	
	Cursor cursorHand = new Cursor(Cursor.HAND_CURSOR);
	
	int PADDING = 5;
	
	default void initView() {
		init();
		reinit();
		dealAction();
	}
	
	void init();
	void reinit();
	void dealAction();
	
	void showView();
	void closeView();
}
