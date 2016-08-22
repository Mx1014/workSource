package com.everhomes.equipment;

import java.util.ArrayList;
import java.util.List;

import com.everhomes.server.schema.tables.pojos.EhEquipmentInspectionTaskLogs;
import com.everhomes.util.StringHelper;

public class EquipmentInspectionTasksLogs extends EhEquipmentInspectionTaskLogs{

	private static final long serialVersionUID = -3930910931342146789L;
	
	private List<EquipmentInspectionTasksAttachments> attachments = new ArrayList<EquipmentInspectionTasksAttachments>();
	
	public List<EquipmentInspectionTasksAttachments> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<EquipmentInspectionTasksAttachments> attachments) {
		this.attachments = attachments;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
