package com.everhomes.rest.user;

import java.util.ArrayList;
import java.util.List;

import com.everhomes.discover.ItemType;

public class AppIdStatusResponse {
    @ItemType(Long.class)
    private List<Long> appIds;
    
    private String name;
    
    public AppIdStatusResponse() {
        appIds = new ArrayList<Long>();
    }
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public List<Long> getAppIds() {
        return appIds;
    }
    public void setAppIds(List<Long> appIds) {
        this.appIds = appIds;
    }
    
}
