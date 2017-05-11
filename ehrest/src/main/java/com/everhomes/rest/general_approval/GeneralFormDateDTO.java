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
	private Integer type;
 

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }


	public Integer getType() {
		return type;
	}


	public void setType(Integer type) {
		this.type = type;
	}
}
