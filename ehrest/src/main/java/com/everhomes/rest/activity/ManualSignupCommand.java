//@formatter:off
package com.everhomes.rest.activity;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.general_approval.PostApprovalFormItem;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>activityId: 活动id</li>
 * <li>phone: 手机</li>
 * <li>formOriginId: 表单formOriginId</li>
 * <li>communityName: 园区名称</li>
 * <li>values: 报名中，每项对应的值{@link PostApprovalFormItem} </li>
 * <li>payFlag: 支付标志  参考{@link com.everhomes.rest.activity.ActivityRosterPayFlag}</li>
 * </ul>
 */
public class ManualSignupCommand {
	private Long activityId;
	private String phone;
	private Long formOriginId;
	private String communityName;
	@ItemType(PostApprovalFormItem.class)
	List<PostApprovalFormItem> values;
    private String payFlag;

	public String getCommunityName() {
		return communityName;
	}

	public void setCommunityName(String communityName) {
		this.communityName = communityName;
	}

	public Long getActivityId() {
		return activityId;
	}

	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

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

	public String getPayFlag() {
		return payFlag;
	}

	public void setPayFlag(String payFlag) {
		this.payFlag = payFlag;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
