package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;
import java.util.List;
import java.sql.Timestamp;

import com.everhomes.discover.ItemType;

public class FlowEventLogDTO {
    private Long     flowMainId;
    private String     logType;
    private String     logContent;
    private Long     flowActionId;
    private Long     id;
    private String     logTitle;
    private Long     flowUserId;
    private Long     subjectId;
    private String     stringTag5;
    private String     stringTag4;
    private String     stringTag1;
    private String     stringTag3;
    private String     stringTag2;
    private Long     flowSelectionId;
    private Long     flowNodeId;
    private Long     parentId;
    private Integer     flowVersion;
    private Integer     namespaceId;
    private Long     integralTag1;
    private Long     integralTag2;
    private Long     integralTag3;
    private Long     integralTag4;
    private Long     integralTag5;
    private Long     flowCaseId;
    private Long     flowButtonId;


    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}

