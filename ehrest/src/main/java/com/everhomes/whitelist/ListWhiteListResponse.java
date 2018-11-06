// @formatter:off
package com.everhomes.whitelist;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>nextAnchor: 下一页锚点</li>
 *     <li>whiteListDTOList: 白名单列表{@link WhiteListDTO}</li>
 * </ul>
 */
public class ListWhiteListResponse {

    private Long nextPageAnchor;

    @ItemType(WhiteListDTO.class)
    private List<WhiteListDTO> dtos;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<WhiteListDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<WhiteListDTO> dtos) {
        this.dtos = dtos;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
