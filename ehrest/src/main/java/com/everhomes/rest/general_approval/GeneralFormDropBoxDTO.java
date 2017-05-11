package com.everhomes.rest.general_approval;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>selectValue: 选项list </li>
 * </ul>
 * @author janson
 *
 */
public class GeneralFormDropBoxDTO {
	@ItemType(String.class)
	private List<String> selectValue;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public List<String> getSelectValue() {
		return selectValue;
	}

	public void setSelectValue(List<String> selectValue) {
		this.selectValue = selectValue;
	}

 
}
