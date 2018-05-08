// @formatter:off
package com.everhomes.rest.visitorsys;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>nextPageAnchor: (选填)下一页锚点</li>
 * <li>blackListList: (必填)黑名单列表，{@link com.everhomes.rest.visitorsys.BaseBlackListDTO}</li>
 * </ul>
 */
public class ListBlackListsResponse{
    private Long nextPageAnchor;
    @ItemType(BaseBlackListDTO.class)
    private List<BaseBlackListDTO> blackListList;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<BaseBlackListDTO> getBlackListList() {
        return blackListList;
    }

    public void setBlackListList(List<BaseBlackListDTO> blackListList) {
        this.blackListList = blackListList;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
