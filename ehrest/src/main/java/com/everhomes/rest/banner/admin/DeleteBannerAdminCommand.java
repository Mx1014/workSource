package com.everhomes.rest.banner.admin;


import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: banner id</li>
 * </ul>
 */
public class DeleteBannerAdminCommand {
    
    @NotNull
    private Long id;
    
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
