package com.everhomes.quality;

import java.util.ArrayList;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.server.schema.tables.pojos.EhQualityInspectionTaskRecords;
import com.everhomes.util.StringHelper;

public class QualityInspectionTaskRecords extends EhQualityInspectionTaskRecords {

	private static final long serialVersionUID = 3827403057422993994L;
	@ItemType(QualityInspectionTaskAttachments.class)
	private List<QualityInspectionTaskAttachments> attachments = new ArrayList<QualityInspectionTaskAttachments>();
	@ItemType(QualityInspectionSpecificationItemResults.class)
	private List<QualityInspectionSpecificationItemResults> itemResults = new ArrayList<QualityInspectionSpecificationItemResults>();
	
	public List<QualityInspectionTaskAttachments> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<QualityInspectionTaskAttachments> attachments) {
		this.attachments = attachments;
	}
	
	public List<QualityInspectionSpecificationItemResults> getItemResults() {
		return itemResults;
	}

	public void setItemResults(
			List<QualityInspectionSpecificationItemResults> itemResults) {
		this.itemResults = itemResults;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
