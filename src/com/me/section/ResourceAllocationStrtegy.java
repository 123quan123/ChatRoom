package com.me.section;

import java.util.ArrayList;
import java.util.List;

import com.me.util.ImageUtil;

public class ResourceAllocationStrtegy implements IResourceAllocationStrtegy {
	private int maxSectionLength;
	
	public ResourceAllocationStrtegy() {
		maxSectionLength = DEFAULT_MAX_SECTION_LENGTH;
	}
	
	@Override
	public void setMaxSectionLength(int maxSectionLength) {
		if (maxSectionLength > MIN_SECTION_LENGTH) {
			this.maxSectionLength = maxSectionLength;
		}
	}

	@Override
	public void allocationSection(FileSection fileSection) {

		long fileSectionSize = fileSection.getSize();
		byte[] fileValue = fileSection.getValue();
		List<SectionInfo> sectionList = new ArrayList<SectionInfo>();
		int count =  (int) (fileSectionSize / maxSectionLength) + 1;
		
		long offset = 0L;
		int restLen = (int) fileSectionSize;
		int len;
		for (int i = 0; i < count; i++) {
			len = restLen > maxSectionLength ? maxSectionLength : restLen;
			SectionInfo sectionInfo = new SectionInfo();
			sectionInfo.setOffset(offset);
			sectionInfo.setSize(len);
			byte[] value = new byte[len];
			int index = 0;
			for (int x = (int) offset; x < offset + len;) {
				value[index++] = fileValue[x++];
			}
			sectionInfo.setValue(value);
			sectionList.add(sectionInfo);
			offset += len;
			restLen -= len;
		}
		fileSection.setSectionInfoList(sectionList);
		
	}
	
	public static void main(String[] args) {
		ResourceAllocationStrtegy allocationStrtegy = new ResourceAllocationStrtegy();
		byte[] imgArray = ImageUtil.imgArray("F:\\ChatRoom的图片文件夹\\Q.bmp");
		FileSection fileSection = new FileSection((long) imgArray.length);
		fileSection.setValue(imgArray);
		System.out.println(imgArray.length);
		
		allocationStrtegy.allocationSection(fileSection);
//		for (SectionInfo sectionInfo : fileSection.getSectionInfoList()) {
//			System.out.println(sectionInfo);
//		}
	}
}
