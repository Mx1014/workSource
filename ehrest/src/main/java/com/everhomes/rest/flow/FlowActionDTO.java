package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;
import java.util.List;
import java.sql.Timestamp;

import com.everhomes.discover.ItemType;

public class FlowActionDTO {
    private Byte     status;
    private Long     integralTag3;
    private String     stringTag3;
    private String     renderText;
    private Long     integralTag1;
    private Long     flowMainId;
    private String     stringTag2;
    private Long     integralTag4;
    private Long     integralTag2;
    private String     stepType;
    private Integer     namespaceId;
    private String     stringTag5;
    private String     stringTag4;
    private String     actionType;
    private Timestamp     createTime;
    private Integer     flowVersion;
    private String     stringTag1;
    private Long     integralTag5;
    private Long     id;
    private Long     belongTo;
    private String     belongType;


    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}

