// @formatter:off
package com.everhomes.rest.techpark.punch;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>process: 进度百分比100%的时候刷新页面</li>
 * </ul>
 */
public class GetMonthReportProcessResponse {

	private Integer process;

	public GetMonthReportProcessResponse() {

	}

	public GetMonthReportProcessResponse(Integer process) {
		super();
		this.process = process;
	}

	public Integer getProcess() {
		return process;
	}

	public void setProcess(Integer process) {
		this.process = process;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
