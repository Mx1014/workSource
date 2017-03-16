package com.everhomes.rest.techpark.expansion;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.address.AddressDTO;

import java.util.List;

/**
 * Created by Administrator on 2017/3/15.
 */
public class AddLeaseIssuerCommand {
    private Long communityId;
    private String issuerContact;
    private String issuerName;
    @ItemType(Long.class)
    private List<Long> addresIds;
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

    public List<Long> getAddresIds() {
        return addresIds;
    }

    public void setAddresIds(List<Long> addresIds) {
        this.addresIds = addresIds;
    }

    public Long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }
}
