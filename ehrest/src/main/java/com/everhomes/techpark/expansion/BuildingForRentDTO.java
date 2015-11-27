package com.everhomes.techpark.expansion;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.util.StringHelper;

public class BuildingForRentDTO {
	
	private Long id;
	
	private String rentType;
	
	private String subject;
	
	private java.lang.String   rentAreas;
	
	private Byte status;
	
	private Timestamp createTime;

	private List<BuildingForRentAttachmentDTO> attachments;
	
	
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



	public List<BuildingForRentAttachmentDTO> getAttachments() {
		return attachments;
	}



	public void setAttachments(List<BuildingForRentAttachmentDTO> attachments) {
		this.attachments = attachments;
	}



	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }



	public java.lang.String getRentAreas() {
		return rentAreas;
	}



	public void setRentAreas(java.lang.String rentAreas) {
		this.rentAreas = rentAreas;
	}

}
