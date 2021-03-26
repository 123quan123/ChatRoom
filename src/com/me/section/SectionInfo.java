package com.me.section;

import java.util.Arrays;

import com.me.util.ByteString;

public class SectionInfo{
	public static final int SECTION_INFO_LEN = 12;
	
	private long offset;
	private int size;
	private byte[] value;
	
	public SectionInfo() {
	}
	
	public SectionInfo(SectionInfo info) {
		this.offset = info.getOffset();
		this.size = info.getSize();
	}
	
	public byte[] getValue() {
		return value;
	}

	public void setValue(byte[] value) {
		this.value = value;
	}


	public SectionInfo(long offset, int size) {
		this.offset = offset;
		this.size = size;
	}
	
	public SectionInfo(long offset, int size, byte[] value) {
		this.offset = offset;
		this.size = size;
		this.value = value;
	}
	
	public boolean isRightSection(long offset, int size) {
		return (this.offset <= offset) && (this.offset + this.size) >= (offset + size);
	}
	
	public long getOffset() {
		return offset;
	}

	public void setOffset(long offset) {
		this.offset = offset;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
	
	@Override
	public String toString() {
		return "SectionInfo [offset=" + offset + ", size=" + size + ", value=" + Arrays.toString(value) + "]";
	}


//	@Override
//	public String toString() {
//		StringBuffer res = new StringBuffer("fileHandle:");
//		res.append("offset:").append(this.offset).append(",")
//		.append("size:").append(this.size);
//		
//		return res.toString();
//	}	
}
