package com.everhomes.rest.relocation;

import com.everhomes.util.StringHelper;

/**
 * @author sw on 2017/11/20.
 */
public class GetRelocationRequestDetailCommand {
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
