package com.everhomes.rest.acl.admin;


import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>nextPageOffset: 下一页码</li>
 * <li>requests: 结果列表，参考{@link com.everhomes.rest.acl.admin.AclRoleAssignmentsDTO}</li>
 * </ul>
 */
public class ListUserRolesAdminCommandResponse {
    
    @ItemType(AclRoleAssignmentsDTO.class)
    private List<AclRoleAssignmentsDTO> requests;
    
    private Integer nextPageOffset;
    
    public List<AclRoleAssignmentsDTO> getRequests() {
        return requests;
    }

    public void setRequests(List<AclRoleAssignmentsDTO> requests) {
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
