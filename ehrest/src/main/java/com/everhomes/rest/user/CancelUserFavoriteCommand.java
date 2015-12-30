package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

public class CancelUserFavoriteCommand {
    private String targetType;
    private Long targetId;

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
