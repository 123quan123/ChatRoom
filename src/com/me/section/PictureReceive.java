package com.me.section;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.me.util.CreateFileUtil;

public class PictureReceive {
	private final static String DEFAULT_PATH = "F:\\ChatRoom的图片文件夹\\";
	private String path;
	private UnReceivedFileInfo unReceivedFileInfo;
	private FileWriter fileWriter;
	private volatile List<SectionInfo> sectionList;
	
	public PictureReceive() {
		sectionList = new ArrayList<SectionInfo>();
	}
	
	public PictureReceive(UnReceivedFileInfo unReceivedFileInfo) {
		sectionList = new ArrayList<SectionInfo>();
		setUnReceivedFileInfo(unReceivedFileInfo);
	}
	
	public String getPath() {
		return path;
	}
	
	public File setPath(String path) {
		this.path = DEFAULT_PATH + path + ".bmp";
		File file = CreateFileUtil.createFile(this.path);
		return file;
	}
	
	public List<SectionInfo> getSectionList() {
		return sectionList;
	}

	public void setSectionList(List<SectionInfo> sectionList) {
		this.sectionList = sectionList;
	}

	public UnReceivedFileInfo getUnReceivedFileInfo() {
		return unReceivedFileInfo;
	}
	
	public void setUnReceivedFileInfo(UnReceivedFileInfo unReceivedFileInfo) {
		this.unReceivedFileInfo = unReceivedFileInfo;
		
	}
	
	public FileWriter getFileWriter() {
		return fileWriter;
	}
	
	public void setFileWriter(FileWriter fileWriter) {
		this.fileWriter = fileWriter;
	}
	
	
	public boolean writeToFile(FileSection fileSection, File file) {
		for (SectionInfo sectionInfo : fileSection.getSectionInfoList()) {
			unReceivedFileInfo.afterReceiveSection(sectionInfo);
		}
		
		boolean complete = unReceivedFileInfo.isComplete();
		if (complete) {
			FileWriter fileWriter = new FileWriter(file, fileSection.getSectionInfoList());
			fileWriter.start();
			return true;
		}
		return false;
	}
	
	
}
