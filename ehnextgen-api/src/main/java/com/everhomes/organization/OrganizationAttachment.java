package com.everhomes.organization;

import com.everhomes.server.schema.tables.pojos.EhOrganizationAttachments;

public class OrganizationAttachment extends EhOrganizationAttachments {

	private static final long serialVersionUID = -6089951792515971474L;

	private String contentUrl;

	public String getContentUrl() {
		return contentUrl;
	}

	public void setContentUrl(String contentUrl) {
		this.contentUrl = contentUrl;
	}
}
