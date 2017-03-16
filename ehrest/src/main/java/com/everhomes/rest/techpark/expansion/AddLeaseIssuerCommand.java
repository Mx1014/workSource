package com.everhomes.rest.techpark.expansion;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.address.AddressDTO;

import java.util.List;

/**
 * Created by Administrator on 2017/3/15.
 */
public class AddLeaseIssuerCommand {

    @ItemType(Long.class)
    private List<Long> addressIds;

    private Long communityId;
    private String issuerContact;
    private String issuerName;
    private Long enterpriseId;

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

    public Long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }
}
