package com.everhomes.aclink;

import com.everhomes.util.StringHelper;

public class AclinkDeleteByIdCommand {
    private Long objId;

    public Long getObjId() {
        return objId;
    }

    public void setObjId(Long objId) {
        this.objId = objId;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
