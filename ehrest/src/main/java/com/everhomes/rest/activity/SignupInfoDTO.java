//@formatter:off
package com.everhomes.rest.activity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.general_approval.PostApprovalFormItem;
import com.everhomes.util.StringHelper;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

/**
 * 
 * <ul>
 * <li>id: id</li>
 * <li>phone: 手机</li>
 * <li>nickName: 昵称</li>
 * <li>realName:真实名称</li>
 * <li>gender: 性别，0未知1男2女，参考{@link com.everhomes.rest.user.UserGender}</li>
 * <li>communityName: 园区名称</li>
 * <li>values: values: 报名中，每项对应的值{@link PostApprovalFormItem}</li>
 * <li>type: 类型，1认证2非认证3非注册</li>
 * <li>sourceFlag: 来源，1自发报名2后台录入</li>
 * <li>confirmFlag: 报名确认，1已确认0未确认</li>
 * <li>checkinFlag: 是否已签到，1是0否</li>
 * <li>signupTime: 报名时间 yyyy-MM-dd HH:MM</li>
 * <li>email: 邮箱</li>
 * <li>order: 序号</li>
 * <li>payFlag: 支付标志  参考{@link com.everhomes.rest.activity.ActivityRosterPayFlag}</li>
 * <li>orderNo:支付订单号</li>
 *<li>orderStartTime:订单开始时间，用于计算取消订单</li>
 *<li>orderCountdown:订单倒计时时间长度</li>
 *<li>vendorType:支付方式 10001: alipay, 10002: wechatpay 参考 {@link com.everhomes.rest.organization.VendorType }</li>
 *<li>payAmount:支付金额</li>
 *<li>payTime:支付时间</li>
 *<li>refundOrderNo:退款订单号</li>
 *<li>refundAmount:退款金额</li>
 *<li>refundTime:退款时间</li>
 *<li>status:订单状态 0: cancel, 1: reject, 2:normal 参考 {@link com.everhomes.rest.activity.ActivityRosterStatus }</li>
 *<li>signupStatusText:报名状态text</li>
 * <li>activityName: 活动名称</li>
 * </ul>
 */
public class SignupInfoDTO {
	private Long id;
	private String phone;
	private String nickName;
	private String realName;
	private Byte gender;
	private String communityName;
	@ItemType(PostApprovalFormItem.class)
	List<PostApprovalFormItem> values;
	private Byte type;
	private String typeText;
	private Byte sourceFlag;
	private String sourceFlagText;
	private Byte confirmFlag;
	private String confirmFlagText;
	private Byte checkinFlag;
	private String checkinFlagText;
	private Byte createFlag;
	private String signupTime;
	private String email;
	private String order;
	private Byte payFlag;
	private Long orderNo;
    private Timestamp orderStartTime;
    private Long orderCountdown;
    private String vendorType;
    private BigDecimal payAmount;
    private Timestamp payTime;
    private Long refundOrderNo;
    private BigDecimal refundAmount;
    private Timestamp refundTime;
	private Byte status;
	private String signupStatusText;
	private String activitySubject;
	private String activityName;

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getActivitySubject() {
		return activitySubject;
	}

	public void setActivitySubject(String activitySubject) {
		this.activitySubject = activitySubject;
	}

	public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Byte getGender() {
        return gender;
    }

    public void setGender(Byte gender) {
        this.gender = gender;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public Byte getCreateFlag() {
		return createFlag;
	}
	public void setCreateFlag(Byte createFlag) {
		this.createFlag = createFlag;
	}
	public String getTypeText() {
		return typeText;
	}
	public void setTypeText(String typeText) {
		this.typeText = typeText;
	}
	public String getSourceFlagText() {
		return sourceFlagText;
	}
	public void setSourceFlagText(String sourceFlagText) {
		this.sourceFlagText = sourceFlagText;
	}
	public String getConfirmFlagText() {
		return confirmFlagText;
	}
	public void setConfirmFlagText(String confirmFlagText) {
		this.confirmFlagText = confirmFlagText;
	}
	public String getCheckinFlagText() {
		return checkinFlagText;
	}
	public void setCheckinFlagText(String checkinFlagText) {
		this.checkinFlagText = checkinFlagText;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

    public List<PostApprovalFormItem> getValues() {
        return values;
    }

    public void setValues(List<PostApprovalFormItem> values) {
        this.values = values;
    }

    public Byte getType() {
		return type;
	}
	public void setType(Byte type) {
		this.type = type;
	}
	public Byte getSourceFlag() {
		return sourceFlag;
	}
	public void setSourceFlag(Byte sourceFlag) {
		this.sourceFlag = sourceFlag;
	}
	public Byte getConfirmFlag() {
		return confirmFlag;
	}
	public void setConfirmFlag(Byte confirmFlag) {
		this.confirmFlag = confirmFlag;
	}
	public Byte getCheckinFlag() {
		return checkinFlag;
	}
	public void setCheckinFlag(Byte checkinFlag) {
		this.checkinFlag = checkinFlag;
	}
	public String getSignupTime() {
		return signupTime;
	}
	public void setSignupTime(String signupTime) {
		this.signupTime = signupTime;
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public Byte getPayFlag() {
		return payFlag;
	}
	public void setPayFlag(Byte payFlag) {
		this.payFlag = payFlag;
	}
	public Long getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(Long orderNo) {
		this.orderNo = orderNo;
	}
	public Timestamp getOrderStartTime() {
		return orderStartTime;
	}
	public void setOrderStartTime(Timestamp orderStartTime) {
		this.orderStartTime = orderStartTime;
	}
	public Long getOrderCountdown() {
		return orderCountdown;
	}
	public void setOrderCountdown(Long orderCountdown) {
		this.orderCountdown = orderCountdown;
	}
	public String getVendorType() {
		return vendorType;
	}
	public void setVendorType(String vendorType) {
		this.vendorType = vendorType;
	}
	
	public BigDecimal getPayAmount() {
		return payAmount;
	}
	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}
	public BigDecimal getRefundAmount() {
		return refundAmount;
	}
	public void setRefundAmount(BigDecimal refundAmount) {
		this.refundAmount = refundAmount;
	}
	public Timestamp getPayTime() {
		return payTime;
	}
	public void setPayTime(Timestamp payTime) {
		this.payTime = payTime;
	}
	public Long getRefundOrderNo() {
		return refundOrderNo;
	}
	public void setRefundOrderNo(Long refundOrderNo) {
		this.refundOrderNo = refundOrderNo;
	}
	
	public Timestamp getRefundTime() {
		return refundTime;
	}
	public void setRefundTime(Timestamp refundTime) {
		this.refundTime = refundTime;
	}
	public Byte getStatus() {
		return status;
	}
	public void setStatus(Byte status) {
		this.status = status;
	}

	public String getSignupStatusText() {
		return signupStatusText;
	}

	public void setSignupStatusText(String signupStatusText) {
		this.signupStatusText = signupStatusText;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
}
