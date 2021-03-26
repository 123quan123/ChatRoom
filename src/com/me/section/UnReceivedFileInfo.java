package com.me.section;

import java.util.LinkedList;
import java.util.List;

public class UnReceivedFileInfo {
	private List<SectionInfo> sections;
	
	//初始化List
	public UnReceivedFileInfo(int size) {
		sections = new LinkedList<SectionInfo>();
		sections.add(new SectionInfo(0, size));
	}
	
	private int getRightSectionIndex(SectionInfo sectionInfo) throws Exception {
		int index = 0;
		long offset = sectionInfo.getOffset();
		int size = sectionInfo.getSize();
		
		for (index = 0; index < sections.size(); index++) {
			SectionInfo info = sections.get(index);
			if (info.isRightSection(offset, size)) {
				return index;
			}
		}
		throw new Exception("片段" + sectionInfo + "异常");
	}
	
	public void afterReceiveSection(SectionInfo sectionInfo) {
		int index;
		try {
			index = getRightSectionIndex(sectionInfo);
		
			SectionInfo orgSection = sections.get(index);
			
			long orgOffset = orgSection.getOffset();
			int orgSize = orgSection.getSize();
			
			long curOffset = sectionInfo.getOffset();
			int curSize = sectionInfo.getSize();
			
			long leftOffset = orgOffset;
			int leftSize = (int) (curOffset - orgOffset);
			
			long rightOffset = curOffset + curSize;
			int rightSize = (int) (orgOffset + orgSize - rightOffset);
			
			sections.remove(index);
			if (leftSize > 0) {
				sections.add(new SectionInfo(leftOffset, leftSize));
			}
			if (rightSize > 0) {
				sections.add(new SectionInfo(rightOffset, rightSize));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean isComplete() {
		return sections.size() == 0;
	}
}
