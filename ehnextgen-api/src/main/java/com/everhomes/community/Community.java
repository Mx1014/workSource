// @formatter:off
package com.everhomes.community;

import com.everhomes.server.schema.tables.pojos.EhCommunities;
import com.everhomes.util.StringHelper;

public class Community extends EhCommunities {
    private static final long serialVersionUID = -5487824409222624113L;

    public Community() {
    }

    public Long getRequestStatus(){
        return CommunityCustomField.REQUEST_STATUS.getIntegralValue(this);
    }
    
    public void setRequestStatus(Long requestStatus) {
        CommunityCustomField.REQUEST_STATUS.setIntegralValue(this, requestStatus);
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
