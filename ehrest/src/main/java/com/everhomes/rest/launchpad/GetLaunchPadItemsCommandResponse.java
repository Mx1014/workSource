// @formatter:off
package com.everhomes.rest.launchpad;

import java.util.List;




import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <p>launchPad信息</p>
 * <ul>
 * <li>launchPadItems: 服务市场信息，参考{@link com.everhomes.rest.launchpad.LaunchPadItemDTO}</li>
 * </ul>
 */
public class GetLaunchPadItemsCommandResponse {
    
    @ItemType(LaunchPadItemDTO.class)
    private List<LaunchPadItemDTO> launchPadItems;
    
    public GetLaunchPadItemsCommandResponse() {
    }
    public List<LaunchPadItemDTO> getLaunchPadItems() {
        return launchPadItems;
    }

    public void setLaunchPadItems(List<LaunchPadItemDTO> launchPadItems) {
        this.launchPadItems = launchPadItems;
    }
    
    @Override
    public boolean equals(Object obj){
        if (! (obj instanceof GetLaunchPadItemsCommandResponse)) {
            return false;
        }
        return EqualsBuilder.reflectionEquals(this, obj);
    }
    
    @Override
    public int hashCode(){
        return HashCodeBuilder.reflectionHashCode(this);
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
   
}
