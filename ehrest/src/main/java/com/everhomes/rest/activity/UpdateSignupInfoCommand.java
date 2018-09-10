//@formatter:off
package com.everhomes.rest.activity;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.general_approval.PostApprovalFormItem;
import com.everhomes.util.StringHelper;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

import java.util.List;

/**
 * 
 * <ul>
 * <li>id: id</li>
 * <li>values: 报名中，每项对应的值{@link PostApprovalFormItem} </li>
 * <li>checkinFlag: 是否签到，1是0否</li>
 * </ul>
 */
public class UpdateSignupInfoCommand {
	private Long id;
	@ItemType(PostApprovalFormItem.class)
	List<PostApprovalFormItem> values;
	private Byte checkinFlag;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<PostApprovalFormItem> getValues() {
		return values;
	}

	public void setValues(List<PostApprovalFormItem> values) {
		this.values = values;
	}

	public Byte getCheckinFlag() {
		return checkinFlag;
	}

	public void setCheckinFlag(Byte checkinFlag) {
		this.checkinFlag = checkinFlag;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
