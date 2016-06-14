package com.everhomes.rest.aclink;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class GetDoorAccessByHardwareIdCommand {
    @ItemType(String.class)
    private List<String> hardwareIds;
    
    Long organizationId;
    
    public List<String> getHardwareIds() {
        return hardwareIds;
    }



    public void setHardwareIds(List<String> hardwareIds) {
        this.hardwareIds = hardwareIds;
    }



    public Long getOrganizationId() {
        return organizationId;
    }



    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }



    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
