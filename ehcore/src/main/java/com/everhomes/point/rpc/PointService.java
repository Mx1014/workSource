package com.everhomes.point.rpc;

import com.everhomes.rest.ApiConstants;
import com.everhomes.rest.user.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PointService extends PointServerRestService {

    public UserInfo getUserInfo(Long uid) {
        GetUserSnapshotInfoCommand cmd = new GetUserSnapshotInfoCommand();
        cmd.setUid(uid);

        GetUserSnapshotInfoRestResponse resp =
                call(ApiConstants.USER_GETUSERINFO_URL, cmd, GetUserSnapshotInfoRestResponse.class);
        return resp.getResponse();
    }

    public UserInfo getUserInfoByToken(String token) {
        ValidateUserTokenCommand cmd = new ValidateUserTokenCommand();
        cmd.setToken(token);

        ValidateTokenRestResponse resp = call(ApiConstants.USER_VALIDATETOKEN_URL, cmd, ValidateTokenRestResponse.class);
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

        GetUserByPhoneRestResponse resp = call(ApiConstants.USER_GETUSERBYPHONE_URL, cmd, GetUserByPhoneRestResponse.class);
        return resp.getResponse() ;
    }

    /**
     * 通过会员等级查询人员信息
     * @param memberLevel
     * @param namespaceId
     * @return
     */
    public List<UserInfo> getUsersByMemberLevel(Byte memberLevel  , Integer namespaceId){
        return null ;
    }

}
