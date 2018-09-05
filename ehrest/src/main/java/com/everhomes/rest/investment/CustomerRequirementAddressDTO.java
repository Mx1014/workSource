package com.everhomes.rest.investment;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

/**
 * <ul>
 *     <li>namespaceId: namespaceId</li>
 *     <li>communityId: communityId</li>
 *     <li>requirementId: requirementId</li>
 *     <li>customerId: customerId</li>
 *     <li>addressId: addressId</li>
 *     <li>status: status</li>
 *     <li>createTime: createTime</li>
 *     <li>creatorUid: creatorUid</li>
 *     <li>operatorTime: operatorTime</li>
 *     <li>operatorUid: operatorUid</li>
 * </ul>
 */
public class CustomerRequirementAddressDTO {
    private Integer namespaceId;
    private Long communityId;
    private Long requirementId;
    private Long customerId;
    private String addressId;
    private Byte status;
    private Timestamp createTime;
    private Long creatorUid;
    private Timestamp operatorTime;
    private Long operatorUid;

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

    public Long getRequirementId() {
        return requirementId;
    }

    public void setRequirementId(Long requirementId) {
        this.requirementId = requirementId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
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
