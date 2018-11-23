package com.everhomes.oauthapi;

import com.everhomes.rest.organization.OrganizationMemberDTO;
import com.everhomes.rest.user.UserInfo;
import com.everhomes.rest.user.UserInfoDTO;
import com.everhomes.rest.user.ZhenZhiHuiUserDetailInfo;

import java.util.List;

/**
 * Created by xq.tian on 2018/4/13.
 */
public interface OAuth2ApiService {

    UserInfo getUserInfoForInternal(Long grantorUid);

    UserInfoDTO getUserInfoForThird(Long grantorUid);

    ZhenZhiHuiUserDetailInfo getUserInfoForZhenZhiHui(Long grantorUid);

    List<OrganizationMemberDTO> getAuthenticationInfo(Long grantorUid);
}
