package com.everhomes.rest.general_approval;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>formOriginId : 表单id </li>
 * <li>values : 每项对应的值{@link com.everhomes.rest.general_approval.PostApprovalFormItem} </li>
 * </ul>
 *
 *  @author:dengs 2017年5月23日
 */
public class PostFormCommand {
	private Long formOriginId;
	
	@ItemType(PostApprovalFormItem.class)
	List<PostApprovalFormItem> values;

	public Long getFormOriginId() {
		return formOriginId;
	}

	public void setFormOriginId(Long formOriginId) {
		this.formOriginId = formOriginId;
	}

	public List<PostApprovalFormItem> getValues() {
		return values;
	}

	public void setValues(List<PostApprovalFormItem> values) {
		this.values = values;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
