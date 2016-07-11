package com.everhomes.quality;

import com.everhomes.server.schema.tables.pojos.EhQualityInspectionTaskAttachments;
import com.everhomes.util.StringHelper;

public class QualityInspectionTaskAttachments extends
		EhQualityInspectionTaskAttachments {

	private static final long serialVersionUID = 1338249719499760733L;
	
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
