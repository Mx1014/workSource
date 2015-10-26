package com.everhomes.enterprise;

import javax.validation.constraints.NotNull;

public class EnterpriseApproveCommand {
    @NotNull
    Long enterpriseId;
    
    @NotNull
    Long communityId;
}
