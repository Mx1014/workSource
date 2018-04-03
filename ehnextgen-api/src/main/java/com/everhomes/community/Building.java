package com.everhomes.community;

import java.util.ArrayList;
import java.util.List;

import com.everhomes.server.schema.tables.pojos.EhBuildings;

public class Building extends EhBuildings{

	private static final long serialVersionUID = -5800525047417779384L;

	private List<BuildingAttachment> attachments = new ArrayList<BuildingAttachment>();

	public List<BuildingAttachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<BuildingAttachment> attachments) {
		this.attachments = attachments;
	}

}
