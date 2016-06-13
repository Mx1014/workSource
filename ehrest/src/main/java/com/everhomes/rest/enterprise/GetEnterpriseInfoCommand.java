package com.everhomes.rest.enterprise;

import javax.validation.constraints.NotNull;

public class GetEnterpriseInfoCommand {
    @NotNull
    Long enterpriseId;

    public Long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }
    
    
}
