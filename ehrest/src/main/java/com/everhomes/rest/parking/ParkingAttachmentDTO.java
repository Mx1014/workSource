package com.everhomes.rest.parking;

import com.everhomes.util.StringHelper;

public class ParkingAttachmentDTO {
	private Byte informationType;
	private Long id;
	private String contentType;
	private String contentUri;
	private String contentUrl;

	public Byte getInformationType() {
		return informationType;
	}
	public void setInformationType(Byte informationType) {
		this.informationType = informationType;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public String getContentUri() {
		return contentUri;
	}
	public void setContentUri(String contentUri) {
		this.contentUri = contentUri;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	public String getContentUrl() {
		return contentUrl;
	}
	public void setContentUrl(String contentUrl) {
		this.contentUrl = contentUrl;
	}
}
