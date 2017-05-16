package com.everhomes.rest.general_form;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.general_approval.PostApprovalFormItem;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul> 提交表单信息
 * <li>approvalId 具体审批项的 ID </li>
 * <li>values: 审批项中，每项对应的值{@link PostApprovalFormItem} </li>
 * </ul>
 * @author janson
 *
 */
public class addGeneralFormValuesCommand {
	private Long generalFormId;
	
	@ItemType(PostApprovalFormItem.class)
	List<PostApprovalFormItem> values;

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
