package com.me.section;

import java.util.Arrays;
import java.util.List;

/**
 * 图片的section分片
 */
public class FileSection {	
	private List<SectionInfo> sectionInfoList;
	private byte[] value;
	private long size;

	public FileSection() {
	}
	
	public FileSection(Long size) {
		this.size = size;
	}
	
	public byte[] getValue() {
		return value;
	}

	public List<SectionInfo> getSectionInfoList() {
		return sectionInfoList;
	}

	public void setSectionInfoList(List<SectionInfo> sectionInfoList) {
		this.sectionInfoList = sectionInfoList;
	}

	public void setValue(byte[] value) {
		this.value = value;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (size ^ (size >>> 32));
		result = prime * result + Arrays.hashCode(value);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FileSection other = (FileSection) obj;
		if (size != other.size)
			return false;
		if (!Arrays.equals(value, other.value))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "FileSection [value=" + Arrays.toString(value) + ", size=" + size + "]";
	}
	
}
