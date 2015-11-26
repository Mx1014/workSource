package com.everhomes.techpark.expansion;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

public class BuildingForRentDTO {
	
	private Long id;
	
	private String rentType;
	
	private String subject;
	
	private Byte status;
	
	private Timestamp createTime;

	
	
	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public String getRentType() {
		return rentType;
	}



	public void setRentType(String rentType) {
		this.rentType = rentType;
	}



	public String getSubject() {
		return subject;
	}



	public void setSubject(String subject) {
		this.subject = subject;
	}



	public Byte getStatus() {
		return status;
	}



	public void setStatus(Byte status) {
		this.status = status;
	}



	public Timestamp getCreateTime() {
		return createTime;
	}



	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}



	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
