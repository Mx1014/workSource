// @formatter:off
package com.everhomes.rest.user;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * <ul>
 *     <li>userDtos: 用户信息列表</li>
 * </ul>
 */
public class GetUserListResponse {

    @ItemType(UserDTO.class)
    private List<UserDTO> userDtos;

    public List<UserDTO> getUserDtos() {
        return userDtos;
    }

    public void setUserDtos(List<UserDTO> userDtos) {
        this.userDtos = userDtos;
    }
}
