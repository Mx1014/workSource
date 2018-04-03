package com.everhomes.rest.blacklist;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>nextPageAnchor: 下一个瞄点</li>
 * <li>dtos: 黑名单列表， 参考{@link com.everhomes.rest.blacklist.UserBlacklistDTO}</li>
 * </ul>
 */
public class ListUserBlacklistsResponse {

    private Long nextPageAnchor;

    @ItemType(UserBlacklistDTO.class)
    private List<UserBlacklistDTO> dtos;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<UserBlacklistDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<UserBlacklistDTO> dtos) {
        this.dtos = dtos;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
