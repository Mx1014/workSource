// @formatter:off
package com.everhomes.rest.aclink;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>users:用户{@link com.everhomes.rest.aclink.AclinkUserDTO}</li>
 * <li>nextPageAnchor:下一页锚点</li>
 * <li>若用户无权限，抛出异常"errorCode":100055,"errorDescription":"校验应用权限失败"</li>
 * </ul>
 */
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
