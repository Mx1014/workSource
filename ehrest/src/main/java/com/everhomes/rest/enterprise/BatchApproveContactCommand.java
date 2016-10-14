package com.everhomes.rest.enterprise;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;
/**
* <ul>  
* <li>enterpriseId：企业Id</li>
* <li>userIds: 用户id list</li> 
* </ul>
*/
public class BatchApproveContactCommand {
    private Long enterpriseId;
    
    @ItemType(Long.class)
    private List<Long> userIds;

    public Long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }
 

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public List<Long> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<Long> userIds) {
		this.userIds = userIds;
	}

}
