// @formatter:off
package com.everhomes.rest.parking;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerType: 归属的类型，{@link com.everhomes.rest.parking.ParkingOwnerType}</li>
 * <li>ownerId: 归属的ID，如小区ID</li>
 * <li>parkingLotId: 停车场ID</li>
 * <li>plateNumber: 车牌号</li>
 * <li>plateOwnerName: 车主名称</li>
 * <li>plateOwnerPhone: 车主手机号</li>
 * <li>payerEnterpriseId: 付款人所在公司ID</li>
 * <li>cardNumber: 卡号</li>
 * <li>rateToken: 费率ID，不同厂商有不同类型的ID</li>
 * <li>rateName: 费率名称</li>
 * <li>monthCount: 充值月数，不一定每个厂商都有</li>
 * <li>price: 价格</li>
 * </ul>
 */
public class CreateParkingRechargeOrderCommand {
	@NotNull
    private String ownerType;
	@NotNull
    private Long ownerId;
	@NotNull
    private Long parkingLotId;
	@NotNull
    private String plateNumber;
    private String plateOwnerName;
    private String plateOwnerPhone;
	@NotNull
    private Long payerEnterpriseId;
//    private String vendorName;
    
    private String cardNumber;
    private String rateToken;
    private String rateName;
    private Integer monthCount;
    private BigDecimal price;
//	private Long expiredTime;
    
//	public Long getExpiredTime() {
//		return expiredTime;
//	}
//
//	public void setExpiredTime(Long expiredTime) {
//		this.expiredTime = expiredTime;
//	}

	public CreateParkingRechargeOrderCommand() {
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

//    public String getVendorName() {
//        return vendorName;
//    }
//
//    public void setVendorName(String vendorName) {
//        this.vendorName = vendorName;
//    }

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

    public Integer getMonthCount() {
        return monthCount;
    }

    public void setMonthCount(Integer monthCount) {
        this.monthCount = monthCount;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
