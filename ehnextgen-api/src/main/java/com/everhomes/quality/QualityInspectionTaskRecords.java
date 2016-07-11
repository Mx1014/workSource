package com.everhomes.quality;

import java.util.ArrayList;
import java.util.List;

import com.everhomes.server.schema.tables.pojos.EhQualityInspectionTaskRecords;
import com.everhomes.util.StringHelper;

public class QualityInspectionTaskRecords extends EhQualityInspectionTaskRecords {

	private static final long serialVersionUID = 3827403057422993994L;
	
	private List<QualityInspectionTaskAttachments> attachments = new ArrayList<QualityInspectionTaskAttachments>();
	
	public List<QualityInspectionTaskAttachments> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<QualityInspectionTaskAttachments> attachments) {
		this.attachments = attachments;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
