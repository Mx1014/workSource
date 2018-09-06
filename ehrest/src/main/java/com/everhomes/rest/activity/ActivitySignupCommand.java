// @formatter:off
package com.everhomes.rest.activity;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.general_approval.PostApprovalFormItem;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 *<ul>
 *<li>activityId:活动ID</li>
 *<li>adultCount:成人数</li>
 *<li>childCount:小孩数</li>
 *<li>formOriginId: 表单formOriginID</li>
 *<li>formId: 表单ID</li>
 *<li>values: 报名中，每项对应的值{@link PostApprovalFormItem} </li>
 *<li>payFlag: 支付标志  参考{@link com.everhomes.rest.activity.ActivityRosterPayFlag}</li>
 *<li>signupSourceFlag: 来源  参考{@link com.everhomes.rest.activity.ActivityRosterSourceFlag}</li>
 *</ul>
 */
public class ActivitySignupCommand {
    @NotNull
    private Long activityId;
    
    @NotNull
    private Integer adultCount;
    @NotNull
    private Integer childCount;

    private Long formId;
    private Long formOriginId;

	@ItemType(PostApprovalFormItem.class)
	List<PostApprovalFormItem> values;
    private String payFlag;
	private Byte signupSourceFlag;

	private String communityName;

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public ActivitySignupCommand() {
    }

    public Long getFormId() {
        return formId;
    }

    public void setFormId(Long formId) {
        this.formId = formId;
    }

    public Long getFormOriginId() {
        return formOriginId;
    }

    public void setFormOriginId(Long formOriginId) {
        this.formOriginId = formOriginId;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public Integer getAdultCount() {
        return adultCount;
    }

    public void setAdultCount(Integer adultCount) {
        this.adultCount = adultCount;
    }

    public Integer getChildCount() {
        return childCount;
    }

    public void setChildCount(Integer childCount) {
        this.childCount = childCount;
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

	public Byte getSignupSourceFlag() {
		return signupSourceFlag;
	}

	public void setSignupSourceFlag(Byte signupSourceFlag) {
		this.signupSourceFlag = signupSourceFlag;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
