package com.everhomes.rest.rentalv2;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id：id</li>
 * </ul>
 */
public class FindRentalSiteByIdCommand {
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
