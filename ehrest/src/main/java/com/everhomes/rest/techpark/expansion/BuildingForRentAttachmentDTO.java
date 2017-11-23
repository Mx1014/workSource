package com.everhomes.rest.techpark.expansion;


import com.everhomes.util.StringHelper;

public class BuildingForRentAttachmentDTO {

	private String contentUrl;
	
	private String contentUri;

	public String getContentUrl() {
		return contentUrl;
	}

	public void setContentUrl(String contentUrl) {
		this.contentUrl = contentUrl;
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

}
