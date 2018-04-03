// @formatter:off
package com.everhomes.rest.print;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>jobsNumber : 任务数量</li>
 * </ul>
 *
 *  @author:dengs 2017年6月16日
 */
public class ListPrintingJobsResponse {
	private Integer jobsNumber;

	public ListPrintingJobsResponse(Integer jobsNumber) {
		this.jobsNumber = jobsNumber;
	}

	public ListPrintingJobsResponse() {
	}

	public Integer getJobsNumber() {
		return jobsNumber;
	}

	public void setJobsNumber(Integer jobsNumber) {
		this.jobsNumber = jobsNumber;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
}
