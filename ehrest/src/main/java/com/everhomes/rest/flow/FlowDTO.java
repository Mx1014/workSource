package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;
import java.util.List;
import java.sql.Timestamp;

import com.everhomes.discover.ItemType;

public class FlowDTO {
    private String     flowName;
    private Long     flowMainId;
    private String     ownerType;
    private Long     endNode;
    private String     moduleType;
    private Long     id;
    private Integer     namespaceId;
    private String     stringTag5;
    private String     stringTag4;
    private Timestamp     stopTime;
    private String     stringTag1;
    private String     stringTag3;
    private String     stringTag2;
    private Byte     status;
    private Long     lastNode;
    private Integer     flowVersion;
    private Timestamp     runTime;
    private Timestamp     createTime;
    private Integer     moduleId;
    private Long     startNode;
    private Long     integralTag1;
    private Long     integralTag2;
    private Long     integralTag3;
    private Long     integralTag4;
    private Long     integralTag5;
    private Long     ownerId;
    private Long     superviserId;


    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}

