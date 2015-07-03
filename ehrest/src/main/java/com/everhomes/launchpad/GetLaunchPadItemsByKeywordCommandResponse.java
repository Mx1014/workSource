// @formatter:off
package com.everhomes.launchpad;



import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>launchPadItems: item列表，参考{@link com.everhomes.launchpad.LaunchPadItemDTO}</li>
 * </ul>
 */
public class GetLaunchPadItemsByKeywordCommandResponse {
    
    @ItemType(LaunchPadItemDTO.class)
    private List<LaunchPadItemDTO> launchPadItems;
    private Integer nextPageOffset;
    public GetLaunchPadItemsByKeywordCommandResponse() {
    }
    
    public List<LaunchPadItemDTO> getLaunchPadItems() {
        return launchPadItems;
    }

    public void setLaunchPadItems(List<LaunchPadItemDTO> launchPadItems) {
        this.launchPadItems = launchPadItems;
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
