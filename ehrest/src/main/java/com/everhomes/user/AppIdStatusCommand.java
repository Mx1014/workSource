package com.everhomes.user;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.Name;

@Name("AppIdStatusCommand")
public class AppIdStatusCommand {

    private String name;
    
    @ItemType(Long.class)
    private List<Long> appIds;

    public List<Long> getAppIds() {
        return appIds;
    }

    public void setAppIds(List<Long> appIds) {
        this.appIds = appIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
