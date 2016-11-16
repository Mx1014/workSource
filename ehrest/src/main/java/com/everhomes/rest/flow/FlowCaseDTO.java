package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;
import java.util.List;
import java.sql.Timestamp;

import com.everhomes.discover.ItemType;

public class FlowCaseDTO {
    private Long     applyUserId;
    private Long     flowMainId;
    private String     ownerType;
    private Long     referId;
    private String     moduleType;
    private Long     id;
    private Long     processUserId;
    private String     stringTag5;
    private Timestamp     lastStepTime;
    private Integer     namespaceId;
    private String     content;
    private String     stringTag4;
    private String     stringTag1;
    private Long     rejectNodeId;
    private String     stringTag2;
    private Integer     rejectCount;
    private Byte     status;
    private String     stringTag3;
    private Integer     flowVersion;
    private Timestamp     createTime;
    private Integer     moduleId;
    private Long     currentNodeId;
    private Long     integralTag1;
    private Long     integralTag2;
    private Long     integralTag3;
    private Long     integralTag4;
    private Long     integralTag5;
    private String     referType;
    private Long     ownerId;


    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}

