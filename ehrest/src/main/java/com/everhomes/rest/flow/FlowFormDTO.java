package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;
import java.util.List;
import java.sql.Timestamp;

import com.everhomes.discover.ItemType;

public class FlowFormDTO {
    private String     formDefault;
    private Long     integralTag3;
    private Long     integralTag5;
    private Long     integralTag1;
    private Long     flowMainId;
    private String     stringTag2;
    private Long     integralTag4;
    private Long     integralTag2;
    private Integer     namespaceId;
    private String     stringTag5;
    private String     stringTag4;
    private String     formRender;
    private String     stringTag3;
    private Integer     flowVersion;
    private String     stringTag1;
    private String     formType;
    private String     belongType;
    private Long     id;
    private Long     belongTo;
    private String     formName;


    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}

