// @formatter:off
package com.everhomes.zhenzhihui;

import com.everhomes.rest.zhenzhihui.CreateZhenZhiHuiUserAndEnterpriseInfoCommand;
import com.everhomes.rest.zhenzhihui.CreateZhenZhiHuiUserInfoCommand;
import com.everhomes.rest.zhenzhihui.CreateZhenZhiHuiUserInfoResponse;
import com.everhomes.rest.zhenzhihui.ZhenZhiHuiRedirectCommand;
import com.everhomes.user.UserLogin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ZhenZhiHuiService {
    String ssoService(HttpServletRequest request, HttpServletResponse response);
    String zhenzhihuiRedirect(ZhenZhiHuiRedirectCommand cmd);
    CreateZhenZhiHuiUserInfoResponse createZhenzhihuiUserInfo(CreateZhenZhiHuiUserInfoCommand cmd);
    CreateZhenZhiHuiUserInfoResponse createZhenzhihuiUserAndEnterpriseInfo(CreateZhenZhiHuiUserAndEnterpriseInfoCommand cmd);
}
