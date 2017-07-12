package com.everhomes.rest.approval;

public interface ApprovalTypeTemplateCode {
	static final String SCOPE = "approval.type";
	
	static final int ABSENCE = 1;
	static final int EXCEPTION = 2;
	static final int OVERTIME = 3; 
	
	static final String TIME_SCOPE = "time.unit";
	
	static final String DAY = "day";
	static final String HOUR = "hour";
	static final String MIN = "min";
	
	
}
