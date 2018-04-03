package com.everhomes.point;

import com.everhomes.rest.point.AddUserPointCommand;
import com.everhomes.rest.point.GetUserTreasureCommand;
import com.everhomes.rest.point.GetUserTreasureResponse;
import com.everhomes.rest.point.PointType;

public interface UserPointService {
    @Deprecated
    void addPoint(AddUserPointCommand cmd);

    // get user treasure
    GetUserTreasureResponse getUserTreasure(GetUserTreasureCommand cmd);

    @Deprecated
    Integer getItemPoint(PointType pointType);
}
