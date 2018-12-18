package com.everhomes.aclink.dingxin;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.aclink.DoorAuth;
import com.everhomes.rest.aclink.CreateDoorAuthCommand;
import com.everhomes.rest.aclink.VerifyDoorAuthCommand;
import com.everhomes.rest.user.UserInfo;

public interface DingxinService {

    String verifyDoorAuth(VerifyDoorAuthCommand cmd);

    JSONObject openDoor(String uid);

}
