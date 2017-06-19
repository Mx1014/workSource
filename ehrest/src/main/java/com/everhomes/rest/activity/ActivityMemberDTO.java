// @formatter:off
package com.everhomes.rest.activity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;
/**
 * 
 *<ul>
 *<li>id:登记ID</li>
 *<li>uid:用户ID</li>
 *<li>userName:用户名</li>
 *<li>userAvatar:用户头像</li>
 *<li>familyName:家庭名</li>
 *<li>familyId:家庭id</li>
 *<li>adultCount:成人数</li>
 *<li>childCount:小孩数</li>
 *<li>checkInFlag:签到标记</li>
 *<li>checkInTime:签到时间</li>
 *<li>confirmFlag:确认标记</li>
 *<li>creatorFlag:创建人标识</li>
 *<li>lotteryWinnerFlag:中奖标识</li>
 *<li>lotteryWonTime:中奖时间</li>
 *<li>phone:用户电话号码</li>
 *<li>sourceFlag:支付状态  1:自发报名, 2:后台录入 参考 {@com.everhomes.rest.activity.ActivityRosterSourceFlag }</li>
 *<li>payFlag:支付状态  0: no pay, 1:have pay, 2:refund 参考 {@link com.everhomes.rest.activity.ActivityRosterPayFlag }</li>
 *<li>orderNo:支付订单号</li>
 *<li>orderStartTime:订单开始时间，用于计算取消订单</li>
 *<li>orderExpireTime:订单开始时间，用于计算取消订单</li>
 *<li>orderCountdown:订单倒计时时间长度</li>
 *<li>vendorType:支付方式 10001: alipay, 10002: wechatpay 参考 {@link com.everhomes.rest.organization.VendorType }</li>
 *<li>payAmount:支付金额</li>
 *<li>payTime:支付时间</li>
 *<li>refundOrderNo:退款订单号</li>
 *<li>refundAmount:退款金额</li>
 *<li>refundTime:退款时间</li>
 *<li>status:订单状态 0: cancel, 1: reject, 2:normal 参考 {@link com.everhomes.rest.activity.ActivityRosterStatus }</li>
 *</ul>
 */
public class ActivityMemberDTO {
    private Long id;
    private Long uid;
    private String userName;
    private String userAvatar;
    private String familyName;
    private Long familyId;
    private Integer adultCount;
    private Integer childCount;
    private Integer checkinFlag;
    private String checkinTime;
    private Integer confirmFlag;
    private String confirmTime;
    private Integer creatorFlag;
    private Integer lotteryWinnerFlag;
    private String lotteryWonTime;
    private Byte sourceFlag;
    private Byte payFlag;
    private Long orderNo;
    private Timestamp orderStartTime;
    private Timestamp orderExpireTime;
    private Long orderCountdown;
    private String vendorType;
    private BigDecimal payAmount;
    private Timestamp payTime;
    private Long refundOrderNo;
    private BigDecimal refundAmount;
    private Timestamp refundTime;
    private Byte status;
    
    @ItemType(String.class)
    private List<String> phone;
    
    public List<String> getPhone() {
		return phone;
	}

	public void setPhone(List<String> phone) {
		this.phone = phone;
	}

	public ActivityMemberDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public Long getFamilyId() {
        return familyId;
    }

    public void setFamilyId(Long familyId) {
        this.familyId = familyId;
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


    public Integer getCheckinFlag() {
        return checkinFlag;
    }

    public void setCheckinFlag(Integer checkinFlag) {
        this.checkinFlag = checkinFlag;
    }

    public String getCheckinTime() {
        return checkinTime;
    }

    public void setCheckinTime(String checkinTime) {
        this.checkinTime = checkinTime;
    }

    public Integer getConfirmFlag() {
        return confirmFlag;
    }

    public void setConfirmFlag(Integer confirmFlag) {
        this.confirmFlag = confirmFlag;
    }

    public String getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(String confirmTime) {
        this.confirmTime = confirmTime;
    }

    public Integer getCreatorFlag() {
        return creatorFlag;
    }

    public void setCreatorFlag(Integer creatorFlag) {
        this.creatorFlag = creatorFlag;
    }

    public Integer getLotteryWinnerFlag() {
        return lotteryWinnerFlag;
    }

    public void setLotteryWinnerFlag(Integer lotteryWinnerFlag) {
        this.lotteryWinnerFlag = lotteryWinnerFlag;
    }

    public String getLotteryWonTime() {
        return lotteryWonTime;
    }

    public void setLotteryWonTime(String lotteryWonTime) {
        this.lotteryWonTime = lotteryWonTime;
    }
    
    public Byte getPayFlag() {
		return payFlag;
	}

	public void setPayFlag(Byte payFlag) {
		this.payFlag = payFlag;
	}

	public Byte getSourceFlag() {
		return sourceFlag;
	}

	public void setSourceFlag(Byte sourceFlag) {
		this.sourceFlag = sourceFlag;
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

	public Timestamp getOrderExpireTime() {
		return orderExpireTime;
	}

	public void setOrderExpireTime(Timestamp orderExpireTime) {
		this.orderExpireTime = orderExpireTime;
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

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
