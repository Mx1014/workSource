package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;
import java.util.List;
import java.sql.Timestamp;

import com.everhomes.discover.ItemType;

public class FlowVariableDTO {
    private String     name;
    private String     ownerType;
    private Integer     namespaceId;
    private Long     ownerId;
    private String     moduleType;
    private Long     id;
    private Integer     moduleId;


    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}

