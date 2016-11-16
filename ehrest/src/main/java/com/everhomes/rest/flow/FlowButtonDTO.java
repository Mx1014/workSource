package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;
import java.util.List;
import java.sql.Timestamp;

import com.everhomes.discover.ItemType;

public class FlowButtonDTO {
    private Byte     status;
    private Integer     gotoLevel;
    private Long     flowNodeId;
    private Long     flowMainId;
    private Timestamp     createTime;
    private Integer     namespaceId;
    private String     flowStepType;
    private Integer     flowVersion;
    private Long     id;
    private String     buttonName;


    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}

