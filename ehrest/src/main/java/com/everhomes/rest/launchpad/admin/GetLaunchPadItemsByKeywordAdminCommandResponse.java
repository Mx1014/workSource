// @formatter:off
package com.everhomes.rest.launchpad.admin;



import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.launchpad.LaunchPadItemDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>launchPadItems: item列表，参考{@link com.everhomes.rest.launchpad.LaunchPadItemDTO}</li>
 * </ul>
 */
public class GetLaunchPadItemsByKeywordAdminCommandResponse {
    
    @ItemType(LaunchPadItemAdminDTO.class)
    private List<LaunchPadItemAdminDTO> launchPadItems;
    private Integer nextPageOffset;
    public GetLaunchPadItemsByKeywordAdminCommandResponse() {
    }
    
    public List<LaunchPadItemAdminDTO> getLaunchPadItems() {
        return launchPadItems;
    }

    public void setLaunchPadItems(List<LaunchPadItemAdminDTO> launchPadItems) {
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
