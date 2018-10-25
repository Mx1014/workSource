// @formatter:off
package com.everhomes.rest.parking;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespaceId: 域空间id</li>
 * <li>ownerType: 归属的类型，{@link com.everhomes.rest.parking.ParkingOwnerType}</li>
 * <li>ownerId: 归属的ID，如小区ID</li>
 * <li>parkingLotId: 停车场ID</li>
 * <li>parkingLotName: 停车场名称</li>
 * <li>businessType: 业务标识，{@link ParkingBusinessType}</li>
 * <li>payeeId: 收款方账号id</li>
 * <li>payeeUserType: 收款方账号类型 帐号类型，{@link com.everhomes.rest.order.OwnerType}</li>
 * <li>payeeUserName: 收款方账号名称</li>
 * <li>payeeUserAliasName: 收款方账号别名</li>
 * <li>payeeRegisterStatus: 企业账户：0未审核 1审核通过 个人帐户：0 未绑定手机 1 绑定手机</li>
 * <li>payeeRemark: 用户自定义账户名称，用以区分同一个企业注册了多个用户</li>
 * <li>payeeAccountCode: payeeAccountCode</li>
 * <li>merchantId:商户ID</li>
 * </ul>
 */
public class BusinessPayeeAccountDTO {
    private Long id;
    private Integer namespaceId;
    private String ownerType;
    private Long ownerId;
    private Long parkingLotId;
    private String parkingLotName;
    private String businessType;
    private Long payeeId;
    private String payeeUserType;
    private String payeeUserName;
    private String payeeUserAliasName;
    private Integer payeeRegisterStatus;
    private String payeeRemark;
    private String payeeAccountCode;
    private Long merchantId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
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

    public String getParkingLotName() {
        return parkingLotName;
    }

    public void setParkingLotName(String parkingLotName) {
        this.parkingLotName = parkingLotName;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public Long getPayeeId() {
        return payeeId;
    }

    public void setPayeeId(Long payeeId) {
        this.payeeId = payeeId;
    }

    public String getPayeeUserType() {
        return payeeUserType;
    }

    public void setPayeeUserType(String payeeUserType) {
        this.payeeUserType = payeeUserType;
    }

    public String getPayeeUserName() {
        return payeeUserName;
    }

    public void setPayeeUserName(String payeeUserName) {
        this.payeeUserName = payeeUserName;
    }

    public String getPayeeUserAliasName() {
        return payeeUserAliasName;
    }

    public void setPayeeUserAliasName(String payeeUserAliasName) {
        this.payeeUserAliasName = payeeUserAliasName;
    }

    public Integer getPayeeRegisterStatus() {
        return payeeRegisterStatus;
    }

    public void setPayeeRegisterStatus(Integer payeeRegisterStatus) {
        this.payeeRegisterStatus = payeeRegisterStatus;
    }

    public String getPayeeRemark() {
        return payeeRemark;
    }

    public void setPayeeRemark(String payeeRemark) {
        this.payeeRemark = payeeRemark;
    }

    public String getPayeeAccountCode() {
        return payeeAccountCode;
    }

    public void setPayeeAccountCode(String payeeAccountCode) {
        this.payeeAccountCode = payeeAccountCode;
    }

    
    public Long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
