package com.everhomes.rest.investment;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>namespaceId: namespaceId</li>
 *     <li>communityId: communityId</li>
 *     <li>expectedLocation: 期望地段</li>
 *     <li>demandAreaMin: 期望最小面积</li>
 *     <li>demandAreaMax: 期望最大面积</li>
 *     <li>demandPriceMin: 期望最小价格</li>
 *     <li>demandPriceMax: 期望最大价格</li>
 *     <li>demandPriceUnit: 期望价格单位</li>
 *     <li>buyOrLease: 租赁还是购买</li>
 *     <li>expectAddress: 期望房源</li>
 *     <li>demandVersion: demandVersion</li>
 *     <li>status: 信息状态</li>
 *     <li>customerId: customerId</li>
 *     <li>createTime: createTime</li>
 *     <li>createBy: createBy</li>
 *     <li>operatorTime: operatorTime</li>
 *     <li>operatorBy: operatorBy</li>
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
