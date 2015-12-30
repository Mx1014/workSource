// @formatter:off
package com.everhomes.rest.family;

import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>neighborCount: 小区邻入住邻居总数</li>
 * <li>neighborUserList: 小区邻居列表，参考{@link com.everhomes.rest.family.NeighborUserDTO}</li>
 * </ul>
 */
public class ListNeighborUsersCommandResponse{

    private Integer neighborCount;
    @ItemType(NeighborUserDTO.class)
    private List<NeighborUserDTO> neighborUserList;

    public ListNeighborUsersCommandResponse() {
    }
    
    public Integer getNeighborCount() {
        return neighborCount;
    }

    public void setNeighborCount(Integer neighborCount) {
        this.neighborCount = neighborCount;
    }

    public List<NeighborUserDTO> getNeighborUserList() {
        return neighborUserList;
    }

    public void setNeighborUserList(List<NeighborUserDTO> neighborUserList) {
        this.neighborUserList = neighborUserList;
    }
    

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }


    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
