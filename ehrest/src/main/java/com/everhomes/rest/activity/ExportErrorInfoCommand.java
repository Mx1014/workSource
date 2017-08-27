//@formatter:off
package com.everhomes.rest.activity;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>jobId: 任务id</li>
 * </ul>
 */
public class ExportErrorInfoCommand {
	private Long jobId;

	public Long getJobId() {
		return jobId;
	}

	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
