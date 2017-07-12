package com.everhomes.rest.quality;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>ownerId: 任务所属组织等的id</li>
 *  <li>ownerType: 任务所属组织类型，如enterprise</li>
 *  <li>targetId: 任务所属项目等的id</li>
 *  <li>targetType: 任务所属项目类型，如community</li>
 *  <li>startTime: 起始月份  </li>
 *  <li>endTime: 截止月份  </li>
 *  <li>offset: 偏移</li>
 *  <li>pageSize: 页面大小</li>
 *  <li>sampleId: 任务所属例行检查id</li>
 * </ul>
 */
public class CountTasksCommand {

	@NotNull
	private Long ownerId;
	
	@NotNull
	private String ownerType;
	
	private Long targetId;
	
	private String targetType;
	
	private Long startTime;
	
	private Long endTime;
	
	private Integer offset;
	
	private Integer pageSize;

	private Long sampleId;

	public Long getSampleId() {
		return sampleId;
	}

	public void setSampleId(Long sampleId) {
		this.sampleId = sampleId;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public Long getTargetId() {
		return targetId;
	}

	public void setTargetId(Long targetId) {
		this.targetId = targetId;
	}

	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
