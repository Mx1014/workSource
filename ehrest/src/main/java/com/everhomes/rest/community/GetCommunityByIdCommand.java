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
public class GetCommunityByIdCommand {
    @NotNull
    private Long id;
    
    public GetCommunityByIdCommand() {
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
