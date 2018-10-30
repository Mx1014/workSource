// @formatter:off
package com.everhomes.rest.parking;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

/**
 * <ul>
 * <li>id: 停车场ID</li>
 * <li>ownerType: 归属的类型，{@link com.everhomes.rest.parking.ParkingOwnerType}</li>
 * <li>ownerId: 归属的ID，如小区ID</li>
 * <li>name: 停车场名称</li>
 * <li>vendorName: 厂商名称（用于作逻辑，不用于显示），{@link com.everhomes.rest.parking.ParkingLotVendor}</li>
 * <li>createTime: 停车场创建时间</li>
 * <li>expiredRechargeFlag: 是否支持过期充值 0：不支持，1：支持 {@link ParkingConfigFlag}</li>
 * <li>maxExpiredDay: 支持过期充值时，最多过期天数</li>
 * <li>expiredRechargeMonthCount: 支持过期充值时，至少充值几个月</li>
 * <li>expiredRechargeType: 支持过期充值时，按照什么模式充值 {@link ParkingCardExpiredRechargeType}</li>
 * <li>monthlyDiscountFlag: 月卡是否启用优惠 0：不启用，1：启用 {@link ParkingConfigFlag}</li>
 * <li>monthlyDiscount: 月卡优惠折扣</li>
 * <li>tempFeeDiscountFlag: 临时车是否启用优惠 0：不启用，1：启用 {@link ParkingConfigFlag}</li>
 * <li>tempFeeDiscount: 临时车优惠折扣</li>
 * <li>flowMode: 申请月卡工作流模式 {@link ParkingRequestFlowType}</li>
 * <li>tempfeeFlag: 是否支持临时车 0：不支持，1：支持 {@link ParkingConfigFlag}</li>
 * <li>rateFlag: 费率是否可以删除增加 0：不支持，1：支持 {@link ParkingConfigFlag}</li>
 * <li>lockCarFlag: 是否支持锁车 0：不支持，1：支持 {@link ParkingConfigFlag}</li>
 * <li>searchCarFlag: 是否支持寻车 0：不支持，1：支持 {@link ParkingConfigFlag}</li>
 * <li>currentInfoType: app首页显示车辆方式 {@link ParkingCurrentInfoType}</li>
 * <li>contact: 停车场配置客服联系方式，用在app订单异常界面，客户可以拨打联系客服</li>
 * <li>invoiceFlag: 是否支持发票 {@link ParkingConfigFlag} 0：不支持，1：支持</li>
 * <li>businessLicenseFlag: 申请月卡 是否需要 营业执照 0：不需要， 1：需要{@link ParkingConfigFlag}</li>
 * <li>identityCardFlag: 是否支持填写身份证号码 0：不支持，1：支持 {@link ParkingConfigFlag}</li>
 * <li>flowId: 工作流id</li>
 * <li>summary: 用户须知</li>
 * <li>noticeFlag: 是否开启用户须知 0：关闭，1：开启</li>
 * <li>noticeContact: 用户须知的联系电话</li>
 * </ul>
 */
public class ParkingLotDTO {
    private Long id;
    private String ownerType;
    private Long ownerId;
    private String name;
    private String vendorName;
    private Timestamp createTime;
    //是否支持过期充值
    private Byte expiredRechargeFlag;
    //支持过期充值时，最多过期天数
    private Integer maxExpiredDay;
    //支持过期充值时，至少充值几个月
    private Integer expiredRechargeMonthCount;
    //支持过期充值时，按照什么模式充值 {@link ParkingCardExpiredRechargeType}
    private Byte expiredRechargeType;

    private Byte monthlyDiscountFlag;
    private String monthlyDiscount;

    private Byte tempFeeDiscountFlag;
    private String tempFeeDiscount;

    //申请月卡的模式
    private Integer flowMode;

    //是否支持临时车缴费
    private Byte tempfeeFlag;
    //是否支持添加/删除费率
    private Byte rateFlag;
    //是否支持锁车
    private Byte lockCarFlag;
    //是否支持寻车
    private Byte searchCarFlag;
    //显示当前在场车/当前剩余车位
    private Byte currentInfoType;
    //停车场客服联系方式
    private String contact;

    private Byte invoiceFlag;

    private Byte businessLicenseFlag;

    private Byte vipParkingFlag;
    private Byte monthRechargeFlag;
    private String vipParkingUrl;
    private Byte identityCardFlag;
    private Long flowId;
    private String summary;
    private Byte noticeFlag;
    private String noticeContact;
    private String province;
    private String city;

    
    public Byte getIdentityCardFlag() {
        return identityCardFlag;
    }

