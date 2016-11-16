package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;
import java.util.List;
import java.sql.Timestamp;

import com.everhomes.discover.ItemType;

public class FlowNodeDTO {
    private Byte     status;
    private String     description;
    private Integer     autoStepHour;
    private Long     flowMainId;
    private Timestamp     createTime;
    private Integer     namespaceId;
    private Integer     flowVersion;
    private Integer     nodeLevel;
    private Long     id;
    private String     nodeName;


    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}

