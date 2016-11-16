package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;
import java.util.List;
import java.sql.Timestamp;

import com.everhomes.discover.ItemType;

public class FlowUserSelectionDTO {
    private Byte     status;
    private String     sourceType;
    private String     selectType;
    private Long     sourceId;
    private Long     flowMainId;
    private String     belongEntity;
    private Timestamp     createTime;
    private Integer     namespaceId;
    private Integer     flowVersion;
    private Long     id;
    private Long     belongTo;
    private String     belongType;


    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}

