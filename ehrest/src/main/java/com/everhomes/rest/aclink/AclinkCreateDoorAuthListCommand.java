package com.everhomes.rest.aclink;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class AclinkCreateDoorAuthListCommand {
    @ItemType(CreateDoorAuthCommand.class)
    private List<CreateDoorAuthCommand> auths;

    public List<CreateDoorAuthCommand> getAuths() {
        return auths;
    }

    public void setAuths(List<CreateDoorAuthCommand> auths) {
        this.auths = auths;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
