package com.everhomes.rest.general_form;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>limitLength: 限制长度</li>
 * </ul> 
 *
 */
public class GeneralFormNumDTO {

	private Integer limitLength;
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	public Integer getLimitLength() {
		return limitLength;
	}
	public void setLimitLength(Integer limitLength) {
		this.limitLength = limitLength;
	}
}
