package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

public class QryDoorAuthStatisticsCommand {

    private Long doorId;

    private Byte RightType;

    public Long getDoorId() {
        return doorId;
    }

    public void setDoorId(Long doorId) {
        this.doorId = doorId;
    }

    public Byte getRightType() {
        return RightType;
    }

    public void setRightType(Byte rightType) {
        RightType = rightType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
