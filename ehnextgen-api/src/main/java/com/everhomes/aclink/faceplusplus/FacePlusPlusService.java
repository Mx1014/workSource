package com.everhomes.aclink.faceplusplus;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.aclink.AclinkGroup;
import com.everhomes.aclink.AesUserKey;
import com.everhomes.aclink.DoorAccess;
import com.everhomes.aclink.DoorAuth;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.rest.aclink.*;
import com.everhomes.rest.user.UserInfo;
import com.everhomes.user.User;
import com.google.gson.JsonObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;

public interface FacePlusPlusService {

    String login (String ip,String username, String password);

    JSONObject createUser (String cookie, Integer subjectType, String name, Long start_time, Long end_time, String ip);

    void deleteUser (String cookie, String subjectId, String ip);

    DoorAuth createAuth (DoorAuth doorAuth, CreateDoorAuthCommand cmd, UserInfo custom, String ip,String username, String password);

    void addPhoto(String url,Long authId,Long userId, String ip,String username, String password);

    String filetest (String url);

    String uploadPhoto (String cookie, String url, String subjectId,String ip);

    //    JSONObject updateUser (String cookie, Integer subjectType, String name, Integer start_time, Integer end_time);

}
