package com.everhomes.rest.investment;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;


/**
 * <ul>
 *     <li>id: id</li>
 *     <li>namespaceId: namespaceId</li>
 *     <li>communityId: communityId</li>
 *     <li>customerId: customerId</li>
 *     <li>intentionLocation: intentionLocation</li>
 *     <li>minArea: minArea</li>
 *     <li>maxArea: maxArea</li>
 *     <li>minRentPrice: minRentPrice</li>
 *     <li>maxRentPrice: maxRentPrice</li>
 *     <li>rentPriceUnit: rentPriceUnit</li>
 *     <li>rentType: rentType</li>
 *     <li>version: version</li>
 *     <li>status: status</li>
 *     <li>createTime: createTime</li>
 *     <li>creatorUid: creatorUid</li>
 *     <li>operatorTime: operatorTime</li>
 *     <li>operatorUid: operatorUid</li>
 * </ul>
 */
public class CustomerRequirementDTO {


    private Long id;
    private Integer namespaceId;
    private Long communityId;
    private Long customerId;
    private String intentionLocation;
    private BigDecimal minArea;
    private BigDecimal maxArea;
    private BigDecimal minRentPrice;
    private BigDecimal maxRentPrice;
    private Byte rentPriceUnit;
    private Byte rentType;
    private Long version;
    private List<CustomerRequirementAddressDTO> addresses;
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

    public String getIntentionLocation() {
        return intentionLocation;
    }

    public void setIntentionLocation(String intentionLocation) {
        this.intentionLocation = intentionLocation;
    }

    public BigDecimal getMinArea() {
        return minArea;
    }

    public void setMinArea(BigDecimal minArea) {
        this.minArea = minArea;
    }

    public BigDecimal getMaxArea() {
        return maxArea;
    }

    public void setMaxArea(BigDecimal maxArea) {
        this.maxArea = maxArea;
    }

    public BigDecimal getMinRentPrice() {
        return minRentPrice;
    }

    public void setMinRentPrice(BigDecimal minRentPrice) {
        this.minRentPrice = minRentPrice;
    }

    public BigDecimal getMaxRentPrice() {
        return maxRentPrice;
    }

    public void setMaxRentPrice(BigDecimal maxRentPrice) {
        this.maxRentPrice = maxRentPrice;
    }

    public Byte getRentPriceUnit() {
        return rentPriceUnit;
    }

    public void setRentPriceUnit(Byte rentPriceUnit) {
        this.rentPriceUnit = rentPriceUnit;
    }

    public Byte getRentType() {
        return rentType;
    }

    public void setRentType(Byte rentType) {
        this.rentType = rentType;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
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

    public List<CustomerRequirementAddressDTO> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<CustomerRequirementAddressDTO> addresses) {
        this.addresses = addresses;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
