package com.everhomes.rest.general_approval;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>values: 字段值<@link com.everhomes.rest.general_approval.PostApprovalFormItem}</li>
 * </ul>
 * @author janson
 *
 */
public class PostApprovalFormFormItemValue {
	@ItemType(PostApprovalFormItem.class)
	private List<PostApprovalFormItem> values;
	 
 

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
 
}
