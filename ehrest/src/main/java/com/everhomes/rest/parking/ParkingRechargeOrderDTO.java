// @formatter:off
package com.everhomes.rest.parking;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: 订单ID</li>
 * <li>orderNo: orderNo</li>
 * <li>ownerType: 归属的类型，{@link com.everhomes.rest.parking.ParkingOwnerType}</li>
 * <li>ownerId: 归属的ID，如小区ID</li>
 * <li>parkingLotId: 停车场ID</li>
 * <li>plateNumber: 车牌号</li>
 * <li>plateOwnerName: 车主名称</li>
 * <li>plateOwnerPhone: 车主手机号</li>
 * <li>payerEnterpriseId: 付款人所在公司</li>
 * <li>payerUid: 付款人用户ID</li>
 * <li>payerName: 付款人名称</li>
 * <li>payerPhone: 付款人手机</li>
 * <li>paidType: 支付方式,10001-支付宝，10002-微信 {@link com.everhomes.rest.organization.VendorType}</li>
 * <li>paidTime: 付款时间</li>
 * <li>vendorName: 厂商名称（用于作逻辑，不用于显示），{@link com.everhomes.rest.parking.ParkingLotVendor}</li>
 * <li>cardNumber: 卡号</li>
 * <li>rateToken: 费率ID，不同厂商有不同类型的ID</li>
 * <li>rateName: 费率名称</li>
 * <li>monthCount: 充值月数，不一定每个厂商都有</li>
 * <li>price: 价格</li>
 * <li>status: 订单状态，{@link com.everhomes.rest.parking.ParkingRechargeOrderStatus}</li>
 * <li>rechargeStatus: 充值状态</li>
 * <li>rechargeTime: 充值时间</li>
 * <li>createTime: 订单创建时间</li>
 * <li>rechargeType: 订单类型， {@link com.everhomes.rest.parking.ParkingRechargeType}}</li>
 * <li>parkingLotName: 停车场名称</li>
 * <li>startPeriod: 充值开始时间</li>
 * <li>endPeriod: 充值结束时间</li>
 * <li>parkingTime: 临时车停车时间</li>
 * <li>errorDescription: 异常记录</li>
 * <li>contact: 客服联系方式，来自停车场配置参数</li>
 * <li>refundTime: 退款时间</li>
 * <li>delayTime: 剩余免费总时间</li>
 * <li>originalPrice: 原价</li>
 * <li>invoiceName: 发票名称</li>
 * <li>paySource: 缴费来源， 参考{@link com.everhomes.rest.parking.ParkingPaySourceType}</li>
 * <li>payMode : 支付类型，全部则为空，参考 {@link com.everhomes.rest.print.PrintPayType}</li>
 * </ul>
 */
public class ParkingRechargeOrderDTO {
    private Long id;
    private Long orderNo;
    private String ownerType;
    private Long ownerId;
    private Long parkingLotId;
    private String plateNumber;
    private String plateOwnerName;
    private String plateOwnerPhone;
    private Long payerEnterpriseId;
    private Long payerUid;
    private String payerName;
    private String payerPhone;
    private String paidType;
    private Timestamp paidTime;
    private String vendorName;
    private String cardNumber;
    private String rateToken;
    private String rateName;
    private BigDecimal monthCount;
    private BigDecimal price;
    private Byte status;
    private Byte rechargeStatus;
    private Timestamp rechargeTime;
    private Timestamp createTime;
    private Byte rechargeType;
    private String parkingLotName;
    private Timestamp startPeriod;
    private Timestamp endPeriod;
    private Integer parkingTime;
    private String errorDescription;
    private String contact;
    private Timestamp refundTime;
    private Integer delayTime;

    private BigDecimal originalPrice;
    private String invoiceName;
    private String paySource;
    private Byte invoiceFlag;
    private Byte payMode;
    private Byte invoiceStatus;
    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getInvoiceName() {
        return invoiceName;
    }

    public void setInvoiceName(String invoiceName) {
        this.invoiceName = invoiceName;
    }

    public Integer getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(Integer delayTime) {
        this.delayTime = delayTime;
    }

