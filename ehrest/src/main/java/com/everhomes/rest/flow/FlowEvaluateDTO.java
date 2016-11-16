package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;
import java.util.List;
import java.sql.Timestamp;

import com.everhomes.discover.ItemType;

public class FlowEvaluateDTO {
    private Byte     star;
    private Long     userId;
    private Long     flowMainId;
    private Integer     flowVersion;
    private String     ownerType;
    private Integer     namespaceId;
    private Long     flowCaseId;
    private Long     ownerId;
    private String     moduleType;
    private Long     id;
    private Integer     moduleId;


    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}

