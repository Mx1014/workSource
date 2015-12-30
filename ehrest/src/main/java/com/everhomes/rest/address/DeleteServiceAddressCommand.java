// @formatter:off
package com.everhomes.rest.address;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: 地址id</li>
 * </ul>
 */
public class DeleteServiceAddressCommand {
    
    private Long id;


    public DeleteServiceAddressCommand() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