    public void setIdentityCardFlag(Byte identityCardFlag) {
        this.identityCardFlag = identityCardFlag;
    }

    public Byte getBusinessLicenseFlag() {
        return businessLicenseFlag;
    }

    public void setBusinessLicenseFlag(Byte businessLicenseFlag) {
        this.businessLicenseFlag = businessLicenseFlag;
    }

    public Byte getVipParkingFlag() {
        return vipParkingFlag;
    }

    public void setVipParkingFlag(Byte vipParkingFlag) {
        this.vipParkingFlag = vipParkingFlag;
    }

    public String getVipParkingUrl() {
        return vipParkingUrl;
    }

    public void setVipParkingUrl(String vipParkingUrl) {
        this.vipParkingUrl = vipParkingUrl;
    }

    public Byte getInvoiceFlag() {
        return invoiceFlag;
    }

    public void setInvoiceFlag(Byte invoiceFlag) {
        this.invoiceFlag = invoiceFlag;
    }

    public Byte getMonthlyDiscountFlag() {
        return monthlyDiscountFlag;
    }

    public void setMonthlyDiscountFlag(Byte monthlyDiscountFlag) {
        this.monthlyDiscountFlag = monthlyDiscountFlag;
    }

    public String getMonthlyDiscount() {
        return monthlyDiscount;
    }

    public void setMonthlyDiscount(String monthlyDiscount) {
        this.monthlyDiscount = monthlyDiscount;
    }

    public String getTempFeeDiscount() {
        return tempFeeDiscount;
    }

    public void setTempFeeDiscount(String tempFeeDiscount) {
        this.tempFeeDiscount = tempFeeDiscount;
    }

    public Byte getTempFeeDiscountFlag() {
        return tempFeeDiscountFlag;
    }

    public void setTempFeeDiscountFlag(Byte tempFeeDiscountFlag) {
        this.tempFeeDiscountFlag = tempFeeDiscountFlag;
    }


    public Byte getCurrentInfoType() {
        return currentInfoType;
    }

    public void setCurrentInfoType(Byte currentInfoType) {
        this.currentInfoType = currentInfoType;
    }

    public Byte getSearchCarFlag() {
        return searchCarFlag;
    }

    public void setSearchCarFlag(Byte searchCarFlag) {
        this.searchCarFlag = searchCarFlag;
    }

    public Byte getMonthRechargeFlag() {
        return monthRechargeFlag;
    }

    public void setMonthRechargeFlag(Byte monthRechargeFlag) {
        this.monthRechargeFlag = monthRechargeFlag;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public ParkingLotDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public Integer getMaxExpiredDay() {
        return maxExpiredDay;
    }

    public void setMaxExpiredDay(Integer maxExpiredDay) {
        this.maxExpiredDay = maxExpiredDay;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Byte getTempfeeFlag() {
        return tempfeeFlag;
    }

    public void setTempfeeFlag(Byte tempfeeFlag) {
        this.tempfeeFlag = tempfeeFlag;
    }

    public Byte getRateFlag() {
        return rateFlag;
    }

    public void setRateFlag(Byte rateFlag) {
        this.rateFlag = rateFlag;
    }

    public Byte getExpiredRechargeFlag() {
        return expiredRechargeFlag;
    }

    public void setExpiredRechargeFlag(Byte expiredRechargeFlag) {
        this.expiredRechargeFlag = expiredRechargeFlag;
    }

    public Integer getExpiredRechargeMonthCount() {
        return expiredRechargeMonthCount;
    }

    public void setExpiredRechargeMonthCount(Integer expiredRechargeMonthCount) {
        this.expiredRechargeMonthCount = expiredRechargeMonthCount;
    }

    public Byte getExpiredRechargeType() {
        return expiredRechargeType;
    }

    public void setExpiredRechargeType(Byte expiredRechargeType) {
        this.expiredRechargeType = expiredRechargeType;
    }

    public Integer getFlowMode() {
        return flowMode;
    }

    public void setFlowMode(Integer flowMode) {
        this.flowMode = flowMode;
    }

    public Byte getLockCarFlag() {
        return lockCarFlag;
    }

    public void setLockCarFlag(Byte lockCarFlag) {
        this.lockCarFlag = lockCarFlag;
    }

    public Long getFlowId() {
        return flowId;
    }

    public void setFlowId(Long flowId) {
        this.flowId = flowId;
    }

    public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public Byte getNoticeFlag() {
		return noticeFlag;
	}

	public void setNoticeFlag(Byte noticeFlag) {
		this.noticeFlag = noticeFlag;
	}

	public String getNoticeContact() {
		return noticeContact;
	}

	public void setNoticeContact(String noticeContact) {
		this.noticeContact = noticeContact;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
