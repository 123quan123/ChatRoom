package com.me.section;

public interface IResourceAllocationStrtegy {
	int DEFAULT_MAX_SECTION_LENGTH = 1 << 13;
	int MIN_SECTION_LENGTH = 1 << 10;
	
	void setMaxSectionLength(int maxSectionLength);
	void allocationSection(FileSection fileSection);
}
