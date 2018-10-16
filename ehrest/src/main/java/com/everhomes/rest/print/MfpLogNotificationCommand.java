package com.everhomes.rest.print;

import com.everhomes.util.StringHelper;

public class MfpLogNotificationCommand {
	
	private String jobData;
	
	public String getJobData() {
		return jobData;
	}

	public void setJobData(String jobData) {
		this.jobData = jobData;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
