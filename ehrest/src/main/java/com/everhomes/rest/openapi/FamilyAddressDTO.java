package com.everhomes.rest.openapi;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.address.AddressDTO;
import com.everhomes.util.StringHelper;

import java.util.List;


public class FamilyAddressDTO {
    private Long familyId;

    private String familyName;

    private AddressDTO adddress;

    public Long getFamilyId() {
        return familyId;
    }

    public void setFamilyId(Long familyId) {
        this.familyId = familyId;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public AddressDTO getAdddress() {
        return adddress;
    }

    public void setAdddress(AddressDTO adddress) {
        this.adddress = adddress;
    }

    @Override
    public String toString(){
        return StringHelper.toJsonString(this);
        
    }
}
