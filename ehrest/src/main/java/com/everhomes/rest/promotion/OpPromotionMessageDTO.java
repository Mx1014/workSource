package com.everhomes.rest.promotion;

import com.everhomes.util.StringHelper;
import java.util.List;
import java.sql.Timestamp;

import com.everhomes.discover.ItemType;

public class OpPromotionMessageDTO {
    private Long     messageSeq;
    private Long     metaAppId;
    private String     resultData;
    private String     messageText;
    private String     ownerType;
    private Timestamp     createTime;
    private Integer     namespaceId;
    private String     messageMeta;
    private Long     ownerId;
    private Long     senderUid;
    private Long     targetUid;
    private Long     id;


    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
