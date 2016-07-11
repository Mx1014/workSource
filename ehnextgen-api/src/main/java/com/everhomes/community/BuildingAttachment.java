package com.everhomes.community;

import com.everhomes.server.schema.tables.pojos.EhBuildingAttachments;

public class BuildingAttachment extends EhBuildingAttachments{

	private static final long serialVersionUID = -2405369501902842252L;
	
	private String contentUrl;

	public String getContentUrl() {
		return contentUrl;
	}

	public void setContentUrl(String contentUrl) {
		this.contentUrl = contentUrl;
	}

	
}
