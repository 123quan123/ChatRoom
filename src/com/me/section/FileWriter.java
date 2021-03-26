package com.me.section;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

public class FileWriter implements Runnable {
	private volatile RandomAccessFile raf;
	private volatile List<SectionInfo> sectionList;
	private Thread thread;
	private volatile boolean isOk = false;

	public FileWriter() {
	}
	
	public FileWriter(File file, List<SectionInfo> sectionList) {
		try {
			this.raf = new RandomAccessFile(file, "rw");
			this.sectionList = sectionList;
			this.thread = new Thread(this);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public FileWriter(RandomAccessFile raf, List<SectionInfo> sectionList) {
		this.raf = raf;
		this.sectionList = sectionList;
		this.thread = new Thread(this);
	}
	
	public void start() {
		this.thread.start();
	}

	public Thread getThread() {
		return thread;
	}

	@Override
	public void run() {
		for (SectionInfo section : sectionList) {
			byte[] value = section.getValue();
			long offset = section.getOffset();
			
			try {
				raf.seek(offset);
				raf.write(value);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			isOk = true;
			raf.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean isOk() {
		return isOk;
	}
	
}
