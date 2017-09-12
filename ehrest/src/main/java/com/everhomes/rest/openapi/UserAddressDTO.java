package com.everhomes.rest.openapi;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.family.FamilyDTO;
import com.everhomes.util.StringHelper;

import java.util.List;


public class UserAddressDTO {

    @ItemType(UserServiceAddressDTO.class)
    private List<UserServiceAddressDTO> serviceAddresses;

    @ItemType(FamilyDTO.class)
    private List<FamilyAddressDTO> familyAddresses;

    @ItemType(OrganizationAddressDTO.class)
    private List<OrganizationAddressDTO> organizationAddresses;

    public List<UserServiceAddressDTO> getServiceAddresses() {
        return serviceAddresses;
    }

    public void setServiceAddresses(List<UserServiceAddressDTO> serviceAddresses) {
        this.serviceAddresses = serviceAddresses;
    }

    public List<FamilyAddressDTO> getFamilyAddresses() {
        return familyAddresses;
    }

    public void setFamilyAddresses(List<FamilyAddressDTO> familyAddresses) {
        this.familyAddresses = familyAddresses;
    }

    public List<OrganizationAddressDTO> getOrganizationAddresses() {
        return organizationAddresses;
    }

    public void setOrganizationAddresses(List<OrganizationAddressDTO> organizationAddresses) {
        this.organizationAddresses = organizationAddresses;
    }

    @Override
    public String toString(){
        return StringHelper.toJsonString(this);
        
    }
}
