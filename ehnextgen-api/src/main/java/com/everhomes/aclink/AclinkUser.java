package com.everhomes.aclink;

import com.everhomes.user.User;

public class AclinkUser extends User {

    /**
     * 
     */
    private static final long serialVersionUID = -6496832012592025224L;
    
    private Long companyId;
    private String buildingName;
    private Long buildingId;

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public Long getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Long buildingId) {
        this.buildingId = buildingId;
    }
    
    
}
