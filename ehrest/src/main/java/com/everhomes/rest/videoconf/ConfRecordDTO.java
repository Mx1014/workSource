package com.everhomes.rest.videoconf;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>confId：会议Id</li>
 * <li>confDate：开会时间</li>
 * <li>confTime：会议时长</li>
 * <li>people: 与会人数</li>
 * </ul>
 */
public class ConfRecordDTO {

	private Long confId;
	
	private Timestamp confDate;
	
	private Integer confTime;
	
	private Integer people;

	public Long getConfId() {
		return confId;
	}

	public void setConfId(Long confId) {
		this.confId = confId;
	}

	public Timestamp getConfDate() {
		return confDate;
	}

	public void setConfDate(Timestamp confDate) {
		this.confDate = confDate;
	}

	public Integer getConfTime() {
		return confTime;
	}

	public void setConfTime(Integer confTime) {
		this.confTime = confTime;
	}

	public Integer getPeople() {
		return people;
	}

	public void setPeople(Integer people) {
		this.people = people;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	
}
