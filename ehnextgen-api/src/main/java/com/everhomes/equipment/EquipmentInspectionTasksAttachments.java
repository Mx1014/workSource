package com.everhomes.equipment;

import com.everhomes.server.schema.tables.pojos.EhEquipmentInspectionTaskAttachments;
import com.everhomes.util.StringHelper;

public class EquipmentInspectionTasksAttachments extends EhEquipmentInspectionTaskAttachments{

	private static final long serialVersionUID = 6569077588275396647L;
	
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
