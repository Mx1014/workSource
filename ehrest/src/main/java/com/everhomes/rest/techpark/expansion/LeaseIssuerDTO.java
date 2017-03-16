package com.everhomes.rest.techpark.expansion;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.address.AddressDTO;

import java.util.List;

/**
 * Created by Administrator on 2017/3/15.
 */
public class LeaseIssuerDTO {

    private Long communityId;
    private String issuerContact;
    private String issuerName;
    @ItemType(AddressDTO.class)
    private List<AddressDTO> addresses;
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

    public List<AddressDTO> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<AddressDTO> addresses) {
        this.addresses = addresses;
    }

    public Long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }
}
