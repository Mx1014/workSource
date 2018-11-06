package com.everhomes.core.sdk.user;

import com.everhomes.core.sdk.NsDispatcher;
import com.everhomes.rest.user.*;
import org.springframework.stereotype.Service;

@Service
public class SdkUserService extends NsDispatcher {

    public UserInfo getUserInfo(Integer namespaceId, Long uid) {
        GetUserSnapshotInfoCommand cmd = new GetUserSnapshotInfoCommand();
        cmd.setUid(uid);

        GetUserSnapshotInfoRestResponse resp = dispatcher(namespaceId, sdkClient -> {
            return sdkClient.restCall("post", "/evh/user/getUserInfo", cmd, GetUserSnapshotInfoRestResponse.class);
        });
        return resp.getResponse();
    }

    public UserInfo getUserInfoByToken(Integer namespaceId, String token) {
        ValidateUserTokenCommand cmd = new ValidateUserTokenCommand();
        cmd.setToken(token);

        ValidateTokenRestResponse resp = dispatcher(namespaceId, sdkClient -> {
            return sdkClient.restCall("post", "/evh/user/validateToken", cmd, ValidateTokenRestResponse.class);
        });
        return resp.getResponse();
    }

    /**
     * 通过手机号及域空间查找用户
     * @param phone
     * @param namespaceId
     * @return
     */
    public UserDTO getUserByPhone(String phone  , Integer namespaceId){
        FindUserByPhoneCommand cmd = new FindUserByPhoneCommand();
        cmd.setNamespaceId(namespaceId);
        cmd.setPhone(phone);

        GetUserByPhoneRestResponse resp = dispatcher(namespaceId, sdkClient -> {
            return sdkClient.restCall("post", "/evh/user/getUserByPhone", cmd, GetUserByPhoneRestResponse.class);
        });
        return resp.getResponse() ;
    }

    /**
     * @author feiyue
     * @description 查询超级管理员的信息
     * @date  2018/8/28
     * @param
     * @return com.everhomes.rest.user.UserDTO
     */
    public UserDTO getTopAdministrator(Integer namespaceId, GetTopAdministratorCommand cmd) {
        GetTopAdministratorRestResponse resp = dispatcher(namespaceId, sdkClient -> {
            return sdkClient.restCall("post", "/evh/user/getTopAdministrator", cmd, GetTopAdministratorRestResponse.class);
        });
        return resp.getResponse();
    }
}