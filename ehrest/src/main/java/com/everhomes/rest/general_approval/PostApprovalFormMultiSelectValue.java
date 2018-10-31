package com.everhomes.rest.general_approval;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>selected: 所选中的值</li>
 * </ul>
 * @author janson
 *
 */
public class PostApprovalFormMultiSelectValue {
	@ItemType(String.class)
	private List<String> selected;

	public List<String> getSelected() {
		return selected;
	}

	public void setSelected(List<String> selected) {
		this.selected = selected;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
