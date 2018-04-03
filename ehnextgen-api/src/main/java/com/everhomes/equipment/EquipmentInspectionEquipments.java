package com.everhomes.equipment;

import com.everhomes.server.schema.tables.pojos.EhEquipmentInspectionEquipments;
import com.everhomes.util.StringHelper;

public class EquipmentInspectionEquipments extends EhEquipmentInspectionEquipments{

	private static final long serialVersionUID = -8471061039276564577L;

	//invoke 赋值时候防止异常
	private  String attachments;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getAttachments() {
        return attachments;
    }

    public void setAttachments(String attachments) {
        this.attachments = attachments;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
