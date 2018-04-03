package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;
import java.util.List;
import java.sql.Timestamp;

import com.everhomes.discover.ItemType;

public class FlowStatDTO {
    private Integer     namespaceId;
    private Long     flowMainId;
    private Integer     nodeLevel;
    private Integer     flowVersion;
    private Integer     leaveCount;
    private Integer     enterCount;
    private Long     id;
    private Integer     runningCount;


    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}

