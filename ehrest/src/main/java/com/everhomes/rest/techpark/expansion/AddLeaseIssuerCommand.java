package com.everhomes.rest.techpark.expansion;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.address.AddressDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>addressIds：门牌地址列表</li>
 * <li>communityId：园区id</li>
 * <li>issuerContact：业主手机号</li>
 * <li>issuerName：业主姓名</li>
 * <li>enterpriseIds：公司id列表</li>
 * </ul>
 */
public class AddLeaseIssuerCommand {

    @ItemType(Long.class)
    private List<Long> addressIds;

    private Long communityId;
    private String issuerContact;
    private String issuerName;
    @ItemType(Long.class)
    private List<Long> enterpriseIds;

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public String getIssuerContact() {
        return issuerContact;
    }

    public void setIssuerContact(String issuerContact) {
        this.issuerContact = issuerContact;
    }

    public String getIssuerName() {
        return issuerName;
    }

    public void setIssuerName(String issuerName) {
        this.issuerName = issuerName;
    }

    public List<Long> getAddressIds() {
        return addressIds;
    }

    public void setAddressIds(List<Long> addressIds) {
        this.addressIds = addressIds;
    }

    public List<Long> getEnterpriseIds() {
        return enterpriseIds;
    }

    public void setEnterpriseIds(List<Long> enterpriseIds) {
        this.enterpriseIds = enterpriseIds;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
