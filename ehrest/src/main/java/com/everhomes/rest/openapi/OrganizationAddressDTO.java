package com.everhomes.rest.openapi;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.address.AddressDTO;
import com.everhomes.util.StringHelper;

import java.util.List;


public class OrganizationAddressDTO {
    private Long organizastionId;

    private String organizationName;

    @ItemType(AddressDTO.class)
    private List<AddressDTO> adddresses;

    public Long getOrganizastionId() {
        return organizastionId;
    }

    public void setOrganizastionId(Long organizastionId) {
        this.organizastionId = organizastionId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public List<AddressDTO> getAdddresses() {
        return adddresses;
    }

    public void setAdddresses(List<AddressDTO> adddresses) {
        this.adddresses = adddresses;
    }

    @Override
    public String toString(){
        return StringHelper.toJsonString(this);
        
    }
}
