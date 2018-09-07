package com.everhomes.rest.investment;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>namespaceId: namespaceId</li>
 *     <li>communityId: communityId</li>
 *     <li>nowAddress: 当前地址</li>
 *     <li>nowRental: 当前租金</li>
 *     <li>nowRentalUnit: 当前租金单位</li>
 *     <li>nowArea: 当前面积</li>
 *     <li>nowContractEndDate: 当前合同结束日</li>
 *     <li>status: status</li>
 *     <li>customerId: customerId</li>
 *     <li>createTime: createTime</li>
 *     <li>createBy: createBy</li>
 *     <li>operatorTime: operatorTime</li>
 *     <li>operatorBy: operatorBy</li>
 * </ul>
 */
public class CustomerCurrentRentDTO {

    private Long id;
    private Integer namespaceId;
    private Long communityId;
    private Long customerId;
    private String address;
    private BigDecimal rentPrice;
    private Byte rentPriceUnit;
    private BigDecimal rentArea;
    private Timestamp contractIntentionDate;
    private String version;
    private Byte status;
    private Timestamp createTime;
    private Long creatorUid;
    private Timestamp operatorTime;
    private Long operatorUid;

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

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getRentPrice() {
        return rentPrice;
    }

    public void setRentPrice(BigDecimal rentPrice) {
        this.rentPrice = rentPrice;
    }

    public Byte getRentPriceUnit() {
        return rentPriceUnit;
    }

    public void setRentPriceUnit(Byte rentPriceUnit) {
        this.rentPriceUnit = rentPriceUnit;
    }

    public BigDecimal getRentArea() {
        return rentArea;
    }

    public void setRentArea(BigDecimal rentArea) {
        this.rentArea = rentArea;
    }

    public Timestamp getContractIntentionDate() {
        return contractIntentionDate;
    }

    public void setContractIntentionDate(Timestamp contractIntentionDate) {
        this.contractIntentionDate = contractIntentionDate;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Long getCreatorUid() {
        return creatorUid;
    }

    public void setCreatorUid(Long creatorUid) {
        this.creatorUid = creatorUid;
    }

    public Timestamp getOperatorTime() {
        return operatorTime;
    }

    public void setOperatorTime(Timestamp operatorTime) {
        this.operatorTime = operatorTime;
    }

    public Long getOperatorUid() {
        return operatorUid;
    }

    public void setOperatorUid(Long operatorUid) {
        this.operatorUid = operatorUid;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
