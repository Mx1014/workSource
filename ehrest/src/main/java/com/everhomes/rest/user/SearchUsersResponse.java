// @formatter:off
package com.everhomes.rest.user;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 *<ul>
 * <li>nextPageAnchor:下一页的锚点</li>
 * <li>dtos:用户详情, 参考{@link com.everhomes.rest.user.UserDTO}</li>
 *</ul>
 */
public class SearchUsersResponse {

    private Long nextPageAnchor;

    @ItemType(UserDTO.class)
    private List<UserDTO> dtos;

    public SearchUsersResponse() {
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<UserDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<UserDTO> dtos) {
        this.dtos = dtos;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
