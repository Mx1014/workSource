package com.everhomes.rest.general_approval;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>forms: 子表单值列表<@link com.everhomes.rest.general_approval.PostApprovalFormFormItemValue}</li>
 * </ul>
 * @author janson
 *
 */
public class PostApprovalFormFormValue {
	@ItemType(PostApprovalFormFormItemValue.class)
	private List<PostApprovalFormFormItemValue> forms;
	 
 

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
 
}
