// @formatter:off
package com.everhomes.rest.activity;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;
/**
 *<ul>
 *<li>activityId:活动ID</li>
 *<li>adultCount:成人数</li>
 *<li>childCount:小孩数</li>
 *<li>phone:手机</li>
 *<li>realName:姓名</li>
 *<li>organizationId:公司Id，如果用户使用自定义的公司，则不传id仅传名称</li>
 *<li>organizationName:公司名称</li>
 *<li>position:职位</li>
 *<li>email:邮箱</li>
 *<li>payFlag: 支付标志  参考{@link com.everhomes.rest.activity.ActivityRosterPayFlag}</li>
 *<li>signupSourceFlag: 来源  参考{@link com.everhomes.rest.activity.SignupSourceFlag}</li>
 *</ul>
 */
public class ActivitySignupCommand {
    @NotNull
    private Long activityId;
    
    @NotNull
    private Integer adultCount;
    @NotNull
    private Integer childCount;
    
    private String phone;
    private String realName;
    private Long organizationId;
    private String organizationName;
    private String position;
    private String email;
    private String payFlag;
	private Byte signupSourceFlag;
    
    
    public ActivitySignupCommand() {
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
    
    public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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
