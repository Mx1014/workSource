package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

/**
 * @author sw on 2017/8/14.
 */
public class GetOrganizationDetailByIdCommand {
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
