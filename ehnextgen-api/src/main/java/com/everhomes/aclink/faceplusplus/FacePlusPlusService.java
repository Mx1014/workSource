package com.everhomes.aclink.faceplusplus;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.aclink.AclinkGroup;
import com.everhomes.aclink.AesUserKey;
import com.everhomes.aclink.DoorAccess;
import com.everhomes.aclink.DoorAuth;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.rest.aclink.*;
import com.everhomes.user.User;
import com.google.gson.JsonObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface FacePlusPlusService {

    String login (String username, String password);

    JSONObject createUser (String cookie, Integer subjectType, String name, Integer start_time, Integer end_time);

    JSONObject updateUser (String cookie, Integer subjectType, String name, Integer start_time, Integer end_time);

    JSONObject uploadPhoto (String cookie, Integer subjectType, String name, Integer start_time, Integer end_time);


}
