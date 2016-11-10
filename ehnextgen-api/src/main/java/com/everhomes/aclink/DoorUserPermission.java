package com.everhomes.aclink;
import com.everhomes.server.schema.tables.pojos.EhDoorUserPermission;
import com.everhomes.util.StringHelper;

public class DoorUserPermission extends EhDoorUserPermission {
    /**
     * 
     */
    private static final long serialVersionUID = -8283089275118554924L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
