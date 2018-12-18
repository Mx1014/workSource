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
public class ReleaseQueueJobsCommand {
	private String ownerType;
	private Long ownerId;
	@ItemType(ListQueueJobsDTO.class)
	private List<ListQueueJobsDTO> jobs;
	private Integer namespaceId;
	private String qrid;

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}
	
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

	public String getQrid() {
		return qrid;
	}

	public void setQrid(String qrid) {
		this.qrid = qrid;
	}
}
