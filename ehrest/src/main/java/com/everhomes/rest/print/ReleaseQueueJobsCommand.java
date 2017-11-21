// @formatter:off
package com.everhomes.rest.print;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>jobIds : 任务集合</li>
 * </ul>
 *
 *  @author:dengs 2017年6月16日
 */
public class ReleaseQueueJobsCommand {
	@ItemType(String.class)
	private List<String> jobIds;

	public List<String> getJobIds() {
		return jobIds;
	}

	public void setJobIds(List<String> jobIds) {
		this.jobIds = jobIds;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
