package com.everhomes.rest.reserve;

import com.everhomes.util.StringHelper;

/**
 * @author sw on 2017/12/18.
 */
public class GetReserveOrderByIdCommand {
    private Long id;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
