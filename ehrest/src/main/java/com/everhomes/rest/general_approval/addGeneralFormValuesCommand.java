package com.everhomes.rest.general_approval;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul> 提交表单信息
 * <li>generalFormId 具体审批项的 ID </li>
 * <li>values: 审批项中，每项对应的值{@link PostApprovalFormItem} </li>
 * </ul>
 * @author janson
 *
 */
public class addGeneralFormValuesCommand {
	private Long generalFormId;
	private String sourceType;
	private Long sourceId;

	@ItemType(PostApprovalFormItem.class)
	List<PostApprovalFormItem> values;

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public Long getSourceId() {
		return sourceId;
	}

	public void setSourceId(Long sourceId) {
		this.sourceId = sourceId;
	}

	public List<PostApprovalFormItem> getValues() {
		return values;
	}

	public void setValues(List<PostApprovalFormItem> values) {
		this.values = values;
	}

	public Long getGeneralFormId() {
		return generalFormId;
	}

	public void setGeneralFormId(Long generalFormId) {
		this.generalFormId = generalFormId;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
