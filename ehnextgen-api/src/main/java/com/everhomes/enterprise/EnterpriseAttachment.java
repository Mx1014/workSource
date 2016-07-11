package com.everhomes.enterprise;

import com.everhomes.server.schema.tables.pojos.EhEnterpriseAttachments;

public class EnterpriseAttachment extends EhEnterpriseAttachments {

	private static final long serialVersionUID = -6089951792515971474L;

	private String contentUrl;

	public String getContentUrl() {
		return contentUrl;
	}

	public void setContentUrl(String contentUrl) {
		this.contentUrl = contentUrl;
	}
}
