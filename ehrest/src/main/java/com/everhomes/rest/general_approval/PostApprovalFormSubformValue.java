package com.everhomes.rest.general_approval;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>forms: 子表单值列表 {@link com.everhomes.rest.general_approval.PostApprovalFormSubformItemValue}</li>
 * </ul>
 * @author janson
 *
 */
public class PostApprovalFormSubformValue {
	@ItemType(PostApprovalFormSubformItemValue.class)
	private List<PostApprovalFormSubformItemValue> forms;
	 
 

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }



	public List<PostApprovalFormSubformItemValue> getForms() {
		return forms;
	}



	public void setForms(List<PostApprovalFormSubformItemValue> forms) {
		this.forms = forms;
	}
 
}
