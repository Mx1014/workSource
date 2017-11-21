package com.everhomes.rest.relocation;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * @author sw on 2017/11/21.
 */
public class RelocationInfoDTO {

    private String userName;
    private String contactPhone;

    @ItemType(OrganizationBriefInfoDTO.class)
    private List<OrganizationBriefInfoDTO> organizations;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public List<OrganizationBriefInfoDTO> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(List<OrganizationBriefInfoDTO> organizations) {
        this.organizations = organizations;
    }
}
