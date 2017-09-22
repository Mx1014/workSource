package com.everhomes.techpark.expansion;

import com.everhomes.server.schema.tables.pojos.EhLeasePromotionAttachments;
import com.everhomes.util.StringHelper;

public class LeasePromotionAttachment extends EhLeasePromotionAttachments {

	private static final long serialVersionUID = -5133387507238752989L;

	private String contentUrl;

	public String getContentUrl() {
		return contentUrl;
	}

	public void setContentUrl(String contentUrl) {
		this.contentUrl = contentUrl;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
