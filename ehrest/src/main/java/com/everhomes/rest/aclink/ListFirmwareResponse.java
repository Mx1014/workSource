// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>获取门禁固件包列表
 * <li> nextPageAnchor: 下一页锚点 </li>
 * <li> dtos: 门禁固件包列表，参考{@link FirmwarePackageDTO}</li>
 * </ul>
 */
public class ListFirmwareResponse {
    private Long nextPageAnchor;

    @ItemType(FirmwareNewDTO.class)
    private List<FirmwareNewDTO> dtos;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<FirmwareNewDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<FirmwareNewDTO> dtos) {
        this.dtos = dtos;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
