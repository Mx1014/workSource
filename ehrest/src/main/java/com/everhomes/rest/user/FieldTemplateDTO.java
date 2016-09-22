package com.everhomes.rest.user;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class FieldTemplateDTO {

	@ItemType(FieldDTO.class)
	private List<FieldDTO> fields;

	
	public List<FieldDTO> getFields() {
		return fields;
	}


	public void setFields(List<FieldDTO> fields) {
		this.fields = fields;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
