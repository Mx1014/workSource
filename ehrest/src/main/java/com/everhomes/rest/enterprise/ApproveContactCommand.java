package com.everhomes.rest.enterprise;

import com.everhomes.util.StringHelper;
/**
* <ul>  
* <li>enterpriseId：企业Id</li>
* <li>userId: 用户id </li> 
* </ul>
*/
public class ApproveContactCommand {
    private Long enterpriseId;
    
    private Long userId;

    public Long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
