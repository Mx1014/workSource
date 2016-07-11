package com.everhomes.point;

import com.everhomes.rest.point.AddUserPointCommand;
import com.everhomes.rest.point.GetUserPointCommand;
import com.everhomes.rest.point.GetUserPointResponse;
import com.everhomes.rest.point.GetUserTreasureCommand;
import com.everhomes.rest.point.GetUserTreasureResponse;
import com.everhomes.rest.point.PointType;

public interface UserPointService {
    void addPoint(AddUserPointCommand cmd);

    GetUserPointResponse getUserPoint(GetUserPointCommand cmd);

    // get user treasure
    GetUserTreasureResponse getUserTreasure(GetUserTreasureCommand cmd);

    Integer getItemPoint(PointType pointType);
}
