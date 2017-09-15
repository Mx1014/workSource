package com.everhomes.rest.techpark.expansion;

import com.everhomes.util.StringHelper;

/**
 * @author sw on 2017/8/3.
 */
public class DeleteLeaseBuildingCommand {
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
