// @formatter:off
package com.everhomes.rest.address;

import java.util.List;




import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>requests: 地址列表，参考{@link com.everhomes.rest.address.AddressDTO}</li>
 * <li>nextPageOffset: 下一页页码</li>
 * <li>pageSize: 每页大小</li>
 * </ul>
 */
public class ListAddressByKeywordCommandResponse {
    @ItemType(AddressDTO.class)
    private List<AddressDTO> requests;
    
    private Integer nextPageOffset;

    public ListAddressByKeywordCommandResponse() {
    }

    public List<AddressDTO> getRequests() {
        return requests;
    }

    public void setRequests(List<AddressDTO> requests) {
        this.requests = requests;
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
