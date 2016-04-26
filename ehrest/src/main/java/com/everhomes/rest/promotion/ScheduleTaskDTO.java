package com.everhomes.rest.promotion;

import com.everhomes.util.StringHelper;
import java.util.List;
import java.sql.Timestamp;

import com.everhomes.discover.ItemType;

public class ScheduleTaskDTO {
    private Byte     status;
    private String     progressData;
    private String     resourceType;
    private Long     resourceId;
    private String     ownerType;
    private Timestamp     createTime;
    private Integer     namespaceId;
    private Integer     progress;
    private Integer     processCount;
    private Long     ownerId;
    private Long     id;


    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
