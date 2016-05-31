package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

public class GetVisitorCommand {
    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
