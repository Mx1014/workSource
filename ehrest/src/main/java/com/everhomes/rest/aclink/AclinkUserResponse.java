package com.everhomes.rest.aclink;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class AclinkUserResponse {
    private Long nextPageAnchor;
    
    @ItemType(AclinkUserDTO.class)
    private List<AclinkUserDTO> users;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<AclinkUserDTO> getUsers() {
        return users;
    }

    public void setUsers(List<AclinkUserDTO> users) {
        this.users = users;
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
