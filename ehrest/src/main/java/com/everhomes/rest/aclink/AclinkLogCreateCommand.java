// @formatter:off
package com.everhomes.rest.aclink;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class AclinkLogCreateCommand {
    @ItemType(AclinkLogItem.class)
    List<AclinkLogItem> items;
    
    public List<AclinkLogItem> getItems() {
        return items;
    }
    
    public void setItems(List<AclinkLogItem> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
    
}
