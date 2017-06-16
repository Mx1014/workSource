package com.everhomes.rest.general_approval;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>limitLength: 限制长度</li>
 * <li>defaultValue: 默认值 可以支持公式比如:sum(子表单1.字段A*子表单1.字段B)+sum(子表单2.字段C)+字段C/15 等等 </li>
 * </ul> 
 *
 */
public class GeneralFormNumDTO {

	private Integer limitLength;
	private String defaultValue;
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
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
}
