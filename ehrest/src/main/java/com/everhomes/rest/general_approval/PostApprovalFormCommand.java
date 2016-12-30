package com.everhomes.rest.general_approval;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul> 提交表单信息
 * <li>approvalId 具体审批项的 ID </li>
 * <li>values: 审批项中，每项对应的值 </li>
 * </ul>
 * @author janson
 *
 */
public class PostApprovalFormCommand {
	private Long approvalId;
	
	@ItemType(PostApprovalFormItem.class)
	List<PostApprovalFormItem> values;

	public Long getApprovalId() {
		return approvalId;
	}

	public void setApprovalId(Long approvalId) {
		this.approvalId = approvalId;
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