    public Long getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Long orderNo) {
        this.orderNo = orderNo;
    }

    public Timestamp getRefundTime() {
        return refundTime;
    }

    public void setRefundTime(Timestamp refundTime) {
        this.refundTime = refundTime;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Timestamp getStartPeriod() {
        return startPeriod;
    }

    public void setStartPeriod(Timestamp startPeriod) {
        this.startPeriod = startPeriod;
    }

    public Timestamp getEndPeriod() {
        return endPeriod;
    }

    public void setEndPeriod(Timestamp endPeriod) {
        this.endPeriod = endPeriod;
    }

    public Integer getParkingTime() {
        return parkingTime;
    }

    public void setParkingTime(Integer parkingTime) {
        this.parkingTime = parkingTime;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public String getParkingLotName() {
        return parkingLotName;
    }

    public void setParkingLotName(String parkingLotName) {
        this.parkingLotName = parkingLotName;
    }

    public ParkingRechargeOrderDTO() {
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

    public Long getParkingLotId() {
        return parkingLotId;
    }

    public void setParkingLotId(Long parkingLotId) {
        this.parkingLotId = parkingLotId;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getPlateOwnerName() {
        return plateOwnerName;
    }

    public void setPlateOwnerName(String plateOwnerName) {
        this.plateOwnerName = plateOwnerName;
    }

    public String getPlateOwnerPhone() {
        return plateOwnerPhone;
    }

    public void setPlateOwnerPhone(String plateOwnerPhone) {
        this.plateOwnerPhone = plateOwnerPhone;
    }

    public Long getPayerEnterpriseId() {
        return payerEnterpriseId;
    }

    public void setPayerEnterpriseId(Long payerEnterpriseId) {
        this.payerEnterpriseId = payerEnterpriseId;
    }

    public Long getPayerUid() {
        return payerUid;
    }

    public void setPayerUid(Long payerUid) {
        this.payerUid = payerUid;
    }

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public String getPayerPhone() {
        return payerPhone;
    }

    public void setPayerPhone(String payerPhone) {
        this.payerPhone = payerPhone;
    }

    public Timestamp getPaidTime() {
        return paidTime;
    }

    public void setPaidTime(Timestamp paidTime) {
        this.paidTime = paidTime;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getRateToken() {
        return rateToken;
    }

    public void setRateToken(String rateToken) {
        this.rateToken = rateToken;
    }

    public String getRateName() {
        return rateName;
    }

    public void setRateName(String rateName) {
        this.rateName = rateName;
    }

    public BigDecimal getMonthCount() {
        return monthCount;
    }

    public void setMonthCount(BigDecimal monthCount) {
        this.monthCount = monthCount;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Byte getRechargeStatus() {
        return rechargeStatus;
    }

    public void setRechargeStatus(Byte rechargeStatus) {
        this.rechargeStatus = rechargeStatus;
    }

    public Timestamp getRechargeTime() {
        return rechargeTime;
    }

    public void setRechargeTime(Timestamp rechargeTime) {
        this.rechargeTime = rechargeTime;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public String getPaidType() {
        return paidType;
    }

    public void setPaidType(String paidType) {
        this.paidType = paidType;
    }

    public Byte getRechargeType() {
        return rechargeType;
    }

    public void setRechargeType(Byte rechargeType) {
        this.rechargeType = rechargeType;
    }

    public String getPaySource() {
        return paySource;
    }

    public void setPaySource(String paySource) {
        this.paySource = paySource;
    }

	public Byte getInvoiceFlag() {
		return invoiceFlag;
	}

	public void setInvoiceFlag(Byte invoiceFlag) {
		this.invoiceFlag = invoiceFlag;
	}

	public Byte getPayMode() {
		return payMode;
	}

	public void setPayMode(Byte payMode) {
		this.payMode = payMode;
	}

	public Byte getInvoiceStatus() {
		return invoiceStatus;
	}

	public void setInvoiceStatus(Byte invoiceStatus) {
		this.invoiceStatus = invoiceStatus;
	}
	
}
