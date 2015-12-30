// @formatter:off
package com.everhomes.rest.community;


import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <p>
 * <ul>
 * <li>id: 小区Id</li>
 * </ul>
 */
public class GetNearbyCommunitiesByIdCommand {
    @NotNull
    private Long id;
    
    public GetNearbyCommunitiesByIdCommand() {
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
