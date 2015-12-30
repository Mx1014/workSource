package com.everhomes.rest.configuration.admin;


import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>nextPageOffset: 下一页码</li>
 * <li>pageSize: 每页大小</li>
 * </ul>
 */
public class ListConfigurationsAdminCommandResponse {
    
    @ItemType(ConfigurationDTO.class)
    private List<ConfigurationDTO> requests;
    
    private Integer nextPageOffset;
    
    public List<ConfigurationDTO> getRequests() {
        return requests;
    }

    public void setRequests(List<ConfigurationDTO> requests) {
        this.requests = requests;
    }

    public Integer getNextPageOffset() {
        return nextPageOffset;
    }

    public void setNextPageOffset(Integer nextPageOffset) {
        this.nextPageOffset = nextPageOffset;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
