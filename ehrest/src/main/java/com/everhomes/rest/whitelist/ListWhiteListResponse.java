// @formatter:off
package com.everhomes.rest.whitelist;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>nextAnchor: 下一页锚点</li>
 *     <li>whiteListDTOList: 白名单列表</li>
 * </ul>
 */
public class ListWhiteListResponse {

    private Long nextAnchor;

    @ItemType(WhiteListDTO.class)
    private List<WhiteListDTO> whiteListDTOList;

    public Long getNextAnchor() {
        return nextAnchor;
    }

    public void setNextAnchor(Long nextAnchor) {
        this.nextAnchor = nextAnchor;
    }

    public List<WhiteListDTO> getWhiteListDTOList() {
        return whiteListDTOList;
    }

    public void setWhiteListDTOList(List<WhiteListDTO> whiteListDTOList) {
        this.whiteListDTOList = whiteListDTOList;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
