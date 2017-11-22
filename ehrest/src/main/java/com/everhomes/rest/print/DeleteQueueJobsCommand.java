// @formatter:off
package com.everhomes.rest.print;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>任务列表，参考 {@link com.everhomes.rest.print.ListQueueJobsDTO}</li>
 * </ul>
 *
 */
public class DeleteQueueJobsCommand {
	@ItemType(ListQueueJobsDTO.class)
	private List<ListQueueJobsDTO> jobs;

	public List<ListQueueJobsDTO> getJobs() {
		return jobs;
	}

	public void setJobs(List<ListQueueJobsDTO> jobs) {
		this.jobs = jobs;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
