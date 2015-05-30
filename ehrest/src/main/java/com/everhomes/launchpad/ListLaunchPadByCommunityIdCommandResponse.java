// @formatter:off
package com.everhomes.launchpad;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <p>launchPad信息
 * <ul>
 * <li>launchPadItems: 服务市场信息，参考{@link com.everhomes.launchpad.LaunchPadItemDTO}</li>
 * </ul>
 */
public class ListLaunchPadByCommunityIdCommandResponse {
    
    @ItemType(LaunchPadItemDTO.class)
    private List<LaunchPadItemDTO> launchPadItems;
    
    public ListLaunchPadByCommunityIdCommandResponse() {
    }
    public List<LaunchPadItemDTO> getLaunchPadItems() {
        return launchPadItems;
    }

    public void setLaunchPadItems(List<LaunchPadItemDTO> launchPadItems) {
        this.launchPadItems = launchPadItems;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
