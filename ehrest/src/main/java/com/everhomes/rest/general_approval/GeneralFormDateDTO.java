package com.everhomes.rest.general_approval;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>type: 类型 {@link com.everhomes.rest.general_approval.GeneralFormDateType} </li>
 * </ul>
 * @author janson
 *
 */
public class GeneralFormDateDTO {
	private String type;
 

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}
 
}
