package com.everhomes.videoconf;

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

	private Integer confId;
	
	private Timestamp confDate;
	
	private Long confTime;
	
	private Integer people;

	public Integer getConfId() {
		return confId;
	}

	public void setConfId(Integer confId) {
		this.confId = confId;
	}

	public Timestamp getConfDate() {
		return confDate;
	}

	public void setConfDate(Timestamp confDate) {
		this.confDate = confDate;
	}

	public Long getConfTime() {
		return confTime;
	}

	public void setConfTime(Long confTime) {
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
